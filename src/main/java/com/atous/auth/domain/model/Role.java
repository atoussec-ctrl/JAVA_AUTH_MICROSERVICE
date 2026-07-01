package com.atous.auth.domain.model;

import com.atous.auth.domain.valueobject.RoleId;
import java.time.Instant;
import java.util.Set;

public record Role(RoleId id, String name, String description, Set<Permission> permissions, Instant createdAt, Instant updatedAt) {
    public Role {
        if (id == null) throw new IllegalArgumentException("Role id is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Role name is required");
        name = name.trim().toUpperCase();
        description = description == null ? "" : description.trim();
        permissions = permissions == null ? Set.of() : Set.copyOf(permissions);
    }
}
