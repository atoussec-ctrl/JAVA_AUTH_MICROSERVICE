package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.EnableMfaCommand; import com.atous.auth.application.dto.result.MfaSetupResult; import com.atous.auth.application.port.in.EnableMfaUseCase; import java.util.List;

public final class EnableMfaService implements EnableMfaUseCase { public MfaSetupResult execute(EnableMfaCommand c){ return new MfaSetupResult("development-secret","otpauth://totp/auth-service", List.of("RECOVERY-DEV-001")); } }
