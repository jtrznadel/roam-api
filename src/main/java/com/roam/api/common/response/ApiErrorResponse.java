package com.roam.api.common.response;

public record ApiErrorResponse(
        String code,
        String message
) {
}
