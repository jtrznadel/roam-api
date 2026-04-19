package com.roam.api.auth.entity;

import com.roam.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash", nullable = false, unique = true, length = 255)
    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private RefreshTokenStatus status;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "rotated_at")
    private Instant rotatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by_token_id")
    private RefreshToken replacedByToken;

    @Column(name = "device_id", length = 128)
    private String deviceId;

    @Column(name = "device_name", length = 120)
    private String deviceName;

    public static RefreshToken create(
            User user,
            String tokenHash,
            Instant now,
            Instant expiresAt,
            String deviceId,
            String deviceName
    ) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.id = UUID.randomUUID();
        refreshToken.user = user;
        refreshToken.tokenHash = tokenHash;
        refreshToken.status = RefreshTokenStatus.ACTIVE;
        refreshToken.expiresAt = expiresAt;
        refreshToken.createdAt = now;
        refreshToken.deviceId = deviceId;
        refreshToken.deviceName = deviceName;
        return refreshToken;
    }

    public boolean isExpired(Instant now) {
        return !expiresAt.isAfter(now);
    }

    public boolean isActive() {
        return status == RefreshTokenStatus.ACTIVE;
    }

    public void expire() {
        this.status = RefreshTokenStatus.EXPIRED;
    }

    public void revoke(Instant now) {
        this.status = RefreshTokenStatus.REVOKED;
        this.revokedAt = now;
    }

    public void rotate(RefreshToken replacement, Instant now) {
        this.status = RefreshTokenStatus.ROTATED;
        this.replacedByToken = replacement;
        this.rotatedAt = now;
    }

}
