package com.roam.api.auth.dto;

public record RequestEmailOtpResponse(
        String message,
        String devOtpCode
) {
}
