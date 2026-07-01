package com.atous.auth.application.dto.result;

public record TokenValidationResult(boolean valid, java.util.UUID userId, String email, java.util.Set<String> roles, java.util.Set<String> permissions, java.time.Instant issuedAt, java.time.Instant expiresAt) {}
