package com.roam.api.user.service;

import com.roam.api.user.entity.User;
import com.roam.api.user.entity.UserProfile;
import com.roam.api.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserProfileBootstrapService {

    private final UserProfileRepository userProfileRepository;
    ;
    private final Clock clock;

    public void createDefaultProfile(User user) {
        if (userProfileRepository.existsByUserId(user.getId())) {
            return;
        }

        Instant now = Instant.now(clock);
        String usernameSuggestion = createAvailableUsernameSuggestion(user.getEmail());


        UserProfile profile = UserProfile.create(user, usernameSuggestion, now);
        userProfileRepository.save(profile);
    }

    private String createAvailableUsernameSuggestion(String email) {
        String normalizedUsername = createUsernameSuggestion(email);

        if (normalizedUsername == null) {
            return null;
        }

        if (userProfileRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            return null;
        }

        return normalizedUsername;
    }

    private String createUsernameSuggestion(String email) {
        String localPart = email.split("@", 2)[0];

        return normalizeUsername(localPart);
    }

    private String normalizeUsername(String value) {
        String normalized = value.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9._]", "")
                .substring(0, Math.min(value.length(), 20));

        if (normalized.isBlank()) {
            return null;
        }

        return normalized;
    }
}
