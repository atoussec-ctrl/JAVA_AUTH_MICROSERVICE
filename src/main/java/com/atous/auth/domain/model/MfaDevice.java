package com.atous.auth.domain.model;

import com.atous.auth.domain.valueobject.UserId;
import java.time.Instant;
import java.util.UUID;

public record MfaDevice(UUID id, UserId userId, String secretHash, boolean enabled, Instant createdAt,
        Instant verifiedAt) {
}
