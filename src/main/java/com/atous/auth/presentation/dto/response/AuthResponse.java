package com.atous.auth.presentation.dto.response;

public record AuthResponse(String accessToken, String refreshToken, String tokenType, long expiresIn, java.time.Instant issuedAt, java.time.Instant expiresAt, UserSummaryResponse user) {}
