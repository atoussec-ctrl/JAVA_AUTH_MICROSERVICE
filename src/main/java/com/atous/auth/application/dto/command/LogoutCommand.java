package com.atous.auth.application.dto.command;

public record LogoutCommand(String refreshToken, String accessToken, String ipAddress, String userAgent) {}
