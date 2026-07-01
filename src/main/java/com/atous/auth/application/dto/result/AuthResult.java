package com.atous.auth.application.dto.result;

public record AuthResult(String accessToken, String refreshToken, String tokenType, long expiresIn, java.time.Instant issuedAt, java.time.Instant expiresAt, com.atous.auth.application.dto.view.UserSummaryView user) {}
