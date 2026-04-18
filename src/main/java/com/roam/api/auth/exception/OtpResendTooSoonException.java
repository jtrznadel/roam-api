package com.roam.api.auth.exception;

import com.roam.api.common.exception.ApiException;
import com.roam.api.common.exception.ErrorCode;

public class OtpResendTooSoonException extends ApiException {

    public OtpResendTooSoonException() {
        super(ErrorCode.OTP_RESEND_TOO_SOON, "Please wait before requesting another code");
    }
}
