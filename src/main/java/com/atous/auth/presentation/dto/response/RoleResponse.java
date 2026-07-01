package com.atous.auth.presentation.dto.response;

public record RoleResponse(java.util.UUID id, String name, String description, java.util.Set<PermissionResponse> permissions, java.time.Instant createdAt, java.time.Instant updatedAt) {}
