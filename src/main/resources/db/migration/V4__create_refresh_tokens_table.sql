CREATE TABLE refresh_tokens
(
    id                   UUID PRIMARY KEY,
    user_id              UUID         NOT NULL,
    token_hash           VARCHAR(255) NOT NULL UNIQUE,
    status               VARCHAR(32)  NOT NULL,
    expires_at           TIMESTAMPTZ  NOT NULL,
    created_at           TIMESTAMPTZ  NOT NULL,
    revoked_at           TIMESTAMPTZ,
    rotated_at           TIMESTAMPTZ,
    replaced_by_token_id UUID,
    device_id            VARCHAR(128),
    device_name          VARCHAR(120),

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_refresh_tokens_replaced_by_token
        FOREIGN KEY (replaced_by_token_id)
            REFERENCES refresh_tokens (id),

    CONSTRAINT chk_refresh_tokens_status
        CHECK (status IN ('ACTIVE', 'REVOKED', 'ROTATED', 'EXPIRED'))
);

CREATE INDEX idx_refresh_tokens_user_id
    ON refresh_tokens (user_id);

CREATE INDEX idx_refresh_tokens_status
    ON refresh_tokens (status);

CREATE INDEX idx_refresh_tokens_expires_at
    ON refresh_tokens (expires_at);

CREATE INDEX idx_refresh_tokens_device_id
    ON refresh_tokens (device_id);
