package com.atous.auth.application.dto.result;

public record AccessTokenResult(String accessToken, String tokenType, long expiresIn, java.time.Instant issuedAt, java.time.Instant expiresAt) {}
