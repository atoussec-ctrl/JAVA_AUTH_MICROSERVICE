package com.atous.auth.infrastructure.persistence.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.Role;
import com.atous.auth.domain.valueobject.RoleId;
import com.atous.auth.infrastructure.persistence.entity.RoleJpaEntity;

@Component
public class RolePersistenceMapper {
    private final PermissionPersistenceMapper permissionMapper;

    public RolePersistenceMapper(PermissionPersistenceMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public Role toDomain(RoleJpaEntity e) {
        return new Role(RoleId.of(e.getId()), e.getName(), e.getDescription(),
                e.getPermissions().stream().map(permissionMapper::toDomain).collect(Collectors.toUnmodifiableSet()),
                e.getCreatedAt(), e.getUpdatedAt());
    }

    public RoleJpaEntity toEntity(Role r) {
        var e = new RoleJpaEntity(
                r.id().value(),
                r.name(),
                r.description(),
                r.permissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()),
                r.createdAt(),
                r.updatedAt()
        );
        return e;
       
    }
}
