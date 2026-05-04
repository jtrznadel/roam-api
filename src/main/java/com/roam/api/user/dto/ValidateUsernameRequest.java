package com.roam.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ValidateUsernameRequest(
        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^[a-z0-9._]+$")
        String username
) {
}
