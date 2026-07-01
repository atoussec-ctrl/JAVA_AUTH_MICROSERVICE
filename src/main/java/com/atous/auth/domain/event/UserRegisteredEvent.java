package com.atous.auth.domain.event;

public record UserRegisteredEvent(java.util.UUID userId, String email, java.time.Instant occurredAt) {}
