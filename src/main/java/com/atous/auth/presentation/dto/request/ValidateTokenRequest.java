package com.atous.auth.presentation.dto.request;

public record ValidateTokenRequest(@jakarta.validation.constraints.NotBlank String accessToken) {}
