package com.atous.auth.infrastructure.persistence.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.atous.auth.domain.model.User;
import com.atous.auth.domain.valueobject.Email;
import com.atous.auth.domain.valueobject.PasswordHash;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.entity.UserJpaEntity;

@Component
public class UserPersistenceMapper {
    private final RolePersistenceMapper roleMapper;

    public UserPersistenceMapper(RolePersistenceMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public User toDomain(UserJpaEntity e) {
        return User.builder().id(UserId.of(e.getId())).name(e.getName()).email(Email.of(e.getEmail()))
                .passwordHash(PasswordHash.of(e.getPasswordHash()))
                .roles(e.getRoles().stream().map(roleMapper::toDomain).collect(Collectors.toUnmodifiableSet()))
                .enabled(e.isEnabled()).emailVerified(e.isEmailVerified()).mfaEnabled(e.isMfaEnabled())
                .createdAt(e.getCreatedAt()).updatedAt(e.getUpdatedAt()).lastLoginAt(e.getLastLoginAt()).build();
    }

    public UserJpaEntity toEntity(User u) {
        var e = new UserJpaEntity(
                u.id().value(),
                u.name(),
                u.email().value(),
                u.passwordHash().value()
        );
        e.setEnabled(u.enabled());
        e.setEmailVerified(u.emailVerified());
        e.setMfaEnabled(u.mfaEnabled());
        e.setRoles(u.roles().stream().map(roleMapper::toEntity).collect(Collectors.toSet()));
        e.setCreatedAt(u.createdAt());
        e.setUpdatedAt(u.updatedAt());
        e.setLastLoginAt(u.lastLoginAt());
        return e;
    }
}
