package com.atous.auth.presentation.dto.response;

public record RegisterUserResponse(java.util.UUID id, String name, String email, boolean enabled, boolean emailVerified, java.time.Instant createdAt) {}
