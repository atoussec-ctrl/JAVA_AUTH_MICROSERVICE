package com.atous.auth.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.MfaRecoveryCode;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.entity.MfaRecoveryCodeJpaEntity;

@Component
public class MfaRecoveryCodePersistenceMapper {
    public MfaRecoveryCode toDomain(MfaRecoveryCodeJpaEntity e) {
        return new MfaRecoveryCode(e.getId(), UserId.of(e.getUserId()), TokenHash.of(e.getCodeHash()), e.isUsed(),
                e.getCreatedAt(), e.getUsedAt());
    }

    public MfaRecoveryCodeJpaEntity toEntity(MfaRecoveryCode c) {
        return new MfaRecoveryCodeJpaEntity(c.id(), c.userId().value(), c.codeHash().value(), c.used(),
                c.createdAt(), c.usedAt());
    }
}
