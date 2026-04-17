package com.roam.api.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "email_otp_challenges")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailOtpChallenge {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(name = "otp_hash", nullable = false, length = 255)
    private String otpHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EmailOtpChallengeStatus status;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "max_attempts", nullable = false)
    private int maxAttempts;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "consumed_at")
    private Instant consumedAt;

    public static EmailOtpChallenge create(
            String email, String otpHash, Instant now, Instant expiresAt, int maxAttempts
    ) {
        EmailOtpChallenge challenge = new EmailOtpChallenge();
        challenge.id = UUID.randomUUID();
        challenge.email = email;
        challenge.otpHash = otpHash;
        challenge.status = EmailOtpChallengeStatus.PENDING;
        challenge.expiresAt = expiresAt;
        challenge.attemptCount = 0;
        challenge.maxAttempts = maxAttempts;
        challenge.createdAt = now;
        return challenge;
    }

    public void revoke() {
        this.status = EmailOtpChallengeStatus.REVOKED;
    }
}
