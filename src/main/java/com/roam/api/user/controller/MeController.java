package com.roam.api.user.controller;

import com.roam.api.security.CurrentUserPrincipal;
import com.roam.api.user.dto.CompleteProfileSetupRequest;
import com.roam.api.user.dto.CurrentUserProfileResponse;
import com.roam.api.user.dto.CurrentUserResponse;
import com.roam.api.user.entity.User;
import com.roam.api.user.entity.UserProfile;
import com.roam.api.user.mapper.UserMapper;
import com.roam.api.user.mapper.UserProfileMapper;
import com.roam.api.user.service.UserProfileService;
import com.roam.api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @GetMapping("/api/v1/me")
    public CurrentUserResponse me(
            @AuthenticationPrincipal CurrentUserPrincipal currentUser
    ) {
        User user = userService.getById(currentUser.userId());

        return userMapper.toResponse(user);
    }

    @PatchMapping("/api/v1/me/onboarding")
    public CurrentUserResponse completeOnboarding(
            @AuthenticationPrincipal CurrentUserPrincipal currentUser
    ) {
        User user = userService.completeOnboarding(currentUser.userId());

        return userMapper.toResponse(user);
    }

    @GetMapping("/api/v1/me/profile")
    public CurrentUserProfileResponse profile(
            @AuthenticationPrincipal CurrentUserPrincipal currentUser
    ) {
        UserProfile userProfile = userProfileService.getByUserId(currentUser.userId());

        return userProfileMapper.toResponse(userProfile);
    }

    @PatchMapping("/api/v1/me/profile/setup")
    public CurrentUserProfileResponse completeProfileSetup(
            @AuthenticationPrincipal CurrentUserPrincipal currentUser,
            @Valid @RequestBody CompleteProfileSetupRequest request
    ) {
        UserProfile userProfile = userProfileService.completeProfileSetup(
                currentUser.userId(),
                request
        );

        return userProfileMapper.toResponse(userProfile);
    }
}
