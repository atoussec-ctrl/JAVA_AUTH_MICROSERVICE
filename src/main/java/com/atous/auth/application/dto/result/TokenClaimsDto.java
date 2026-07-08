package com.atous.auth.application.dto.result;
import java.util.Set;


public record TokenClaimsDto(
    String tokenId, 
    java.util.UUID userId, 
    String subject,
    String email,
    String issuer,
    Set<String> audience, 
    Set<String> scopes, 
    Set<String> roles, 
    Set<String> permissions, 
    java.time.Instant issuedAt,
    java.time.Instant notBefore, 
    java.time.Instant expiresAt) {}
