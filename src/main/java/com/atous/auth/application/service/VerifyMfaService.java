package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.VerifyMfaCommand; import com.atous.auth.application.dto.result.MfaVerificationResult; import com.atous.auth.application.port.in.VerifyMfaUseCase; import com.atous.auth.application.port.out.ClockProviderPort;

public final class VerifyMfaService implements VerifyMfaUseCase { private final ClockProviderPort clock; public VerifyMfaService(ClockProviderPort clock){this.clock=clock;} public MfaVerificationResult execute(VerifyMfaCommand c){return new MfaVerificationResult(true,true,clock.now());} }
