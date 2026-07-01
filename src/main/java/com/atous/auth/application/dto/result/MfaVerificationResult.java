package com.atous.auth.application.dto.result;

public record MfaVerificationResult(boolean verified, boolean mfaEnabled, java.time.Instant verifiedAt) {}
