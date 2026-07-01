package com.atous.auth.application.dto.command;

public record CreateRoleCommand(String name, String description, java.util.Set<String> permissions) {}
