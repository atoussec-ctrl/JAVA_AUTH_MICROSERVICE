package com.atous.auth.application.dto.command;

public record ResetPasswordCommand(String resetToken, String newPassword, String passwordConfirmation, String ipAddress, String userAgent) {}
