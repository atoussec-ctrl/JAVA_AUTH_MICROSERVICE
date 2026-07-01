package com.atous.auth.presentation.dto.response;

public record MfaVerificationResponse(boolean verified, boolean mfaEnabled, java.time.Instant verifiedAt) {}
