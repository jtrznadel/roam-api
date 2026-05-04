package com.roam.api.user.dto;

import java.time.Instant;
import java.util.UUID;

public record CurrentUserProfileResponse(
        UUID id,
        UUID userId,
        String email,
        String displayName,
        String username,
        String avatarUrl,
        String bio,
        String explorerTitle,
        int level,
        Instant createdAt,
        Instant updatedAt
) {
}
