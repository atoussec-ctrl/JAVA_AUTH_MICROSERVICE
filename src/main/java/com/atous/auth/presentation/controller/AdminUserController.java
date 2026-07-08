package com.atous.auth.presentation.controller;

import com.atous.auth.application.dto.command.*;
import com.atous.auth.application.port.in.*;
import com.atous.auth.presentation.dto.request.*;
import com.atous.auth.presentation.dto.response.*;
import com.atous.auth.presentation.mapper.UserPresentationMapper;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final SearchUsersUseCase search;
    private final UpdateUserStatusUseCase updateStatus;
    private final AssignRolesToUserUseCase assignRoles;
    private final UserPresentationMapper mapper;

    public AdminUserController(
            SearchUsersUseCase search,
            UpdateUserStatusUseCase updateStatus,
            AssignRolesToUserUseCase assignRoles,
            UserPresentationMapper mapper) {
        this.search = search;
        this.updateStatus = updateStatus;
        this.assignRoles = assignRoles;
        this.mapper = mapper;
    }

    @GetMapping("/users")
    public PageResponse<UserResponse> searchUsers(@Valid SearchUsersRequest r) {
        return mapper.toPageResponse(
                search.execute(new SearchUsersQuery(r.search(), r.enabled(), r.normalizedPage(), r.normalizedSize())));
    }

    @PatchMapping("/{userId}/status")
    public UserResponse updateStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequest r) {
        return mapper.toResponse(updateStatus.execute(new UpdateUserStatusCommand(userId, r.enabled())));
    }

    @PutMapping("/{userId}/roles")
    public UserResponse assignRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignRolesRequest r) {
        return mapper.toResponse(assignRoles.execute(new AssignRolesCommand(userId, r.roleIds())));
    }
}
