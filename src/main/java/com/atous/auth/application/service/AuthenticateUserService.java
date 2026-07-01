package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.AuthenticateUserCommand;
import com.atous.auth.application.dto.result.AuthResult;
import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.in.AuthenticateUserUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.InvalidCredentialsException;
import com.atous.auth.domain.model.RefreshToken;
import com.atous.auth.domain.service.LoginPolicyService;
import com.atous.auth.domain.valueobject.Email;
import java.time.Duration;
import java.util.UUID;

public final class AuthenticateUserService implements AuthenticateUserUseCase {
    private final UserRepositoryPort users; private final RefreshTokenRepositoryPort refreshTokens; private final PasswordHasherPort hasher; private final TokenProviderPort tokenProvider; private final RefreshTokenGeneratorPort generator; private final TokenHashingPort tokenHashing; private final ClockProviderPort clock; private final LoginPolicyService loginPolicy; private final UserApplicationMapper mapper; private final Duration ttl;
    public AuthenticateUserService(UserRepositoryPort users, RefreshTokenRepositoryPort refreshTokens, PasswordHasherPort hasher, TokenProviderPort tokenProvider, RefreshTokenGeneratorPort generator, TokenHashingPort tokenHashing, ClockProviderPort clock, LoginPolicyService loginPolicy, UserApplicationMapper mapper, Duration ttl){this.users=users;this.refreshTokens=refreshTokens;this.hasher=hasher;this.tokenProvider=tokenProvider;this.generator=generator;this.tokenHashing=tokenHashing;this.clock=clock;this.loginPolicy=loginPolicy;this.mapper=mapper;this.ttl=ttl;}
    public AuthResult execute(AuthenticateUserCommand c){
        var user=users.findByEmail(Email.of(c.email())).orElseThrow(InvalidCredentialsException::new); loginPolicy.validateCanLogin(user);
        if(!hasher.matches(c.password(), user.passwordHash())) throw new InvalidCredentialsException();
        var now=clock.now(); users.save(user.withLastLoginAt(now)); var rawRefresh=generator.generate();
        refreshTokens.save(new RefreshToken(UUID.randomUUID(), user.id(), tokenHashing.hash(rawRefresh), false, now.plus(ttl), now));
        var access=tokenProvider.generateAccessToken(user);
        return new AuthResult(access.accessToken(), rawRefresh, access.tokenType(), access.expiresIn(), access.issuedAt(), access.expiresAt(), mapper.toSummary(user));
    }
}
