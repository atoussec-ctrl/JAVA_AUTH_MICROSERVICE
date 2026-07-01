package com.atous.auth.presentation.dto.response;

public record MfaSetupResponse(String secret, String qrCodeUri, java.util.List<String> recoveryCodes) {}
