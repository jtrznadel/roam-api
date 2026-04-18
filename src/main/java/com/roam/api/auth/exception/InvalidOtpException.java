package com.roam.api.auth.exception;

import com.roam.api.common.exception.ApiException;
import com.roam.api.common.exception.ErrorCode;

public class InvalidOtpException extends ApiException {

    public InvalidOtpException() {
        super(ErrorCode.INVALID_OTP, "Invalid or expired OTP.");
    }
}
