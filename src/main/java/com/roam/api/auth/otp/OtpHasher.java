package com.roam.api.auth.otp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class OtpHasher {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String hash(String otpCode) {
        return passwordEncoder.encode(otpCode);
    }

    public boolean matches(String rawOtpCode, String otpHash) {
        return passwordEncoder.matches(rawOtpCode, otpHash);
    }
}
