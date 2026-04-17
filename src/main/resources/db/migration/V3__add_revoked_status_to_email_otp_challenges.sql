ALTER TABLE email_otp_challenges
    DROP CONSTRAINT chk_email_otp_challenges_status;

ALTER TABLE email_otp_challenges
    ADD CONSTRAINT chk_email_otp_challenges_status
        CHECK (status IN ('PENDING', 'VERIFIED', 'EXPIRED', 'CONSUMED', 'REVOKED'));