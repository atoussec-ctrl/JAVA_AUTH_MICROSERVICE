package com.atous.auth.application.dto.view;

public record AuditLogView(java.util.UUID id, java.util.UUID userId, String action, String ipAddress, String userAgent, boolean success, String reason, java.time.Instant createdAt) {}
