package com.atous.auth.application.dto.result;

public record LogoutResult(boolean success, String message, java.time.Instant loggedOutAt) {}
