package com.atous.auth.application.service;

import com.atous.auth.application.port.in.DeleteRoleUseCase;
import com.atous.auth.application.port.out.RoleRepositoryPort;
import com.atous.auth.domain.exception.DomainException;
import com.atous.auth.domain.valueobject.RoleId;
import java.util.UUID;

public final class DeleteRoleService implements DeleteRoleUseCase {
    private final RoleRepositoryPort roles;

    public DeleteRoleService(RoleRepositoryPort roles) {
        this.roles = roles;
    }

    public void execute(UUID id) {
        var roleId = RoleId.of(id);
        if (roles.findById(roleId).isEmpty())
            throw new DomainException("Role not found");
        roles.deleteById(roleId);
    }
}
