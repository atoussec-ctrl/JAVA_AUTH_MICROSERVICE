package com.atous.auth.domain.valueobject;

import java.util.UUID;

public record PermissionId(UUID value) {
    public PermissionId {
        if (value == null) throw new IllegalArgumentException("Permission id is required");
    }
    public static PermissionId newId() { return new PermissionId(UUID.randomUUID()); }
    public static PermissionId of(UUID value) { return new PermissionId(value); }
}
