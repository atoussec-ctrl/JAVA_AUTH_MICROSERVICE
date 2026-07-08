package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.EnableMfaCommand;
import com.atous.auth.application.dto.result.MfaSetupResult;
import com.atous.auth.application.port.in.EnableMfaUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.InvalidCredentialsException;
import com.atous.auth.domain.exception.UserNotFoundException;
import com.atous.auth.domain.model.MfaDevice;
import com.atous.auth.domain.model.MfaRecoveryCode;
import com.atous.auth.domain.valueobject.UserId;
import java.util.UUID;
import java.util.stream.Collectors;

public final class EnableMfaService implements EnableMfaUseCase {
    private static final int RECOVERY_CODE_COUNT = 8;
    private static final String ISSUER = "auth-service";

    private final UserRepositoryPort users;
    private final PasswordHasherPort hasher;
    private final MfaDeviceRepositoryPort devices;
    private final MfaRecoveryCodeRepositoryPort recoveryCodes;
    private final TotpPort totp;
    private final TokenHashingPort hash;
    private final ClockProviderPort clock;

    public EnableMfaService(UserRepositoryPort users, PasswordHasherPort hasher, MfaDeviceRepositoryPort devices,
            MfaRecoveryCodeRepositoryPort recoveryCodes, TotpPort totp, TokenHashingPort hash,
            ClockProviderPort clock) {
        this.users = users;
        this.hasher = hasher;
        this.devices = devices;
        this.recoveryCodes = recoveryCodes;
        this.totp = totp;
        this.hash = hash;
        this.clock = clock;
    }

    public MfaSetupResult execute(EnableMfaCommand c) {
        var id = UserId.of(c.userId());
        var user = users.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!hasher.matches(c.password(), user.passwordHash()))
            throw new InvalidCredentialsException();

        var now = clock.now();
        // secretHash actually holds the raw Base32 TOTP secret: HOTP/TOTP verification needs the
        // original key material to recompute codes, so it cannot be a one-way hash like other *Hash fields.
        var secret = totp.generateSecret();
        var deviceId = devices.findByUserId(id).map(MfaDevice::id).orElseGet(UUID::randomUUID);
        devices.save(new MfaDevice(deviceId, id, secret, false, now, null));

        var codes = totp.generateRecoveryCodes(RECOVERY_CODE_COUNT);
        recoveryCodes.deleteAllByUserId(id);
        recoveryCodes.saveAll(codes.stream()
                .map(code -> new MfaRecoveryCode(UUID.randomUUID(), id, hash.hash(code), false, now, null))
                .collect(Collectors.toList()));

        var uri = totp.provisioningUri(ISSUER, user.email().value(), secret);
        return new MfaSetupResult(secret, uri, codes);
    }
}
