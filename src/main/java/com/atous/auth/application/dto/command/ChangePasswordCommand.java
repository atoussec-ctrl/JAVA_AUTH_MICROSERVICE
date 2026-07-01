package com.atous.auth.application.dto.command;

public record ChangePasswordCommand(java.util.UUID userId, String currentPassword, String newPassword, String passwordConfirmation, String ipAddress, String userAgent) {}
