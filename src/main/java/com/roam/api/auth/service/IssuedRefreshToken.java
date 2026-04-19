package com.roam.api.auth.service;

import java.util.UUID;

public record IssuedRefreshToken(
        String token,
        UUID userId
) {
}

