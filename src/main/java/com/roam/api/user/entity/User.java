package com.roam.api.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UserStatus status;

    @Column(name = "email_verified_at")
    private Instant emailVerifiedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "onboarding_completed", nullable = false)
    private boolean onboardingCompleted;

    public static User create(String email, Instant now) {
        User user = new User();
        user.id = UUID.randomUUID();
        user.email = email;
        user.status = UserStatus.ACTIVE;
        user.emailVerifiedAt = now;
        user.createdAt = now;
        user.updatedAt = now;
        user.onboardingCompleted = false;
        return user;
    }

    public void recordLogin(Instant now) {
        this.lastLoginAt = now;
        this.updatedAt = now;
    }

    public void completeOnboarding(Instant now) {
        this.onboardingCompleted = true;
        this.updatedAt = now;
    }
}
