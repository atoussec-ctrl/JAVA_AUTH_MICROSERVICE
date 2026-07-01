package com.atous.auth.domain.valueobject;

public record PasswordHash(String value) {
    public PasswordHash {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Password hash is required");
        value = value.trim();
    }
    public static PasswordHash of(String value) { return new PasswordHash(value); }
}
