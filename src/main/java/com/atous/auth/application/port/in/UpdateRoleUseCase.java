package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.UpdateRoleCommand;
import com.atous.auth.application.dto.view.RoleView;

public interface UpdateRoleUseCase { RoleView execute(UpdateRoleCommand command); }
