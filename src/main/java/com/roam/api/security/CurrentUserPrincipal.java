package com.roam.api.security;

import java.util.UUID;

public record CurrentUserPrincipal(
        UUID userId
) {
}
