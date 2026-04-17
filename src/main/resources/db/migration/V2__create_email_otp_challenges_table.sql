CREATE TABLE email_otp_challenges
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(320) NOT NULL,
    otp_hash      VARCHAR(255) NOT NULL,
    status        VARCHAR(32)  NOT NULL,
    expires_at    TIMESTAMPTZ  NOT NULL,
    attempt_count INTEGER      NOT NULL,
    max_attempts  INTEGER      NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    verified_at   TIMESTAMPTZ,
    consumed_at   TIMESTAMPTZ,

    CONSTRAINT chk_email_otp_challenges_status
        CHECK ( status in ('PENDING', 'VERIFIED', 'EXPIRED', 'CONSUMED')),

    CONSTRAINT chk_email_otp_challenges_attempt_count
        CHECK (attempt_count >= 0),

    CONSTRAINT chk_email_otp_challenges_max_attempts
        CHECK ( max_attempts > 0 )
);

CREATE INDEX idx_email_otp_challenges_email_status
    ON email_otp_challenges (email, status);

CREATE INDEX idx_email_otp_challenges_expires_at
    ON email_otp_challenges (expires_at);