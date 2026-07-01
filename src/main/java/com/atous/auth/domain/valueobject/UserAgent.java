package com.atous.auth.domain.valueobject;

public record UserAgent(String value) {
    public UserAgent {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("User agent is required");
        value = value.trim();
    }
    public static UserAgent of(String value) { return new UserAgent(value); }
}
