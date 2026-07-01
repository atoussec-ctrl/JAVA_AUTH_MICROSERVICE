package com.atous.auth.domain.event;

public record PasswordChangedEvent(java.util.UUID userId, java.time.Instant occurredAt) {}
