package com.roam.api.common.exception;

import com.roam.api.common.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception) {
        return ResponseEntity
                .status(resolveStatus(exception.getErrorCode()))
                .body(new ApiErrorResponse(
                        exception.getErrorCode().name(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(
                        ErrorCode.VALIDATION_FAILED.name(),
                        "Request validation failed."
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(
                        ErrorCode.INTERNAL_ERROR.name(),
                        "Unexpected server error."
                ));
    }


    private HttpStatus resolveStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case INVALID_OTP, VALIDATION_FAILED -> HttpStatus.BAD_REQUEST;
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
