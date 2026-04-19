package com.roam.api.auth.controller;

import com.roam.api.auth.dto.*;
import com.roam.api.auth.service.EmailOtpService;
import com.roam.api.auth.service.IssuedRefreshToken;
import com.roam.api.auth.service.RefreshTokenService;
import com.roam.api.security.jwt.JwtService;
import com.roam.api.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final EmailOtpService emailOtpService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("api/v1/auth/email/otp/request")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestEmailOtpResponse requestEmailOtp(
            @Valid @RequestBody RequestEmailOtpRequest request
    ) {
        String otpCode = emailOtpService.requestOtp(request.email());

        return new RequestEmailOtpResponse(
                "Verification code sent.",
                otpCode
        );
    }

    @PostMapping("api/v1/auth/email/otp/verify")
    public AuthTokensResponse verifyEmailOtp(
            @Valid @RequestBody VerifyEmailOtpRequest request
    ) {
        User user = emailOtpService.verifyOtp(request.email(), request.otpCode());

        String accessToken = jwtService.generateAccessToken(user.getId());

        IssuedRefreshToken issuedRefreshToken = refreshTokenService.issueToken(
                user,
                request.deviceId(),
                request.deviceName()
        );

        return new AuthTokensResponse(
                accessToken,
                issuedRefreshToken.token(),
                "Bearer",
                jwtService.accessTokenTtlSeconds()
        );
    }

    @PostMapping("api/v1/auth/token/refresh")
    public AuthTokensResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        IssuedRefreshToken issuedRefreshToken = refreshTokenService.rotateToken(request.refreshToken());

        String accessToken = jwtService.generateAccessToken(
                issuedRefreshToken.userId()
        );

        return new AuthTokensResponse(
                accessToken,
                issuedRefreshToken.token(),
                "Bearer",
                jwtService.accessTokenTtlSeconds()
        );
    }
}
