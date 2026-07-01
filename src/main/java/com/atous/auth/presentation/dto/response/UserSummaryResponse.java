package com.atous.auth.presentation.dto.response;

public record UserSummaryResponse(java.util.UUID id, String name, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled, java.util.Set<String> roles, java.util.Set<String> permissions) {}
