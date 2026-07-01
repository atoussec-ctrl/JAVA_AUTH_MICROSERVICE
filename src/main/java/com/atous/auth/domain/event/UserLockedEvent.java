package com.atous.auth.domain.event;

public record UserLockedEvent(java.util.UUID userId, String reason, java.time.Instant occurredAt) {}
