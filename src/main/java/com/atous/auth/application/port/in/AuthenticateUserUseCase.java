package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.AuthenticateUserCommand;
import com.atous.auth.application.dto.result.AuthResult;

public interface AuthenticateUserUseCase { AuthResult execute(AuthenticateUserCommand command); }
