package com.roam.api.user.exception;

import com.roam.api.common.exception.ApiException;
import com.roam.api.common.exception.ErrorCode;

public class InvalidProfileStateException extends ApiException {
    public InvalidProfileStateException(String message) {
        super(ErrorCode.INVALID_PROFILE_STATE, message);
    }
}
