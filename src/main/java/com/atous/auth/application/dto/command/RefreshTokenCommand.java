package com.atous.auth.application.dto.command;

public record RefreshTokenCommand(String refreshToken, String ipAddress, String userAgent) {}
