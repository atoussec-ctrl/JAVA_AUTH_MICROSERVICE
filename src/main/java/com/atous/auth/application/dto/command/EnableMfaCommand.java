package com.atous.auth.application.dto.command;

public record EnableMfaCommand(java.util.UUID userId, String password) {}
