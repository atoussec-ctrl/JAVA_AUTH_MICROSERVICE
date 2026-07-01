package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.ValidateTokenCommand; import com.atous.auth.application.dto.result.TokenValidationResult; import com.atous.auth.application.port.in.ValidateTokenUseCase; import com.atous.auth.application.port.out.TokenProviderPort;

public final class ValidateTokenService implements ValidateTokenUseCase { private final TokenProviderPort tokens; public ValidateTokenService(TokenProviderPort tokens){this.tokens=tokens;} public TokenValidationResult execute(ValidateTokenCommand c){var x=tokens.validate(c.accessToken()); return new TokenValidationResult(true,x.userId(),x.subject(),x.roles(),x.permissions(),x.issuedAt(),x.expiresAt());} }
