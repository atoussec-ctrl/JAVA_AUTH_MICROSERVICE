package com.atous.auth.presentation.dto.request;

public record LoginRequest(@jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Email String email, @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(min=8,max=128) String password) {}
