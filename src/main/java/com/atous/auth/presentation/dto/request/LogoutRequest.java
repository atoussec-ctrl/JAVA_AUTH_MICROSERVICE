package com.atous.auth.presentation.dto.request;

public record LogoutRequest(@jakarta.validation.constraints.NotBlank String refreshToken) {}
