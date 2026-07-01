package com.atous.auth.application.mapper;

import com.atous.auth.application.dto.view.*;
import com.atous.auth.domain.model.*;
import java.util.stream.Collectors;

public final class UserApplicationMapper {
    public UserSummaryView toSummary(User u){return new UserSummaryView(u.id().value(),u.name(),u.email().value(),u.enabled(),u.emailVerified(),u.mfaEnabled(),u.roles().stream().map(Role::name).collect(Collectors.toUnmodifiableSet()),u.roles().stream().flatMap(r->r.permissions().stream()).map(Permission::name).collect(Collectors.toUnmodifiableSet()));}
    public UserView toView(User u){return new UserView(u.id().value(),u.name(),u.email().value(),u.enabled(),u.emailVerified(),u.mfaEnabled(),u.roles().stream().map(this::toRoleView).collect(Collectors.toUnmodifiableSet()),u.createdAt(),u.updatedAt(),u.lastLoginAt());}
    public RoleView toRoleView(Role r){return new RoleView(r.id().value(),r.name(),r.description(),r.permissions().stream().map(this::toPermissionView).collect(Collectors.toUnmodifiableSet()),r.createdAt(),r.updatedAt());}
    public PermissionView toPermissionView(Permission p){return new PermissionView(p.id().value(),p.name(),p.description());}
}
