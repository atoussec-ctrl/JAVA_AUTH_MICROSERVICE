CREATE TABLE mfa_devices (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    secret_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    verified_at TIMESTAMP
);
CREATE INDEX idx_mfa_devices_user_id ON mfa_devices(user_id);
