package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.CreateRoleCommand;
import com.atous.auth.application.dto.view.RoleView;
import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.in.CreateRoleUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.DomainException;
import com.atous.auth.domain.model.Role;
import com.atous.auth.domain.valueobject.RoleId;
import java.util.Set;
import java.util.stream.Collectors;

public final class CreateRoleService implements CreateRoleUseCase {
    private final RoleRepositoryPort roles;
    private final PermissionRepositoryPort permissions;
    private final ClockProviderPort clock;
    private final UserApplicationMapper mapper;

    public CreateRoleService(RoleRepositoryPort roles, PermissionRepositoryPort permissions, ClockProviderPort clock,
            UserApplicationMapper mapper) {
        this.roles = roles;
        this.permissions = permissions;
        this.clock = clock;
        this.mapper = mapper;
    }

    public RoleView execute(CreateRoleCommand c) {
        var name = c.name().trim().toUpperCase();
        if (roles.findByName(name).isPresent())
            throw new DomainException("Role already exists");
        var requested = c.permissions() == null ? Set.<String>of() : Set.copyOf(c.permissions());
        var ps = permissions.findAllByNames(requested).stream().collect(Collectors.toUnmodifiableSet());
        if (ps.size() != requested.size())
            throw new DomainException("One or more permissions not found");
        var now = clock.now();
        return mapper.toRoleView(roles.save(new Role(RoleId.newId(), name, c.description(), ps, now, now)));
    }
}
