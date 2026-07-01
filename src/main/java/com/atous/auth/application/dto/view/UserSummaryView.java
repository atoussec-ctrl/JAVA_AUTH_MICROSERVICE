package com.atous.auth.application.dto.view;

public record UserSummaryView(java.util.UUID id, String name, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled, java.util.Set<String> roles, java.util.Set<String> permissions) {}
