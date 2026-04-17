CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(320) NOT NULL UNIQUE,
                       status VARCHAR(32) NOT NULL,
                       email_verified_at TIMESTAMPTZ,
                       created_at TIMESTAMPTZ NOT NULL,
                       updated_at TIMESTAMPTZ NOT NULL,
                       last_login_at TIMESTAMPTZ,
                       deleted_at TIMESTAMPTZ,

                       CONSTRAINT chk_users_status
                           CHECK (status IN ('ACTIVE', 'DISABLED', 'DELETED'))
);

