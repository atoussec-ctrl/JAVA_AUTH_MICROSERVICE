package com.atous.auth.presentation.controller;

import com.atous.auth.application.dto.command.*;
import com.atous.auth.application.port.in.*;
import com.atous.auth.presentation.dto.request.*;
import com.atous.auth.presentation.dto.response.RoleResponse;
import com.atous.auth.presentation.mapper.RolePresentationMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final CreateRoleUseCase create;
    private final UpdateRoleUseCase update;
    private final DeleteRoleUseCase delete;
    private final RolePresentationMapper mapper;

    public RoleController(CreateRoleUseCase create, UpdateRoleUseCase update, DeleteRoleUseCase delete,
            RolePresentationMapper mapper) {
        this.create = create;
        this.update = update;
        this.delete = delete;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse create(@Valid @RequestBody CreateRoleRequest r) {
        return mapper.toResponse(create.execute(new CreateRoleCommand(r.name(), r.description(), r.permissions())));
    }

    @PutMapping("/{roleId}")
    public RoleResponse update(@PathVariable UUID roleId, @Valid @RequestBody UpdateRoleRequest r) {
        return mapper
                .toResponse(update.execute(new UpdateRoleCommand(roleId, r.name(), r.description(), r.permissions())));
    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID roleId) {
        delete.execute(roleId);
    }
}
