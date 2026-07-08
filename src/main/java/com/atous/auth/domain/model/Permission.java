package com.atous.auth.domain.model;

import com.atous.auth.domain.valueobject.PermissionId;

public record Permission(PermissionId id, String name, String description) {
    public Permission {
        if (id == null)
            throw new IllegalArgumentException("Permission id is required");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Permission name is required");
        name = name.trim();
        description = description == null ? "" : description.trim();
    }
}
