package com.atous.auth.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix="auth")
public record SecurityProperties(Jwt jwt, RefreshToken refreshToken, Cors cors) {
    public record Jwt(String issuer, long accessTokenTtlMinutes) {}
    public record RefreshToken(long ttlDays) {}
    public record Cors(List<String> allowedOrigins) {}
}
