package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.AssignRolesCommand;
import com.atous.auth.application.dto.view.UserView;

public interface AssignRolesToUserUseCase { UserView execute(AssignRolesCommand command); }
