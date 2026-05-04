package com.roam.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompleteProfileSetupRequest(

        @NotBlank
        @Size(max = 30)
        String displayName,

        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^[a-z0-9._]+$", message = "Username can only contain lowercase letters, numbers, dots and underscores")
        String username,

        @Size(max = 500)
        String avatarUrl
) {
}
