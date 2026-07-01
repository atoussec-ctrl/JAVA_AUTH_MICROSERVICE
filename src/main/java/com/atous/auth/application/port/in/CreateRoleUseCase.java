package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.CreateRoleCommand;
import com.atous.auth.application.dto.view.RoleView;

public interface CreateRoleUseCase { RoleView execute(CreateRoleCommand command); }
