package com.atous.auth.domain.valueobject;

public record IpAddress(String value) {
    public IpAddress {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("IP address is required");
        value = value.trim();
    }

    public static IpAddress of(String value) {
        return new IpAddress(value);
    }
}
