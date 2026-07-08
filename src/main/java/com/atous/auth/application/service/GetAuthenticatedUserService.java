package com.atous.auth.application.service;

import java.util.UUID;

 import com.atous.auth.application.dto.view.UserView;
 import com.atous.auth.application.mapper.UserApplicationMapper;
 import com.atous.auth.application.port.in.GetAuthenticatedUserUseCase;
 import com.atous.auth.application.port.out.UserRepositoryPort;
 import com.atous.auth.domain.exception.UserNotFoundException;
 import com.atous.auth.domain.valueobject.UserId;

public final class GetAuthenticatedUserService implements GetAuthenticatedUserUseCase {
     private final UserRepositoryPort users; 
     private final UserApplicationMapper mapper; 
     public GetAuthenticatedUserService(UserRepositoryPort users, UserApplicationMapper mapper){this.users=users;this.mapper=mapper;

     } 
     @Override
     public UserView execute(UUID id){
          return users.findById(UserId.of(id)).map(mapper::toView).orElseThrow(()->new UserNotFoundException("User not found"));
     } 
    }
