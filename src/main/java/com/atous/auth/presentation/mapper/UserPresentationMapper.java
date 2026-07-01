package com.atous.auth.presentation.mapper;

import com.atous.auth.application.dto.view.*;
import com.atous.auth.presentation.dto.response.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserPresentationMapper {
    public UserResponse toResponse(UserView v){return new UserResponse(v.id(),v.name(),v.email(),v.enabled(),v.emailVerified(),v.mfaEnabled(),v.roles().stream().map(this::toResponse).collect(Collectors.toUnmodifiableSet()),v.createdAt(),v.updatedAt(),v.lastLoginAt());}
    public RoleResponse toResponse(RoleView v){return new RoleResponse(v.id(),v.name(),v.description(),v.permissions().stream().map(this::toResponse).collect(Collectors.toUnmodifiableSet()),v.createdAt(),v.updatedAt());}
    public PermissionResponse toResponse(PermissionView v){return new PermissionResponse(v.id(),v.name(),v.description());}
    public PageResponse<UserResponse> toPageResponse(PageView<UserView> p){return new PageResponse<>(p.content().stream().map(this::toResponse).toList(),p.page(),p.size(),p.totalElements(),p.totalPages(),p.first(),p.last());}
}
