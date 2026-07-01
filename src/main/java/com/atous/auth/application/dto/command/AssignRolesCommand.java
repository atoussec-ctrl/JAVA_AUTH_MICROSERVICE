package com.atous.auth.application.dto.command;

public record AssignRolesCommand(java.util.UUID userId, java.util.Set<java.util.UUID> roleIds) {}
