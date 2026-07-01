package com.atous.auth.application.dto.result;

public record MfaSetupResult(String secret, String qrCodeUri, java.util.List<String> recoveryCodes) {}
