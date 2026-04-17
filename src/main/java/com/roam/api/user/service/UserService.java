package com.roam.api.user.service;

import com.roam.api.user.entity.User;
import com.roam.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Clock clock;

    @Transactional
    public User findOrCreateAfterSuccessfulLogin(String email) {
        String normalizedEmail = normalizeEmail(email);
        Instant now = Instant.now(clock);

        return userRepository.findByEmail(normalizedEmail)
                .map(user -> {
                    user.recordLogin(now);
                    return user;
                }).orElseGet(() -> userRepository.save(User.create(normalizedEmail, now)));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
