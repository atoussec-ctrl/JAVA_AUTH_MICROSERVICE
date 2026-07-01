package com.atous.auth.application.dto.command;

public record UpdateRoleCommand(java.util.UUID roleId, String name, String description, java.util.Set<String> permissions) {}
