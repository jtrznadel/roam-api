CREATE TABLE user_profiles
(
    id             UUID PRIMARY KEY,
    user_id        UUID        NOT NULL,
    display_name   VARCHAR(20) NOT NULL,
    username       VARCHAR(20),
    avatar_url     VARCHAR(255),
    bio            VARCHAR(160),
    explorer_title VARCHAR(30) NOT NULL DEFAULT 'Rookie',
    level          INTEGER     NOT NULL DEFAULT 1,
    created_at     TIMESTAMP   NOT NULL,
    updated_at     TIMESTAMP   NOT NULL,

    CONSTRAINT fk_user_profiles_users
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_user_profiles_user_id
        UNIQUE (user_id),

    CONSTRAINT chk_user_profiles_level
        CHECK (level >= 1)
);

CREATE UNIQUE INDEX uq_user_profiles_username_lower
    ON user_profiles (LOWER(username))
    WHERE username IS NOT NULL;