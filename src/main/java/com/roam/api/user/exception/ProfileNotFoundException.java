package com.roam.api.user.exception;

import com.roam.api.common.exception.ApiException;
import com.roam.api.common.exception.ErrorCode;

public class ProfileNotFoundException extends ApiException {

    public ProfileNotFoundException() {
        super(ErrorCode.PROFILE_NOT_FOUND, "Profile not found");
    }
}
