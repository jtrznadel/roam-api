package com.roam.api.infrastructure.email;

public interface EmailSender {
    void sendOtpCode(String email, String otpCode);
}

