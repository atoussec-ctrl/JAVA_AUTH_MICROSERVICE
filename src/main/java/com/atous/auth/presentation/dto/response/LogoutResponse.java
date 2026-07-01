package com.atous.auth.presentation.dto.response;

public record LogoutResponse(boolean success, String message, java.time.Instant loggedOutAt) {}
