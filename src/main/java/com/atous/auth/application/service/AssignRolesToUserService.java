package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.AssignRolesCommand;
import com.atous.auth.application.dto.view.UserView; 
import com.atous.auth.application.mapper.UserApplicationMapper; 
import com.atous.auth.application.port.in.AssignRolesToUserUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.DomainException;
import com.atous.auth.domain.exception.UserNotFoundException;
import com.atous.auth.domain.valueobject.*;
import java.util.Set;
import java.util.stream.Collectors;

public final class AssignRolesToUserService implements AssignRolesToUserUseCase { 
        private final UserRepositoryPort users;
        private final RoleRepositoryPort roles; 
        private final ClockProviderPort clock; 
        private final UserApplicationMapper mapper; 
        
        public AssignRolesToUserService(
                UserRepositoryPort users, 
                RoleRepositoryPort roles, 
                ClockProviderPort clock, 
                UserApplicationMapper mapper
            )
            {
                this.users=users;
                this.roles=roles;
                this.clock=clock;
                this.mapper=mapper;
            } 

            public UserView execute(AssignRolesCommand c){

                var u=users.findById(
                    UserId.of(c.userId())
                )
                .orElseThrow(()->new UserNotFoundException("User not found")); 

                var requested = Set.copyOf(c.roleIds());
                var set=roles.findAllByIds(
                    requested
                    .stream()
                    .map(RoleId::of)
                    .toList())
                    .stream()
                    .collect(Collectors.toUnmodifiableSet());
                if (set.size() != requested.size())
                    throw new DomainException("One or more roles not found");
                return mapper.toView(users.save(u.withRoles(set,clock.now())));
            } 
            
        
}
