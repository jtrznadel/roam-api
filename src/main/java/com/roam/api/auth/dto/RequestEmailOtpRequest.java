package com.roam.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestEmailOtpRequest(
        @NotBlank
        @Email
        String email
) {
}
