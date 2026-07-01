package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.ValidateTokenCommand;
import com.atous.auth.application.dto.result.TokenValidationResult;

public interface ValidateTokenUseCase { TokenValidationResult execute(ValidateTokenCommand command); }
