package com.atous.auth.application.port.in;


import com.atous.auth.application.dto.view.UserView;

public interface GetAuthenticatedUserUseCase { UserView execute(java.util.UUID userId); }
