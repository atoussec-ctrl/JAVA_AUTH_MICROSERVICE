package com.atous.auth.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.MfaDevice;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.entity.MfaDeviceJpaEntity;

@Component
public class MfaDevicePersistenceMapper {
    public MfaDevice toDomain(MfaDeviceJpaEntity e) {
        return new MfaDevice(e.getId(), UserId.of(e.getUserId()), e.getSecretHash(), e.isEnabled(), e.getCreatedAt(),
                e.getVerifiedAt());
    }

    public MfaDeviceJpaEntity toEntity(MfaDevice d) {
        return new MfaDeviceJpaEntity(d.id(), d.userId().value(), d.secretHash(), d.enabled(), d.createdAt(),
                d.verifiedAt());
    }
}
