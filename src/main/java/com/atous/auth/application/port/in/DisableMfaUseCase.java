package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.DisableMfaCommand;
import com.atous.auth.application.dto.result.MfaVerificationResult;

public interface DisableMfaUseCase { MfaVerificationResult execute(DisableMfaCommand command); }
