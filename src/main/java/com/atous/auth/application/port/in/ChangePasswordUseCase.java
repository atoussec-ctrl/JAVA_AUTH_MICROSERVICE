package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.ChangePasswordCommand;
import com.atous.auth.application.dto.result.PasswordOperationResult;
public interface ChangePasswordUseCase { PasswordOperationResult execute(ChangePasswordCommand command); }
