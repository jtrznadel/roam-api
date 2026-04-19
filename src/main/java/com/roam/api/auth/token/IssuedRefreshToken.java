package com.roam.api.auth.token;

import java.util.UUID;

public record IssuedRefreshToken(
        String token,
        UUID userId
) {
}

