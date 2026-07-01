package com.atous.auth.application.dto.view;

public record RoleView(java.util.UUID id, String name, String description, java.util.Set<PermissionView> permissions, java.time.Instant createdAt, java.time.Instant updatedAt) {}
