package com.atous.auth.application.dto.command;

public record UpdateUserStatusCommand(java.util.UUID userId, boolean enabled) {}
