package com.roam.api.auth.service;

import com.roam.api.auth.entity.EmailOtpChallenge;
import com.roam.api.auth.entity.EmailOtpChallengeStatus;
import com.roam.api.auth.repository.EmailOtpChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailOtpService {
    private static final int OTP_TTL_MINUTES = 10;
    private static final int OTP_MAX_ATTEMPTS = 5;

    private final EmailOtpChallengeRepository emailOtpChallengeRepository;
    private final OtpCodeGenerator otpCodeGenerator;
    private final OtpHasher otpHasher;
    private final Clock clock;

    @Transactional
    public String requestOtp(String email) {
        String normalizedEmail = normalizeEmail(email);
        Instant now = Instant.now(clock);
        Instant expiresAt = now.plus(OTP_TTL_MINUTES, ChronoUnit.MINUTES);

        emailOtpChallengeRepository.findAllByEmailAndStatus(normalizedEmail, EmailOtpChallengeStatus.PENDING)
                .forEach(EmailOtpChallenge::revoke);

        String otpCode = otpCodeGenerator.generateSixDigitCode();
        String otpHash = otpHasher.hash(otpCode);

        EmailOtpChallenge challenge = EmailOtpChallenge.create(normalizedEmail, otpHash, now, expiresAt, OTP_MAX_ATTEMPTS);

        emailOtpChallengeRepository.save(challenge);

        //temp for development
        return otpCode;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
