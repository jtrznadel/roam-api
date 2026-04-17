package com.roam.api.auth.repository;


import com.roam.api.auth.entity.EmailOtpChallenge;
import com.roam.api.auth.entity.EmailOtpChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailOtpChallengeRepository extends JpaRepository<EmailOtpChallenge, UUID> {

    Optional<EmailOtpChallenge> findFirstByEmailAndStatusOrderByCreatedAtDesc(
            String email, EmailOtpChallengeStatus status
    );

    List<EmailOtpChallenge> findAllByEmailAndStatus(
            String email,
            EmailOtpChallengeStatus status
    );
}
