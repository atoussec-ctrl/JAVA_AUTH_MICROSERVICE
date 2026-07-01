package com.atous.auth.application.dto.result;

public record PasswordOperationResult(boolean success, String message, java.time.Instant executedAt) {}
