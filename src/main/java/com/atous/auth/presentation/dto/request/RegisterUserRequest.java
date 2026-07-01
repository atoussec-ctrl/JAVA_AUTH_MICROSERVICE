package com.atous.auth.presentation.dto.request;

public record RegisterUserRequest(@jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(min=2,max=120) String name, @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Email String email, @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(min=8,max=128) String password, @jakarta.validation.constraints.NotBlank String passwordConfirmation) {}
