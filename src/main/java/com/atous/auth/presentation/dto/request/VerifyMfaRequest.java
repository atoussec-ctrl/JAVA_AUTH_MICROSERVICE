package com.atous.auth.presentation.dto.request;

public record VerifyMfaRequest(@jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Pattern(regexp="^[0-9]{6}$") String code) {}
