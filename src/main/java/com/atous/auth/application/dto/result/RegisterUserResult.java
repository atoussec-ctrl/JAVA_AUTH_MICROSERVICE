package com.atous.auth.application.dto.result;

public record RegisterUserResult(java.util.UUID id, String name, String email, boolean enabled, boolean emailVerified, java.time.Instant createdAt) {}
