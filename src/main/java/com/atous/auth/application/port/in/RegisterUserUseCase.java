package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.RegisterUserCommand;
import com.atous.auth.application.dto.result.RegisterUserResult;
public interface RegisterUserUseCase { RegisterUserResult execute(RegisterUserCommand command); }
