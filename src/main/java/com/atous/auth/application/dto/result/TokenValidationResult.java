package com.atous.auth.application.dto.result;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record TokenValidationResult(
    boolean valid, 
    UUID userId,
    String email, 
    Set<String> roles, 
    Set<String> permissions, 
    Instant issuedAt, 
    Instant expiresAt) {}
