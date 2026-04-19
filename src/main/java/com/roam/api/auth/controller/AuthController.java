package com.roam.api.auth.controller;

import com.roam.api.auth.dto.*;
import com.roam.api.auth.service.AuthService;
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

    private final AuthService authService;

    @PostMapping("/api/v1/auth/email/otp/request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RequestEmailOtpResponse requestEmailOtp(
            @Valid @RequestBody RequestEmailOtpRequest request
    ) {
        authService.requestEmailOtp(request.email());

        return new RequestEmailOtpResponse("Verification code sent.");

    }

    @PostMapping("/api/v1/auth/email/otp/verify")
    public AuthTokensResponse verifyEmailOtp(
            @Valid @RequestBody VerifyEmailOtpRequest request
    ) {
        return authService.verifyEmailOtp(
                request.email(),
                request.otpCode(),
                request.deviceId(),
                request.deviceName()
        );
    }

    @PostMapping("/api/v1/auth/token/refresh")
    public AuthTokensResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return authService.refreshToken(request.refreshToken());
    }

    @PostMapping("/api/v1/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        authService.logout(request.refreshToken());
    }
}
