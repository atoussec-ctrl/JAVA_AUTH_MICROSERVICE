package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.DisableMfaCommand;
import com.atous.auth.application.dto.command.EnableMfaCommand;
import com.atous.auth.application.dto.command.VerifyMfaCommand;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.InvalidCredentialsException;
import com.atous.auth.domain.exception.InvalidMfaCodeException;
import com.atous.auth.domain.model.MfaDevice;
import com.atous.auth.domain.model.User;
import com.atous.auth.domain.valueobject.Email;
import com.atous.auth.domain.valueobject.PasswordHash;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MfaLifecycleServiceTest {
    @Mock private UserRepositoryPort users;
    @Mock private PasswordHasherPort hasher;
    @Mock private MfaDeviceRepositoryPort devices;
    @Mock private MfaRecoveryCodeRepositoryPort recoveryCodes;
    @Mock private TotpPort totp;
    @Mock private TokenHashingPort tokenHashing;
    @Mock private ClockProviderPort clock;

    private final Instant now = Instant.parse("2026-01-01T00:00:00Z");
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(UserId.newId()).name("Alice").email(Email.of("alice@example.com"))
                .passwordHash(PasswordHash.of("hashed")).createdAt(now).build();
    }

    @Test
    void enableShouldPersistDeviceAndRecoveryCodesAfterPasswordCheck() {
        var service = new EnableMfaService(users, hasher, devices, recoveryCodes, totp, tokenHashing, clock);
        when(users.findById(user.id())).thenReturn(Optional.of(user));
        when(hasher.matches("correct-password", user.passwordHash())).thenReturn(true);
        when(devices.findByUserId(user.id())).thenReturn(Optional.empty());
        when(totp.generateSecret()).thenReturn("SECRET");
        when(totp.generateRecoveryCodes(8)).thenReturn(List.of("CODE1AAA", "CODE2BBB"));
        when(totp.provisioningUri(anyString(), anyString(), anyString())).thenReturn("otpauth://totp/x");
        when(tokenHashing.hash(anyString())).thenReturn(TokenHash.of("hashed-code"));
        when(clock.now()).thenReturn(now);

        var result = service.execute(new EnableMfaCommand(user.id().value(), "correct-password"));

        assertThat(result.secret()).isEqualTo("SECRET");
        assertThat(result.recoveryCodes()).containsExactly("CODE1AAA", "CODE2BBB");
        verify(devices).save(argThat(d -> d.secretHash().equals("SECRET") && !d.enabled()));
        verify(recoveryCodes).deleteAllByUserId(user.id());
        verify(recoveryCodes).saveAll(argThat(list -> list.size() == 2));
    }

    @Test
    void enableShouldRejectWrongPasswordWithoutGeneratingSecret() {
        var service = new EnableMfaService(users, hasher, devices, recoveryCodes, totp, tokenHashing, clock);
        when(users.findById(user.id())).thenReturn(Optional.of(user));
        when(hasher.matches("wrong", user.passwordHash())).thenReturn(false);

        assertThatThrownBy(() -> service.execute(new EnableMfaCommand(user.id().value(), "wrong")))
                .isInstanceOf(InvalidCredentialsException.class);

        verifyNoInteractions(totp, devices, recoveryCodes);
    }

    @Test
    void verifyShouldEnableDeviceAndUserWhenCodeMatches() {
        var service = new VerifyMfaService(users, devices, totp, clock);
        var device = new MfaDevice(UUID.randomUUID(), user.id(), "SECRET", false, now, null);
        when(users.findById(user.id())).thenReturn(Optional.of(user));
        when(devices.findByUserId(user.id())).thenReturn(Optional.of(device));
        when(totp.verifyCode("SECRET", "123456")).thenReturn(true);
        when(clock.now()).thenReturn(now);

        var result = service.execute(new VerifyMfaCommand(user.id().value(), "123456"));

        assertThat(result.verified()).isTrue();
        assertThat(result.mfaEnabled()).isTrue();
        verify(devices).save(argThat(MfaDevice::enabled));
        verify(users).save(argThat(User::mfaEnabled));
    }

    @Test
    void verifyShouldRejectWrongCodeWithoutEnablingAnything() {
        var service = new VerifyMfaService(users, devices, totp, clock);
        var device = new MfaDevice(UUID.randomUUID(), user.id(), "SECRET", false, now, null);
        when(users.findById(user.id())).thenReturn(Optional.of(user));
        when(devices.findByUserId(user.id())).thenReturn(Optional.of(device));
        when(totp.verifyCode("SECRET", "000000")).thenReturn(false);

        assertThatThrownBy(() -> service.execute(new VerifyMfaCommand(user.id().value(), "000000")))
                .isInstanceOf(InvalidMfaCodeException.class);

        verify(devices, never()).save(any());
        verify(users, never()).save(any());
    }

    @Test
    void disableShouldRequireBothPasswordAndValidCode() {
        var service = new DisableMfaService(users, hasher, devices, recoveryCodes, totp, clock);
        var mfaUser = User.builder().id(user.id()).name("Alice").email(Email.of("alice@example.com"))
                .passwordHash(PasswordHash.of("hashed")).mfaEnabled(true).createdAt(now).build();
        var device = new MfaDevice(UUID.randomUUID(), user.id(), "SECRET", true, now, now);
        when(users.findById(user.id())).thenReturn(Optional.of(mfaUser));
        when(hasher.matches("correct-password", mfaUser.passwordHash())).thenReturn(true);
        when(devices.findByUserId(user.id())).thenReturn(Optional.of(device));
        when(totp.verifyCode("SECRET", "123456")).thenReturn(true);
        when(clock.now()).thenReturn(now);

        var result = service.execute(new DisableMfaCommand(user.id().value(), "correct-password", "123456"));

        assertThat(result.mfaEnabled()).isFalse();
        verify(devices).save(argThat(d -> !d.enabled()));
        verify(recoveryCodes).deleteAllByUserId(user.id());
        verify(users).save(argThat(u -> !u.mfaEnabled()));
    }
}
