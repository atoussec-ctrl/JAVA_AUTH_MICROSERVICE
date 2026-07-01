package com.atous.auth.application.dto.command;

public record VerifyMfaCommand(java.util.UUID userId, String code) {}
