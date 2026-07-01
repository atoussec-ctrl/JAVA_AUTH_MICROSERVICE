package com.atous.auth.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.Permission;
import com.atous.auth.domain.valueobject.PermissionId;
import com.atous.auth.infrastructure.persistence.entity.PermissionJpaEntity;

@Component
public class PermissionPersistenceMapper {
    public Permission toDomain(PermissionJpaEntity e) {
        return new Permission(PermissionId.of(e.getId()), e.getName(), e.getDescription());
    }

    public PermissionJpaEntity toEntity(Permission p) {
        var e = new PermissionJpaEntity(
                p.id().value(),
                p.name(),
                p.description()
        );
        return e;
    }
}
