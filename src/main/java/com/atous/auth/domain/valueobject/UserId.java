package com.atous.auth.domain.valueobject;

import java.util.UUID;

public record UserId(UUID value) {

    public UserId {
        if (value == null)
            throw new IllegalArgumentException("User id is required");
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}
