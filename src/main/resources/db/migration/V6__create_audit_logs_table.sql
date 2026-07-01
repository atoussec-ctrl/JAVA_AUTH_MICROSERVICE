CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    user_id UUID,
    action VARCHAR(120) NOT NULL,
    ip_address VARCHAR(80),
    user_agent VARCHAR(512),
    success BOOLEAN NOT NULL,
    reason VARCHAR(512),
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
