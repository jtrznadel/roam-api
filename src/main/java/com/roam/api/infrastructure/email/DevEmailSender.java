package com.roam.api.infrastructure.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
public class DevEmailSender implements EmailSender {

    @Override
    public void sendOtpCode(String email, String otpCode) {
        log.info("DEV OTP code for {}: {}", email, otpCode);
    }
}
