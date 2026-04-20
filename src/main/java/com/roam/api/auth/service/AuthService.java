package com.roam.api.auth.service;

import com.roam.api.auth.dto.AuthTokensResponse;
import com.roam.api.auth.exception.InvalidOtpException;
import com.roam.api.auth.exception.InvalidRefreshTokenException;
import com.roam.api.auth.token.IssuedRefreshToken;
import com.roam.api.security.jwt.JwtService;
import com.roam.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailOtpService emailOtpService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public void requestEmailOtp(String email) {
        emailOtpService.requestOtp(email);
    }

    @Transactional(noRollbackFor = InvalidOtpException.class)
    public AuthTokensResponse verifyEmailOtp(
            String email,
            String otpCode,
            String deviceId,
            String deviceName
    ) {
        User user = emailOtpService.verifyOtp(email, otpCode);

        String accessToken = jwtService.generateAccessToken(user.getId());

        IssuedRefreshToken issuedRefreshToken = refreshTokenService.issueToken(
                user,
                deviceId,
                deviceName
        );

        return new AuthTokensResponse(
                accessToken,
                issuedRefreshToken.token(),
                "Bearer",
                jwtService.accessTokenTtlSeconds()
        );
    }

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public AuthTokensResponse refreshToken(String refreshToken) {
        IssuedRefreshToken issuedRefreshToken = refreshTokenService.rotateToken(refreshToken);

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

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }
}
