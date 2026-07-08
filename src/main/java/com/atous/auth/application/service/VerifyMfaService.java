package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.VerifyMfaCommand;
import com.atous.auth.application.dto.result.MfaVerificationResult;
import com.atous.auth.application.port.in.VerifyMfaUseCase;
import com.atous.auth.application.port.out.ClockProviderPort;
import com.atous.auth.application.port.out.MfaDeviceRepositoryPort;
import com.atous.auth.application.port.out.TotpPort;
import com.atous.auth.application.port.out.UserRepositoryPort;
import com.atous.auth.domain.exception.InvalidMfaCodeException;
import com.atous.auth.domain.exception.UserNotFoundException;
import com.atous.auth.domain.model.MfaDevice;
import com.atous.auth.domain.valueobject.UserId;

public final class VerifyMfaService implements VerifyMfaUseCase {
    private final UserRepositoryPort users;
    private final MfaDeviceRepositoryPort devices;
    private final TotpPort totp;
    private final ClockProviderPort clock;

    public VerifyMfaService(UserRepositoryPort users, MfaDeviceRepositoryPort devices, TotpPort totp,
            ClockProviderPort clock) {
        this.users = users;
        this.devices = devices;
        this.totp = totp;
        this.clock = clock;
    }

    public MfaVerificationResult execute(VerifyMfaCommand c) {
        var id = UserId.of(c.userId());
        var user = users.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        var device = devices.findByUserId(id)
                .orElseThrow(() -> new InvalidMfaCodeException("MFA is not set up for this account"));
        if (!totp.verifyCode(device.secretHash(), c.code()))
            throw new InvalidMfaCodeException("Invalid MFA code");

        var now = clock.now();
        devices.save(new MfaDevice(device.id(), id, device.secretHash(), true, device.createdAt(), now));
        users.save(user.withMfaEnabled(true, now));
        return new MfaVerificationResult(true, true, now);
    }
}
