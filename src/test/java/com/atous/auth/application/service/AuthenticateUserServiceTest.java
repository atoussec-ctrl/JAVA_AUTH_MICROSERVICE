package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.AuthenticateUserCommand;
import com.atous.auth.application.dto.result.AccessTokenResult;
import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.InvalidCredentialsException;
import com.atous.auth.domain.exception.InvalidMfaCodeException;
import com.atous.auth.domain.exception.MfaRequiredException;
import com.atous.auth.domain.exception.TooManyAttemptsException;
import com.atous.auth.domain.model.MfaDevice;
import com.atous.auth.domain.model.MfaRecoveryCode;
import com.atous.auth.domain.model.User;
import com.atous.auth.domain.service.LoginPolicyService;
import com.atous.auth.domain.valueobject.Email;
import com.atous.auth.domain.valueobject.PasswordHash;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserServiceTest {
    @Mock private UserRepositoryPort users;
    @Mock private RefreshTokenRepositoryPort refreshTokens;
    @Mock private PasswordHasherPort hasher;
    @Mock private TokenProviderPort tokenProvider;
    @Mock private RefreshTokenGeneratorPort generator;
    @Mock private TokenHashingPort tokenHashing;
    @Mock private ClockProviderPort clock;
    @Mock private RateLimiterPort rateLimiter;
    @Mock private AuditLogRepositoryPort auditLog;
    @Mock private MfaDeviceRepositoryPort mfaDevices;
    @Mock private MfaRecoveryCodeRepositoryPort mfaRecoveryCodes;
    @Mock private TotpPort totp;

    private AuthenticateUserService service;
    private User user;
    private final Instant now = Instant.parse("2026-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        service = new AuthenticateUserService(users, refreshTokens, hasher, tokenProvider, generator, tokenHashing,
                clock, new LoginPolicyService(), new UserApplicationMapper(), rateLimiter, auditLog, mfaDevices,
                mfaRecoveryCodes, totp, Duration.ofDays(30));
        user = User.builder().id(UserId.newId()).name("Alice").email(Email.of("alice@example.com"))
                .passwordHash(PasswordHash.of("hashed")).createdAt(now).build();
    }

    private void stubSuccessfulIssuance() {
        when(clock.now()).thenReturn(now);
        when(generator.generate()).thenReturn("raw-refresh-token");
        when(tokenHashing.hash("raw-refresh-token")).thenReturn(TokenHash.of("hashed-refresh"));
        when(tokenProvider.generateAccessToken(any()))
                .thenReturn(new AccessTokenResult("access-token", "Bearer", 900, now, now.plusSeconds(900)));
    }

    @Test
    void shouldAuthenticateAndResetRateLimiterOnSuccess() {
        var command = new AuthenticateUserCommand("alice@example.com", "Password1", "127.0.0.1", "junit", null);
        when(rateLimiter.isAllowed("alice@example.com")).thenReturn(true);
        when(users.findByEmail(any())).thenReturn(Optional.of(user));
        when(hasher.matches("Password1", user.passwordHash())).thenReturn(true);
        stubSuccessfulIssuance();

        var result = service.execute(command);

        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("raw-refresh-token");
        verify(rateLimiter).reset("alice@example.com");
        verify(rateLimiter, never()).recordFailure(anyString());
        verify(auditLog).save(argThat(a -> a.success() && "LOGIN".equals(a.action()) && a.userId().equals(user.id())));
    }

    @Test
    void shouldRecordFailureAndRejectOnBadPassword() {
        var command = new AuthenticateUserCommand("alice@example.com", "wrong", "127.0.0.1", "junit", null);
        when(rateLimiter.isAllowed("alice@example.com")).thenReturn(true);
        when(users.findByEmail(any())).thenReturn(Optional.of(user));
        when(hasher.matches("wrong", user.passwordHash())).thenReturn(false);
        when(clock.now()).thenReturn(now);

        assertThatThrownBy(() -> service.execute(command)).isInstanceOf(InvalidCredentialsException.class);

        verify(rateLimiter).recordFailure("alice@example.com");
        verify(rateLimiter, never()).reset(anyString());
        verify(auditLog).save(argThat(a -> !a.success() && a.userId().equals(user.id())));
        verify(refreshTokens, never()).save(any());
    }

    @Test
    void shouldRecordFailureWhenUserNotFound() {
        var command = new AuthenticateUserCommand("unknown@example.com", "whatever", "127.0.0.1", "junit", null);
        when(rateLimiter.isAllowed("unknown@example.com")).thenReturn(true);
        when(users.findByEmail(any())).thenReturn(Optional.empty());
        when(clock.now()).thenReturn(now);

        assertThatThrownBy(() -> service.execute(command)).isInstanceOf(InvalidCredentialsException.class);

        verify(rateLimiter).recordFailure("unknown@example.com");
        verify(auditLog).save(argThat(a -> !a.success() && a.userId() == null));
        verifyNoInteractions(hasher);
    }

    @Test
    void shouldRejectWithoutTouchingCredentialsWhenRateLimited() {
        var command = new AuthenticateUserCommand("alice@example.com", "Password1", "127.0.0.1", "junit", null);
        when(rateLimiter.isAllowed("alice@example.com")).thenReturn(false);

        assertThatThrownBy(() -> service.execute(command)).isInstanceOf(TooManyAttemptsException.class);

        verifyNoInteractions(users, hasher, auditLog, refreshTokens, tokenProvider);
        verify(rateLimiter, never()).recordFailure(anyString());
    }

    private User mfaUser() {
        return User.builder().id(user.id()).name("Alice").email(Email.of("alice@example.com"))
                .passwordHash(PasswordHash.of("hashed")).mfaEnabled(true).createdAt(now).build();
    }

    @Test
    void shouldRequireMfaCodeWhenEnabledAndNoneProvided() {
        var mfaEnabledUser = mfaUser();
        var command = new AuthenticateUserCommand("alice@example.com", "Password1", "127.0.0.1", "junit", null);
        when(rateLimiter.isAllowed("alice@example.com")).thenReturn(true);
        when(users.findByEmail(any())).thenReturn(Optional.of(mfaEnabledUser));
        when(hasher.matches("Password1", mfaEnabledUser.passwordHash())).thenReturn(true);
        when(clock.now()).thenReturn(now);

        assertThatThrownBy(() -> service.execute(command)).isInstanceOf(MfaRequiredException.class);

        verify(rateLimiter).recordFailure("alice@example.com");
        verifyNoInteractions(refreshTokens, tokenProvider);
    }

    @Test
    void shouldAuthenticateWithValidTotpCode() {
        var mfaEnabledUser = mfaUser();
        var device = new MfaDevice(UUID.randomUUID(), mfaEnabledUser.id(), "SECRET", true, now, now);
        var command = new AuthenticateUserCommand("alice@example.com", "Password1", "127.0.0.1", "junit", "123456");
        when(rateLimiter.isAllowed("alice@example.com")).thenReturn(true);
        when(users.findByEmail(any())).thenReturn(Optional.of(mfaEnabledUser));
        when(hasher.matches("Password1", mfaEnabledUser.passwordHash())).thenReturn(true);
        when(mfaDevices.findByUserId(mfaEnabledUser.id())).thenReturn(Optional.of(device));
        when(totp.verifyCode("SECRET", "123456")).thenReturn(true);
        stubSuccessfulIssuance();

        var result = service.execute(command);

        assertThat(result.accessToken()).isEqualTo("access-token");
        verify(mfaRecoveryCodes, never()).findActiveByUserIdAndHash(any(), any());
    }

    @Test
    void shouldAuthenticateWithValidRecoveryCodeWhenTotpFails() {
        var mfaEnabledUser = mfaUser();
        var device = new MfaDevice(UUID.randomUUID(), mfaEnabledUser.id(), "SECRET", true, now, now);
        var recovery = new MfaRecoveryCode(UUID.randomUUID(), mfaEnabledUser.id(), TokenHash.of("hashed-code"), false,
                now, null);
        var command = new AuthenticateUserCommand("alice@example.com", "Password1", "127.0.0.1", "junit", "AAAAAAAA");
        when(rateLimiter.isAllowed("alice@example.com")).thenReturn(true);
        when(users.findByEmail(any())).thenReturn(Optional.of(mfaEnabledUser));
        when(hasher.matches("Password1", mfaEnabledUser.passwordHash())).thenReturn(true);
        when(mfaDevices.findByUserId(mfaEnabledUser.id())).thenReturn(Optional.of(device));
        when(totp.verifyCode("SECRET", "AAAAAAAA")).thenReturn(false);
        when(tokenHashing.hash("AAAAAAAA")).thenReturn(TokenHash.of("hashed-code"));
        when(mfaRecoveryCodes.findActiveByUserIdAndHash(eq(mfaEnabledUser.id()), any())).thenReturn(Optional.of(recovery));
        stubSuccessfulIssuance();

        var result = service.execute(command);

        assertThat(result.accessToken()).isEqualTo("access-token");
        verify(mfaRecoveryCodes).markUsed(recovery.id());
    }

    @Test
    void shouldRejectInvalidMfaCode() {
        var mfaEnabledUser = mfaUser();
        var device = new MfaDevice(UUID.randomUUID(), mfaEnabledUser.id(), "SECRET", true, now, now);
        var command = new AuthenticateUserCommand("alice@example.com", "Password1", "127.0.0.1", "junit", "000000");
        when(rateLimiter.isAllowed("alice@example.com")).thenReturn(true);
        when(users.findByEmail(any())).thenReturn(Optional.of(mfaEnabledUser));
        when(hasher.matches("Password1", mfaEnabledUser.passwordHash())).thenReturn(true);
        when(mfaDevices.findByUserId(mfaEnabledUser.id())).thenReturn(Optional.of(device));
        when(totp.verifyCode("SECRET", "000000")).thenReturn(false);
        when(tokenHashing.hash("000000")).thenReturn(TokenHash.of("hashed-000000"));
        when(mfaRecoveryCodes.findActiveByUserIdAndHash(eq(mfaEnabledUser.id()), any())).thenReturn(Optional.empty());
        when(clock.now()).thenReturn(now);

        assertThatThrownBy(() -> service.execute(command)).isInstanceOf(InvalidMfaCodeException.class);

        verify(rateLimiter).recordFailure("alice@example.com");
        verifyNoInteractions(refreshTokens, tokenProvider);
    }
}
