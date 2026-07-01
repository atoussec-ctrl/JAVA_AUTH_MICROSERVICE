package com.atous.auth.application.dto.command;

public record ForgotPasswordCommand(String email, String ipAddress, String userAgent) {}
