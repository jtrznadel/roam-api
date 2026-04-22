package com.roam.api.user.dto;

import com.roam.api.user.entity.UserStatus;

import java.util.UUID;

public record CurrentUserResponse(
        UUID id,
        String email,
        UserStatus status,
        boolean onboardingCompleted,
        boolean profileSetupCompleted
) {
}
