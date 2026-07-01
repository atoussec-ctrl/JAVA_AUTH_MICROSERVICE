package com.atous.auth.application.dto.view;

public record UserView(java.util.UUID id, String name, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled, java.util.Set<RoleView> roles, java.time.Instant createdAt, java.time.Instant updatedAt, java.time.Instant lastLoginAt) {}
