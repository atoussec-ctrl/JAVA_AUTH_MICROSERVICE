package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.UpdateUserStatusCommand;
import com.atous.auth.application.dto.view.UserView;
import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.in.UpdateUserStatusUseCase;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.exception.UserNotFoundException;
import com.atous.auth.domain.valueobject.UserId;

public final class UpdateUserStatusService implements UpdateUserStatusUseCase {
    private final UserRepositoryPort users;
    private final ClockProviderPort clock;
    private final UserApplicationMapper mapper;

    public UpdateUserStatusService(UserRepositoryPort users, ClockProviderPort clock, UserApplicationMapper mapper) {
        this.users = users;
        this.clock = clock;
        this.mapper = mapper;
    }

    public UserView execute(UpdateUserStatusCommand c) {
        var u = users.findById(UserId.of(c.userId())).orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.toView(users.save(u.withEnabled(c.enabled(), clock.now())));
    }
}
