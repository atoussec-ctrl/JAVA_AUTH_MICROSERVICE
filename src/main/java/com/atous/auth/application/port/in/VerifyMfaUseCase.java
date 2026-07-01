package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.VerifyMfaCommand;
import com.atous.auth.application.dto.result.MfaVerificationResult;

public interface VerifyMfaUseCase { MfaVerificationResult execute(VerifyMfaCommand command); }
