package com.atous.auth.presentation.mapper;

import com.atous.auth.application.dto.command.*;
import com.atous.auth.application.dto.result.*;
import com.atous.auth.presentation.dto.request.*;
import com.atous.auth.presentation.dto.response.*;
import org.springframework.stereotype.Component;

@Component
public class AuthPresentationMapper {
    public RegisterUserCommand toCommand(RegisterUserRequest r){return new RegisterUserCommand(r.name(),r.email(),r.password(),r.passwordConfirmation());}
    public AuthenticateUserCommand toCommand(LoginRequest r,String ip,String ua){return new AuthenticateUserCommand(r.email(),r.password(),ip,ua);}
    public RefreshTokenCommand toCommand(RefreshTokenRequest r,String ip,String ua){return new RefreshTokenCommand(r.refreshToken(),ip,ua);}
    public LogoutCommand toCommand(LogoutRequest r,String access,String ip,String ua){return new LogoutCommand(r.refreshToken(),access,ip,ua);}
    public AuthResponse toResponse(AuthResult r){var u=r.user(); return new AuthResponse(r.accessToken(),r.refreshToken(),r.tokenType(),r.expiresIn(),r.issuedAt(),r.expiresAt(),new UserSummaryResponse(u.id(),u.name(),u.email(),u.enabled(),u.emailVerified(),u.mfaEnabled(),u.roles(),u.permissions()));}
    public LogoutResponse toResponse(LogoutResult r){return new LogoutResponse(r.success(),r.message(),r.loggedOutAt());}
}
