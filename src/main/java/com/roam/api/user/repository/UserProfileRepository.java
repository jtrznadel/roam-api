package com.roam.api.user.repository;

import com.roam.api.user.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<UserProfile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    boolean existsByUsernameIgnoreCase(String userName);

    @EntityGraph(attributePaths = "user")
    boolean existsByUsernameIgnoreCaseAndUserIdNot(String userName, UUID userId);
}
