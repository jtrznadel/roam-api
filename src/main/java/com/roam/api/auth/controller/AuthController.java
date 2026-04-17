package com.roam.api.auth.controller;

import com.roam.api.auth.dto.RequestEmailOtpRequest;
import com.roam.api.auth.dto.RequestEmailOtpResponse;
import com.roam.api.auth.dto.VerifyEmailOtpRequest;
import com.roam.api.auth.dto.VerifyEmailOtpResponse;
import com.roam.api.auth.service.EmailOtpService;
import com.roam.api.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final EmailOtpService emailOtpService;

    @PostMapping("api/v1/auth/email/otp/request")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestEmailOtpResponse requestEmailOtp(
            @Valid @RequestBody RequestEmailOtpRequest request
    ) {
        String otpCode = emailOtpService.requestOtp(request.email());

        return new RequestEmailOtpResponse(
                "Verification code sent.",
                otpCode
        );
    }

    @PostMapping("api/v1/auth/email/otp/verify")
    public VerifyEmailOtpResponse verifyEmailOtp(
            @Valid @RequestBody VerifyEmailOtpRequest request
    ) {
        User user = emailOtpService.verifyOtp(request.email(), request.otpCode());

        return new VerifyEmailOtpResponse("Email verified", user.getId());
    }
}
