package com.roam.api.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_profiles")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserProfile {

    private static final String DEFAULT_DISPLAY_NAME = "Explorer";
    private static final String DEFAULT_EXPLORER_TITLE = "Rookie";
    private static final int DEFAULT_LEVEL = 1;

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;

    @Column(length = 20)
    private String username;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(length = 160)
    private String bio;

    @Column(name = "explorer_title", nullable = false, length = 30)
    private String explorerTitle;

    @Column(nullable = false)
    private int level;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static UserProfile create(User user, String username, Instant now) {
        UserProfile profile = new UserProfile();

        profile.id = UUID.randomUUID();
        profile.user = user;
        profile.displayName = DEFAULT_DISPLAY_NAME;
        profile.username = username;
        profile.avatarUrl = null;
        profile.bio = null;
        profile.explorerTitle = DEFAULT_EXPLORER_TITLE;
        profile.level = DEFAULT_LEVEL;
        profile.createdAt = now;
        profile.updatedAt = now;

        return profile;
    }

    public void completeSetup(String displayName, String username, String avatarUrl, Instant now) {
        this.displayName = displayName;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.updatedAt = now;

    }

    public void updateDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void clearAvatarUrl() {
        this.avatarUrl = null;
    }

    public void clearBio() {
        this.bio = null;
    }

    public void touch(Instant now) {
        this.updatedAt = now;
    }


}
