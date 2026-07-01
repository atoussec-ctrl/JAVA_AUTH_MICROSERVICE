package com.atous.auth.presentation.dto.response;

public record TokenValidationResponse(boolean valid, java.util.UUID userId, String email, java.util.Set<String> roles, java.util.Set<String> permissions, java.time.Instant issuedAt, java.time.Instant expiresAt) {}
