package com.atous.auth.domain.event;

public record UserLoggedInEvent(java.util.UUID userId, String ipAddress, java.time.Instant occurredAt) {}
