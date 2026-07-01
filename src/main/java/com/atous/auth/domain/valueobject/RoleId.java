package com.atous.auth.domain.valueobject;

import java.util.UUID;

public record RoleId(UUID value) {
    public RoleId {
        if (value == null) throw new IllegalArgumentException("Role id is required");
    }
    public static RoleId newId() { return new RoleId(UUID.randomUUID()); }
    public static RoleId of(UUID value) { return new RoleId(value); }
}
