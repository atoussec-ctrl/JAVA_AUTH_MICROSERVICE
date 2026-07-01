package com.atous.auth.presentation.dto.request;

public record UpdateRoleRequest(@jakarta.validation.constraints.Size(min=2,max=80) String name, @jakarta.validation.constraints.Size(max=255) String description, java.util.Set<String> permissions) {}
