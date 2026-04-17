package com.roam.api.auth.dto;

import java.util.UUID;

public record VerifyEmailOtpResponse(
        String message,
        UUID userId
) {
}
