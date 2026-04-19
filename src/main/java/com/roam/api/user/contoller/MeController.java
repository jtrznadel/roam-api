package com.roam.api.user.contoller;

import com.roam.api.security.CurrentUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MeController {

    @GetMapping("api/v1/me")
    public MeResponse me(
            @AuthenticationPrincipal CurrentUserPrincipal currentUser
    ) {
        return new MeResponse(currentUser.userId());
    }

    public record MeResponse(
            UUID userId
    ) {

    }
}
