package com.atous.auth.presentation.dto.request;

public record DisableMfaRequest(@jakarta.validation.constraints.NotBlank String password, @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Pattern(regexp="^[0-9]{6}$") String code) {}
