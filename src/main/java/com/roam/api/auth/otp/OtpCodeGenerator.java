package com.roam.api.auth.otp;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpCodeGenerator {
    private static final int OTP_BOUND = 1_000_000;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateSixDigitCode() {
        int code = secureRandom.nextInt(OTP_BOUND);
        return String.format("%06d", code);
    }
}
