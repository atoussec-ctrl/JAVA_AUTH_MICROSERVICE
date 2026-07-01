package com.atous.auth.domain.model;

import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import java.time.Instant;
import java.util.UUID;

public record RefreshToken(UUID id, UserId userId, TokenHash tokenHash, boolean revoked, Instant expiresAt, Instant createdAt) {
    public boolean isExpired(Instant now){return !now.isBefore(expiresAt);}
    public boolean isActive(Instant now){return !revoked && !isExpired(now);}
}
