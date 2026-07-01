package com.atous.auth.domain.valueobject;

public record TokenHash(String value) {
    public TokenHash {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Token hash is required");
        value = value.trim();
    }
    public static TokenHash of(String value) { return new TokenHash(value); }
}
