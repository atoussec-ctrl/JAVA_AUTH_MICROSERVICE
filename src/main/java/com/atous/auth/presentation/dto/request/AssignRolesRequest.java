package com.atous.auth.presentation.dto.request;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public record AssignRolesRequest(@NotEmpty Set<UUID> roleIds) {}
