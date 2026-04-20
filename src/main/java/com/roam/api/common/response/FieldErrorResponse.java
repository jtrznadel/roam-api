package com.roam.api.common.response;

public record FieldErrorResponse(
        String field,
        String message
) {
}
