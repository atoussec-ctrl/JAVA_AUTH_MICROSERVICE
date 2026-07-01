package com.atous.auth.presentation.dto.response;

public record UserResponse(java.util.UUID id, String name, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled, java.util.Set<RoleResponse> roles, java.time.Instant createdAt, java.time.Instant updatedAt, java.time.Instant lastLoginAt) {}
