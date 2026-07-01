package com.atous.auth.domain.service;

import java.util.Set;
import java.util.stream.Collectors;

import com.atous.auth.domain.model.Permission;
import com.atous.auth.domain.model.Role;
import com.atous.auth.domain.model.User;

public final class UserDomainService {
    public Set<String> resolveRoleNames(User u) {
        return u.roles().stream().map(Role::name).collect(Collectors.toUnmodifiableSet());
    }

    public Set<String> resolvePermissionNames(User u) {
        return u.roles().stream().flatMap(r -> r.permissions().stream()).map(Permission::name)
                .collect(Collectors.toUnmodifiableSet());
    }
}
