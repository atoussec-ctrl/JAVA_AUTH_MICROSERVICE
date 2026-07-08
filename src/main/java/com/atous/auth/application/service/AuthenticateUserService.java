package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.AuthenticateUserCommand;
import com.atous.auth.application.dto.result.AuthResult;
import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.in.AuthenticateUserUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.InvalidCredentialsException;
import com.atous.auth.domain.exception.InvalidMfaCodeException;
import com.atous.auth.domain.exception.MfaRequiredException;
import com.atous.auth.domain.exception.TooManyAttemptsException;
import com.atous.auth.domain.model.AuditLog;
import com.atous.auth.domain.model.RefreshToken;
import com.atous.auth.domain.model.User;
import com.atous.auth.domain.service.LoginPolicyService;
import com.atous.auth.domain.valueobject.Email;
import com.atous.auth.domain.valueobject.UserId;
import java.time.Duration;
import java.util.UUID;

public final class AuthenticateUserService implements AuthenticateUserUseCase {
    private final UserRepositoryPort users;
    private final RefreshTokenRepositoryPort refreshTokens;
    private final PasswordHasherPort hasher;
    private final TokenProviderPort tokenProvider;
    private final RefreshTokenGeneratorPort generator;
    private final TokenHashingPort tokenHashing;
    private final ClockProviderPort clock;
    private final LoginPolicyService loginPolicy;
    private final UserApplicationMapper mapper;
    private final RateLimiterPort rateLimiter;
    private final AuditLogRepositoryPort auditLog;
    private final MfaDeviceRepositoryPort mfaDevices;
    private final MfaRecoveryCodeRepositoryPort mfaRecoveryCodes;
    private final TotpPort totp;
    private final Duration ttl;

    public AuthenticateUserService(UserRepositoryPort users, RefreshTokenRepositoryPort refreshTokens,
            PasswordHasherPort hasher, TokenProviderPort tokenProvider, RefreshTokenGeneratorPort generator,
            TokenHashingPort tokenHashing, ClockProviderPort clock, LoginPolicyService loginPolicy,
            UserApplicationMapper mapper, RateLimiterPort rateLimiter, AuditLogRepositoryPort auditLog,
            MfaDeviceRepositoryPort mfaDevices, MfaRecoveryCodeRepositoryPort mfaRecoveryCodes, TotpPort totp,
            Duration ttl) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.hasher = hasher;
        this.tokenProvider = tokenProvider;
        this.generator = generator;
        this.tokenHashing = tokenHashing;
        this.clock = clock;
        this.loginPolicy = loginPolicy;
        this.mapper = mapper;
        this.rateLimiter = rateLimiter;
        this.auditLog = auditLog;
        this.mfaDevices = mfaDevices;
        this.mfaRecoveryCodes = mfaRecoveryCodes;
        this.totp = totp;
        this.ttl = ttl;
    }

    public AuthResult execute(AuthenticateUserCommand c) {
        var key = c.email() == null ? "" : c.email().trim().toLowerCase();
        if (!rateLimiter.isAllowed(key))
            throw new TooManyAttemptsException("Too many failed login attempts, try again later");

        User user = null;
        try {
            user = users.findByEmail(Email.of(c.email())).orElseThrow(InvalidCredentialsException::new);
            loginPolicy.validateCanLogin(user);
            if (!hasher.matches(c.password(), user.passwordHash()))
                throw new InvalidCredentialsException();
            if (user.mfaEnabled())
                requireValidMfaCode(user, c.mfaCode());
        } catch (InvalidCredentialsException | MfaRequiredException | InvalidMfaCodeException e) {
            rateLimiter.recordFailure(key);
            recordAttempt(user == null ? null : user.id(), c, false, e.getMessage());
            throw e;
        }
        rateLimiter.reset(key);

        var now = clock.now();
        users.save(user.withLastLoginAt(now));
        var rawRefresh = generator.generate();
        refreshTokens.save(new RefreshToken(UUID.randomUUID(), user.id(), tokenHashing.hash(rawRefresh), false,
                now.plus(ttl), now));
        var access = tokenProvider.generateAccessToken(user);
        recordAttempt(user.id(), c, true, null);
        return new AuthResult(access.accessToken(), rawRefresh, access.tokenType(), access.expiresIn(),
                access.issuedAt(), access.expiresAt(), mapper.toSummary(user));
    }

    private void requireValidMfaCode(User user, String code) {
        if (code == null || code.isBlank())
            throw new MfaRequiredException("MFA code required");
        var device = mfaDevices.findByUserId(user.id());
        if (device.isPresent() && totp.verifyCode(device.get().secretHash(), code))
            return;
        var codeHash = tokenHashing.hash(code);
        var recovery = mfaRecoveryCodes.findActiveByUserIdAndHash(user.id(), codeHash);
        if (recovery.isPresent()) {
            mfaRecoveryCodes.markUsed(recovery.get().id());
            return;
        }
        throw new InvalidMfaCodeException("Invalid MFA code");
    }

    private void recordAttempt(UserId userId, AuthenticateUserCommand c, boolean success, String reason) {
        auditLog.save(new AuditLog(UUID.randomUUID(), userId, "LOGIN", c.ipAddress(), c.userAgent(), success, reason,
                clock.now()));
    }
}
