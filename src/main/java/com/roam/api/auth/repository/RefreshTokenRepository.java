package com.roam.api.auth.repository;

import com.roam.api.auth.entity.RefreshToken;
import com.roam.api.auth.entity.RefreshTokenStatus;
import com.roam.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUserAndStatus(User user, RefreshTokenStatus status);
}