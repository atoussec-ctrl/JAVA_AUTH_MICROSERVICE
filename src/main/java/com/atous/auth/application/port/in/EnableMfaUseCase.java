package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.EnableMfaCommand;
import com.atous.auth.application.dto.result.MfaSetupResult;

public interface EnableMfaUseCase { MfaSetupResult execute(EnableMfaCommand command); }
