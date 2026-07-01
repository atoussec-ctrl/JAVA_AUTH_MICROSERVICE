package com.atous.auth.application.port.in;
import com.atous.auth.application.dto.command.RefreshTokenCommand;
import com.atous.auth.application.dto.result.AuthResult;    

public interface RefreshTokenUseCase { AuthResult execute(RefreshTokenCommand command); }
