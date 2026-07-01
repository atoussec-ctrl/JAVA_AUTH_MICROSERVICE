package com.atous.auth.presentation.dto.response;

public record AuditLogResponse(java.util.UUID id, java.util.UUID userId, String action, String ipAddress, String userAgent, boolean success, String reason, java.time.Instant createdAt) {}
