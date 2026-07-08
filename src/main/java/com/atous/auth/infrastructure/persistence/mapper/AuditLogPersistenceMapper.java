package com.atous.auth.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.AuditLog;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.entity.AuditLogJpaEntity;

@Component
public class AuditLogPersistenceMapper {
    public AuditLog toDomain(AuditLogJpaEntity e) {
        return new AuditLog(e.getId(), e.getUserId() == null ? null : UserId.of(e.getUserId()), e.getAction(),
                e.getIpAddress(), e.getUserAgent(), e.isSuccess(), e.getReason(), e.getCreatedAt());
    }

    public AuditLogJpaEntity toEntity(AuditLog a) {
        return new AuditLogJpaEntity(a.id(), a.userId() == null ? null : a.userId().value(), a.action(),
                a.ipAddress(), a.userAgent(), a.success(), a.reason(), a.createdAt());
    }
}
