package com.atous.auth.presentation.dto.request;

public record ForgotPasswordRequest(@jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Email String email) {}
