package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.LogoutCommand; import com.atous.auth.application.dto.result.LogoutResult; import com.atous.auth.application.port.in.LogoutUseCase; import com.atous.auth.application.port.out.*;

public final class LogoutService implements LogoutUseCase { private final RefreshTokenRepositoryPort tokens; private final TokenHashingPort hash; private final ClockProviderPort clock; public LogoutService(RefreshTokenRepositoryPort tokens, TokenHashingPort hash, ClockProviderPort clock){this.tokens=tokens;this.hash=hash;this.clock=clock;} public LogoutResult execute(LogoutCommand c){tokens.revoke(hash.hash(c.refreshToken())); return new LogoutResult(true,"Logout completed successfully",clock.now());} }
