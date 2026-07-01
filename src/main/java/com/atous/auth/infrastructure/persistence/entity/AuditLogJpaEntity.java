package com.atous.auth.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="audit_logs")
public class AuditLogJpaEntity {
    @Id private UUID id;
    @Column(name="user_id") private UUID userId;
    @Column(nullable=false, length=120) private String action;
    @Column(name="ip_address", length=80) private String ipAddress;
    @Column(name="user_agent", length=512) private String userAgent;
    @Column(nullable=false) private boolean success;
    @Column(length=512) private String reason;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    protected AuditLogJpaEntity(){}
}
