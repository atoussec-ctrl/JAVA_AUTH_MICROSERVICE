package com.atous.auth.presentation.mapper;

import com.atous.auth.application.dto.view.RoleView;
import com.atous.auth.presentation.dto.response.RoleResponse;
import org.springframework.stereotype.Component;

@Component
public class RolePresentationMapper {
    private final UserPresentationMapper userMapper;
    public RolePresentationMapper(UserPresentationMapper userMapper){this.userMapper=userMapper;}
    public RoleResponse toResponse(RoleView view){return userMapper.toResponse(view);}
}
