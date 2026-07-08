package com.atous.auth.application.dto.result;
import java.time.Instant;
import java.util.UUID;

public record RegisterUserResult(
    UUID id, 
    String name, 
    String email, 
    boolean enabled, 
    boolean emailVerified, 
    Instant createdAt) {}
