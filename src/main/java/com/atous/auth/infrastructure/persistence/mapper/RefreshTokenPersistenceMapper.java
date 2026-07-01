package com.atous.auth.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.RefreshToken;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.entity.RefreshTokenJpaEntity;

@Component
public class RefreshTokenPersistenceMapper {
    public RefreshToken toDomain(RefreshTokenJpaEntity e) {
        return new RefreshToken(e.getId(), UserId.of(e.getUserId()), TokenHash.of(e.getTokenHash()), e.isRevoked(),
                e.getExpiresAt(), e.getCreatedAt());
    }

    public RefreshTokenJpaEntity toEntity(RefreshToken t) {
            var e = new RefreshTokenJpaEntity(
                t.id(),
                t.userId().value(),
                t.tokenHash().value(),
                t.revoked(),
                t.expiresAt(),
                t.createdAt()
        );
        return e;
    }
}
