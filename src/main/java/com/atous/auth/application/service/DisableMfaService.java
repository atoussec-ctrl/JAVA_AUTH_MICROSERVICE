package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.DisableMfaCommand; import com.atous.auth.application.dto.result.MfaVerificationResult; import com.atous.auth.application.port.in.DisableMfaUseCase; import com.atous.auth.application.port.out.ClockProviderPort;

public final class DisableMfaService implements DisableMfaUseCase { private final ClockProviderPort clock; public DisableMfaService(ClockProviderPort clock){this.clock=clock;} public MfaVerificationResult execute(DisableMfaCommand c){return new MfaVerificationResult(true,false,clock.now());} }
