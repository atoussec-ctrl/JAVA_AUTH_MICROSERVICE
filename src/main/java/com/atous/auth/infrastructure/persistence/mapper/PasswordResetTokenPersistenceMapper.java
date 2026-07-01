package com.atous.auth.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.PasswordResetToken;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.entity.PasswordResetTokenJpaEntity;

@Component
public class PasswordResetTokenPersistenceMapper {
    public PasswordResetToken toDomain(PasswordResetTokenJpaEntity e) {
        return new PasswordResetToken(e.getId(), UserId.of(e.getUserId()), TokenHash.of(e.getTokenHash()), e.isUsed(),
                e.getExpiresAt(), e.getCreatedAt());
    }

    public PasswordResetTokenJpaEntity toEntity(PasswordResetToken t) {
        var e = new PasswordResetTokenJpaEntity(
                t.id(),
                t.userId().value(),
                t.tokenHash().value(),
                t.used(),
                t.expiresAt(),
                t.createdAt()
        );
        return e;
    }
}
