package com.atous.auth.application.dto.result;

public record TokenClaimsDto(String tokenId, java.util.UUID userId, String subject, String issuer, java.util.Set<String> audience, java.util.Set<String> scopes, java.util.Set<String> roles, java.util.Set<String> permissions, java.time.Instant issuedAt, java.time.Instant notBefore, java.time.Instant expiresAt) {}
