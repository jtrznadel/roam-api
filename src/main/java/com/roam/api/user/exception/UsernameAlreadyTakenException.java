package com.roam.api.user.exception;

import com.roam.api.common.exception.ApiException;
import com.roam.api.common.exception.ErrorCode;

public class UsernameAlreadyTakenException extends ApiException {
    public UsernameAlreadyTakenException() {
        super(ErrorCode.USERNAME_ALREADY_TAKEN, "Username is already taken");

    }
}
