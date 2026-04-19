package com.roam.api.auth.service;

import com.roam.api.auth.entity.RefreshToken;
import com.roam.api.auth.exception.InvalidRefreshTokenException;
import com.roam.api.auth.repository.RefreshTokenRepository;
import com.roam.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final int REFRESH_TOKEN_TTL_DAYS = 30;

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenHasher refreshTokenHasher;

    private final Clock clock;

    private record PersistedRefreshToken(
            String token,
            RefreshToken entity,
            UUID userId
    ) {
    }

    private PersistedRefreshToken createAndSaveToken(
            User user,
            String deviceId,
            String deviceName,
            Instant now
    ) {
        Instant expiresAt = now.plus(REFRESH_TOKEN_TTL_DAYS, ChronoUnit.DAYS);

        String token = refreshTokenGenerator.generateToken();
        String tokenHash = refreshTokenHasher.hash(token);

        RefreshToken refreshToken = RefreshToken.create(
                user,
                tokenHash,
                now,
                expiresAt,
                deviceId,
                deviceName
        );

        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new PersistedRefreshToken(token, savedRefreshToken, user.getId());
    }

    @Transactional
    public IssuedRefreshToken issueToken(User user, String deviceId, String deviceName) {
        Instant now = Instant.now(clock);

        PersistedRefreshToken issued = createAndSaveToken(user, deviceId, deviceName, now);

        return new IssuedRefreshToken(issued.token(), issued.userId());
    }

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public IssuedRefreshToken rotateToken(String rawRefreshToken) {
        String tokenHash = refreshTokenHasher.hash(rawRefreshToken);
        Instant now = Instant.now(clock);

        RefreshToken currentToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!currentToken.isActive()) {
            throw new InvalidRefreshTokenException();
        }

        if (currentToken.isExpired(now)) {
            currentToken.expire();
            throw new InvalidRefreshTokenException();
        }

        PersistedRefreshToken replacement = createAndSaveToken(
                currentToken.getUser(),
                currentToken.getDeviceId(),
                currentToken.getDeviceName(),
                now
        );

        currentToken.rotate(replacement.entity(), now);

        return new IssuedRefreshToken(replacement.token(), replacement.userId());
    }

    @Transactional
    public void revokeToken(String rawRefreshToken) {
        String tokenHash = refreshTokenHasher.hash(rawRefreshToken);
        Instant now = Instant.now(clock);

        refreshTokenRepository.findByTokenHash(tokenHash)
                .filter(RefreshToken::isActive)
                .ifPresent(refreshToken -> refreshToken.revoke(now));
    }
}
