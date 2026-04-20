package com.roam.api.common.response;

import java.util.List;

public record ApiErrorResponse(
        String code,
        String message,
        List<FieldErrorResponse> errors
) {
    public ApiErrorResponse(String code, String message) {
        this(code, message, List.of());
    }
}
