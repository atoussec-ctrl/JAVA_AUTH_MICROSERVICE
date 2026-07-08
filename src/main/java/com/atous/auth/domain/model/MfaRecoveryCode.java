package com.atous.auth.domain.model;

import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import java.time.Instant;
import java.util.UUID;

public record MfaRecoveryCode(UUID id, UserId userId, TokenHash codeHash, boolean used, Instant createdAt,
        Instant usedAt) {
}
