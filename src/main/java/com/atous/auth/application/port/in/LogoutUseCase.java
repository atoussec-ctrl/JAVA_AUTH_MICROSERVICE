package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.LogoutCommand;
import com.atous.auth.application.dto.result.LogoutResult;

public interface LogoutUseCase { LogoutResult execute(LogoutCommand command); }
