package com.atous.auth.presentation.dto.request;

public record ResetPasswordRequest(@jakarta.validation.constraints.NotBlank String resetToken, @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(min=8,max=128) String newPassword, @jakarta.validation.constraints.NotBlank String passwordConfirmation) {}
