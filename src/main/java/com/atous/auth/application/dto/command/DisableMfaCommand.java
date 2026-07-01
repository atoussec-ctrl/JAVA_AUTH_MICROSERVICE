package com.atous.auth.application.dto.command;

public record DisableMfaCommand(java.util.UUID userId, String password, String code) {}
