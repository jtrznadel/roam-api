package com.roam.api.auth.service;

import com.roam.api.auth.entity.EmailOtpChallenge;
import com.roam.api.auth.entity.EmailOtpChallengeStatus;
import com.roam.api.auth.repository.EmailOtpChallengeRepository;
import com.roam.api.user.entity.User;
import com.roam.api.user.service.UserService;
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

    private final UserService userService;

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

    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public User verifyOtp(String email, String otpCode) {
        String normalizedEmail = normalizeEmail(email);
        Instant now = Instant.now(clock);

        EmailOtpChallenge challenge = emailOtpChallengeRepository.findFirstByEmailAndStatusOrderByCreatedAtDesc(normalizedEmail, EmailOtpChallengeStatus.PENDING).orElseThrow(() -> new IllegalArgumentException("Invalid or expired OTP"));

        if (challenge.isExpired(now)) {
            challenge.expire();
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        if (challenge.hasNoAttemptsRemaining()) {
            challenge.expire();
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        if (!otpHasher.matches(otpCode, challenge.getOtpHash())) {
            challenge.recordFailedAttempt();

            if (challenge.hasNoAttemptsRemaining()) {
                challenge.expire();
            }

            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        challenge.consume(now);

        return userService.findOrCreateAfterSuccessfulLogin(normalizedEmail);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
