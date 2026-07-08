package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.RefreshTokenCommand;
import com.atous.auth.application.dto.result.AuthResult;
import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.in.RefreshTokenUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.*;
import com.atous.auth.domain.model.RefreshToken;
import java.time.Duration;
import java.util.UUID;

public final class RefreshTokenService implements RefreshTokenUseCase {
    private final RefreshTokenRepositoryPort tokens;
    private final UserRepositoryPort users;
    private final RefreshTokenGeneratorPort generator;
    private final TokenHashingPort hash;
    private final TokenProviderPort tokenProvider;
    private final ClockProviderPort clock;
    private final UserApplicationMapper mapper;
    private final Duration ttl;

    public RefreshTokenService(RefreshTokenRepositoryPort tokens, UserRepositoryPort users,
            RefreshTokenGeneratorPort generator, TokenHashingPort hash, TokenProviderPort tokenProvider,
            ClockProviderPort clock, UserApplicationMapper mapper, Duration ttl) {
        this.tokens = tokens;
        this.users = users;
        this.generator = generator;
        this.hash = hash;
        this.tokenProvider = tokenProvider;
        this.clock = clock;
        this.mapper = mapper;
        this.ttl = ttl;
    }

    public AuthResult execute(RefreshTokenCommand c) {
        var oldHash = hash.hash(c.refreshToken());
        var stored = tokens.findByTokenHash(oldHash).orElseThrow(InvalidCredentialsException::new);
        var now = clock.now();
        if (stored.revoked())
            throw new TokenRevokedException("Refresh token was revoked");
        if (stored.isExpired(now))
            throw new TokenExpiredException("Refresh token expired");
        var user = users.findById(stored.userId()).orElseThrow(InvalidCredentialsException::new);
        tokens.revoke(oldHash);
        var raw = generator.generate();
        tokens.save(new RefreshToken(UUID.randomUUID(), user.id(), hash.hash(raw), false, now.plus(ttl), now));
        var access = tokenProvider.generateAccessToken(user);
        return new AuthResult(access.accessToken(), raw, access.tokenType(), access.expiresIn(), access.issuedAt(),
                access.expiresAt(), mapper.toSummary(user));
    }
}
