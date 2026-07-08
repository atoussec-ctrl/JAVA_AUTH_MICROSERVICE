package com.atous.auth.application.dto.command;

public record AuthenticateUserCommand(String email, String password, String ipAddress, String userAgent,
        String mfaCode) {}
