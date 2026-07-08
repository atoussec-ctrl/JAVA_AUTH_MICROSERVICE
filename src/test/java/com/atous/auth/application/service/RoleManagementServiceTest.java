package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.AssignRolesCommand;
import com.atous.auth.application.dto.command.CreateRoleCommand;
import com.atous.auth.application.dto.command.UpdateRoleCommand;
import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.DomainException;
import com.atous.auth.domain.exception.RoleInUseException;
import com.atous.auth.domain.model.Permission;
import com.atous.auth.domain.model.Role;
import com.atous.auth.domain.model.User;
import com.atous.auth.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleManagementServiceTest {
    @Mock private RoleRepositoryPort roles;
    @Mock private PermissionRepositoryPort permissions;
    @Mock private UserRepositoryPort users;
    @Mock private ClockProviderPort clock;

    private final Instant now = Instant.parse("2026-01-01T00:00:00Z");
    private final UserApplicationMapper mapper = new UserApplicationMapper();

    @BeforeEach
    void setUp() {
        lenient().when(clock.now()).thenReturn(now);
    }

    @Test
    void createRoleShouldRejectUnknownPermissionNames() {
        var service = new CreateRoleService(roles, permissions, clock, mapper);
        when(roles.findByName("EDITOR")).thenReturn(Optional.empty());
        when(permissions.findAllByNames(Set.of("READ", "TYPO_PERM"))).thenReturn(List.of(
                new Permission(PermissionId.newId(), "READ", "")));

        assertThatThrownBy(() -> service.execute(new CreateRoleCommand("editor", "desc", Set.of("READ", "TYPO_PERM"))))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("permission");

        verify(roles, never()).save(any());
    }

    @Test
    void updateRoleShouldRejectRenameToExistingRoleName() {
        var service = new UpdateRoleService(roles, permissions, clock, mapper);
        var roleId = RoleId.newId();
        var existing = new Role(roleId, "EDITOR", "desc", Set.of(), now, now);
        when(roles.findById(roleId)).thenReturn(Optional.of(existing));
        when(roles.findByName("ADMIN")).thenReturn(Optional.of(new Role(RoleId.newId(), "ADMIN", "", Set.of(), now, now)));

        assertThatThrownBy(() -> service.execute(new UpdateRoleCommand(roleId.value(), "admin", null, null)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("already exists");

        verify(roles, never()).save(any());
    }

    @Test
    void updateRoleShouldRejectUnknownPermissionNames() {
        var service = new UpdateRoleService(roles, permissions, clock, mapper);
        var roleId = RoleId.newId();
        var existing = new Role(roleId, "EDITOR", "desc", Set.of(), now, now);
        when(roles.findById(roleId)).thenReturn(Optional.of(existing));
        when(permissions.findAllByNames(Set.of("GHOST_PERM"))).thenReturn(List.of());

        assertThatThrownBy(() -> service.execute(new UpdateRoleCommand(roleId.value(), null, null, Set.of("GHOST_PERM"))))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("permission");

        verify(roles, never()).save(any());
    }

    @Test
    void updateRoleShouldAllowKeepingTheSameName() {
        var service = new UpdateRoleService(roles, permissions, clock, mapper);
        var roleId = RoleId.newId();
        var existing = new Role(roleId, "EDITOR", "desc", Set.of(), now, now);
        when(roles.findById(roleId)).thenReturn(Optional.of(existing));
        when(permissions.findAllByNames(Set.of())).thenReturn(List.of());
        when(roles.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.execute(new UpdateRoleCommand(roleId.value(), "editor", "new desc", null));

        assertThat(result.name()).isEqualTo("EDITOR");
        verify(roles, never()).findByName(any());
        verify(roles).save(any());
    }

    @Test
    void deleteRoleShouldRejectUnknownRoleId() {
        var service = new DeleteRoleService(roles);
        var id = UUID.randomUUID();
        when(roles.findById(RoleId.of(id))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(id)).isInstanceOf(DomainException.class);

        verify(roles, never()).deleteById(any());
    }

    @Test
    void deleteRoleShouldPropagateRoleInUseFromAdapter() {
        var service = new DeleteRoleService(roles);
        var id = UUID.randomUUID();
        var roleId = RoleId.of(id);
        when(roles.findById(roleId)).thenReturn(Optional.of(new Role(roleId, "EDITOR", "", Set.of(), now, now)));
        doThrow(new RoleInUseException("in use")).when(roles).deleteById(roleId);

        assertThatThrownBy(() -> service.execute(id)).isInstanceOf(RoleInUseException.class);
    }

    @Test
    void assignRolesShouldRejectUnknownRoleIds() {
        var service = new AssignRolesToUserService(users, roles, clock, mapper);
        var user = User.builder().id(UserId.newId()).name("Alice")
                .email(com.atous.auth.domain.valueobject.Email.of("alice@example.com"))
                .passwordHash(com.atous.auth.domain.valueobject.PasswordHash.of("hash")).createdAt(now).build();
        var knownRoleId = UUID.randomUUID();
        var unknownRoleId = UUID.randomUUID();
        when(users.findById(user.id())).thenReturn(Optional.of(user));
        when(roles.findAllByIds(any())).thenReturn(List.of(new Role(RoleId.of(knownRoleId), "EDITOR", "", Set.of(), now, now)));

        assertThatThrownBy(() -> service.execute(new AssignRolesCommand(user.id().value(), Set.of(knownRoleId, unknownRoleId))))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("role");

        verify(users, never()).save(any());
    }
}
