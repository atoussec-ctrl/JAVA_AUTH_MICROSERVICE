package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.ResetPasswordCommand;
import com.atous.auth.application.dto.result.PasswordOperationResult;
    
public interface ResetPasswordUseCase { PasswordOperationResult execute(ResetPasswordCommand command); }
