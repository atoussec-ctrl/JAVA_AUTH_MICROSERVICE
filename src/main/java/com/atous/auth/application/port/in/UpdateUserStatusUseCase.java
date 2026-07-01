package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.UpdateUserStatusCommand;
import com.atous.auth.application.dto.view.UserView;

public interface UpdateUserStatusUseCase { UserView execute(UpdateUserStatusCommand command); }
