package com.roam.api.user.service;

import com.roam.api.user.dto.CompleteProfileSetupRequest;
import com.roam.api.user.dto.UpdateCurrentUserProfileRequest;
import com.roam.api.user.entity.User;
import com.roam.api.user.entity.UserProfile;
import com.roam.api.user.exception.InvalidProfileStateException;
import com.roam.api.user.exception.ProfileNotFoundException;
import com.roam.api.user.exception.UserNotFoundException;
import com.roam.api.user.exception.UsernameAlreadyTakenException;
import com.roam.api.user.repository.UserProfileRepository;
import com.roam.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    private final Clock clock;

    @Transactional(readOnly = true)
    public UserProfile getByUserId(UUID userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(ProfileNotFoundException::new);
    }

    @Transactional
    public UserProfile completeProfileSetup(UUID userId, CompleteProfileSetupRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        String displayName = request.displayName().trim();
        String username = request.username().trim().toLowerCase(Locale.ROOT);
        String avatarUrl = normalizeOptionalValue(request.avatarUrl());

        if (userProfileRepository.existsByUsernameIgnoreCaseAndUserIdNot(username, userId)) {
            throw new UsernameAlreadyTakenException();
        }

        Instant now = Instant.now(clock);

        profile.completeSetup(displayName, username, avatarUrl, now);
        user.completeProfileSetup(now);

        return profile;
    }

    @Transactional
    public UserProfile updateCurrentUserProfile(UUID userId, UpdateCurrentUserProfileRequest request) {
        UserProfile profile = getByUserId(userId);

        boolean changed = false;

        if (request.displayName() != null) {
            String displayName = request.displayName().trim();

            if (displayName.isEmpty()) {
                throw new InvalidProfileStateException("Display name must not be blank");
            }

            profile.updateDisplayName(displayName);
            changed = true;
        }

        if (request.avatarUrl() != null) {
            String avatarUrl = normalizeOptionalValue(request.avatarUrl());

            if (avatarUrl == null) {
                profile.clearAvatarUrl();
            } else {
                profile.updateAvatarUrl(avatarUrl);
            }

            changed = true;
        }

        if (request.bio() != null) {
            String bio = normalizeOptionalValue(request.bio());

            if (bio == null) {
                profile.clearBio();
            } else {
                profile.updateBio(bio);
            }

            changed = true;
        }

        if (changed) {
            profile.touch(Instant.now(clock));
        }

        return profile;

    }

    private String normalizeOptionalValue(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed;
    }

}
