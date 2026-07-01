package com.atous.auth.domain.model;

import com.atous.auth.domain.valueobject.UserId;
import java.time.Instant;
import java.util.UUID;

public record AuditLog(
    UUID id, 
    UserId userId, 
    String action, 
    String ipAddress, 
    String userAgent, 
    boolean success, 
    String reason, 
    Instant createdAt
){}
