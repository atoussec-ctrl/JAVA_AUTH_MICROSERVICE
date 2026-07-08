package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.RegisterUserCommand;
import com.atous.auth.application.dto.result.RegisterUserResult;
import com.atous.auth.application.port.in.RegisterUserUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.UserAlreadyExistsException;
import com.atous.auth.domain.model.User;
import com.atous.auth.domain.service.PasswordPolicyService;
import com.atous.auth.domain.valueobject.*;
import java.util.Set;

public final class RegisterUserService implements RegisterUserUseCase {
    private final UserRepositoryPort users;
    private final PasswordHasherPort hasher;
    private final ClockProviderPort clock;
    private final PasswordPolicyService policy;

    public RegisterUserService(UserRepositoryPort users, PasswordHasherPort hasher, ClockProviderPort clock,
            PasswordPolicyService policy) {
        this.users = users;
        this.hasher = hasher;
        this.clock = clock;
        this.policy = policy;
    }

    public RegisterUserResult execute(RegisterUserCommand c) {
        var email = Email.of(c.email());
        if (users.existsByEmail(email))
            throw new UserAlreadyExistsException("User already exists");
        policy.validate(c.password(), c.passwordConfirmation());
        var now = clock.now();
        var saved = users.save(User.builder().id(UserId.newId()).name(c.name()).email(email)
                .passwordHash(hasher.hash(c.password())).roles(Set.of()).enabled(true).emailVerified(false)
                .mfaEnabled(false).createdAt(now).updatedAt(now).build());
        return new RegisterUserResult(saved.id().value(), saved.name(), saved.email().value(), saved.enabled(),
                saved.emailVerified(), saved.createdAt());
    }
}
