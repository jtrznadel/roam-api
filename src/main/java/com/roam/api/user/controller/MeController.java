package com.roam.api.user.controller;

import com.roam.api.security.CurrentUserPrincipal;
import com.roam.api.user.dto.CurrentUserResponse;
import com.roam.api.user.entity.User;
import com.roam.api.user.mapper.UserMapper;
import com.roam.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;
    private final UserMapper userMapper;

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
}
