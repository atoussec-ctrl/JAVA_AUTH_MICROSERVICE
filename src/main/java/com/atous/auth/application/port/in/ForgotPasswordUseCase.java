package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.ForgotPasswordCommand;
import com.atous.auth.application.dto.result.PasswordOperationResult;

public interface ForgotPasswordUseCase { PasswordOperationResult execute(ForgotPasswordCommand command); }
