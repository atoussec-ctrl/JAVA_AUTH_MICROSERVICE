package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.DisableMfaCommand;
import com.atous.auth.application.dto.result.MfaVerificationResult;
import com.atous.auth.application.port.in.DisableMfaUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.InvalidCredentialsException;
import com.atous.auth.domain.exception.InvalidMfaCodeException;
import com.atous.auth.domain.exception.UserNotFoundException;
import com.atous.auth.domain.model.MfaDevice;
import com.atous.auth.domain.valueobject.UserId;

public final class DisableMfaService implements DisableMfaUseCase {
    private final UserRepositoryPort users;
    private final PasswordHasherPort hasher;
    private final MfaDeviceRepositoryPort devices;
    private final MfaRecoveryCodeRepositoryPort recoveryCodes;
    private final TotpPort totp;
    private final ClockProviderPort clock;

    public DisableMfaService(UserRepositoryPort users, PasswordHasherPort hasher, MfaDeviceRepositoryPort devices,
            MfaRecoveryCodeRepositoryPort recoveryCodes, TotpPort totp, ClockProviderPort clock) {
        this.users = users;
        this.hasher = hasher;
        this.devices = devices;
        this.recoveryCodes = recoveryCodes;
        this.totp = totp;
        this.clock = clock;
    }

    @Override
    public MfaVerificationResult execute(DisableMfaCommand c) {
        var id = UserId.of(c.userId());
        var user = users.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!hasher.matches(c.password(), user.passwordHash()))
            throw new InvalidCredentialsException();
        var device = devices.findByUserId(id)
                .orElseThrow(() -> new InvalidMfaCodeException("MFA is not set up for this account"));
        if (!totp.verifyCode(device.secretHash(), c.code()))
            throw new InvalidMfaCodeException("Invalid MFA code");

        var now = clock.now();
        devices.save(new MfaDevice(device.id(), id, device.secretHash(), false, device.createdAt(),
                device.verifiedAt()));
        recoveryCodes.deleteAllByUserId(id);
        users.save(user.withMfaEnabled(false, now));
        return new MfaVerificationResult(true, false, now);
    }
}
