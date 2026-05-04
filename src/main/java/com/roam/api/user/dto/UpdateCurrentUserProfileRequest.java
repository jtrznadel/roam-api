package com.roam.api.user.dto;

import jakarta.validation.constraints.Size;

public record UpdateCurrentUserProfileRequest(
        @Size(max = 30)
        String displayName,

        @Size(max = 160)
        String bio,

        @Size(max = 500)
        String avatarUrl
) {
}
