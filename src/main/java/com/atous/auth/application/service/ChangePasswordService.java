package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.ChangePasswordCommand;
import com.atous.auth.application.dto.result.PasswordOperationResult;
import com.atous.auth.application.port.in.ChangePasswordUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.*;
import com.atous.auth.domain.service.PasswordPolicyService;
import com.atous.auth.domain.valueobject.UserId;

public final class ChangePasswordService implements ChangePasswordUseCase {
    private final UserRepositoryPort users;
    private final RefreshTokenRepositoryPort refreshTokens;
    private final PasswordHasherPort hasher;
    private final PasswordPolicyService policy;
    private final ClockProviderPort clock;

    public ChangePasswordService(UserRepositoryPort users, RefreshTokenRepositoryPort refreshTokens,
            PasswordHasherPort hasher, PasswordPolicyService policy, ClockProviderPort clock) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.hasher = hasher;
        this.policy = policy;
        this.clock = clock;
    }

    public PasswordOperationResult execute(ChangePasswordCommand c) {
        var id = UserId.of(c.userId());
        var u = users.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!hasher.matches(c.currentPassword(), u.passwordHash()))
            throw new InvalidCredentialsException();
        policy.validate(c.newPassword(), c.passwordConfirmation());
        var now = clock.now();
        users.save(u.withPasswordHash(hasher.hash(c.newPassword()), now));
        refreshTokens.revokeAllByUserId(id);
        return new PasswordOperationResult(true, "Password changed successfully", now);
    }
}
