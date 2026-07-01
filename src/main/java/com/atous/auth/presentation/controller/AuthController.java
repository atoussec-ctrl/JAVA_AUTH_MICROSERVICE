package com.atous.auth.presentation.controller;

import com.atous.auth.application.dto.command.*;
import com.atous.auth.application.port.in.*;
import com.atous.auth.presentation.dto.request.*;
import com.atous.auth.presentation.dto.response.*;
import com.atous.auth.presentation.mapper.AuthPresentationMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegisterUserUseCase registerUser;
    private final AuthenticateUserUseCase authenticate;
    private final RefreshTokenUseCase refresh;
    private final LogoutUseCase logout;
    private final ValidateTokenUseCase validate;
    private final ForgotPasswordUseCase forgot;
    private final ResetPasswordUseCase reset;
    private final AuthPresentationMapper mapper;

    public AuthController(
        RegisterUserUseCase registerUser, 
        AuthenticateUserUseCase authenticate, 
        RefreshTokenUseCase refresh, 
        LogoutUseCase logout, 
        ValidateTokenUseCase validate, 
        ForgotPasswordUseCase forgot, 
        ResetPasswordUseCase reset, 
        AuthPresentationMapper mapper
    ){
        this.registerUser = registerUser;
        this.authenticate = authenticate;
        this.refresh = refresh;
        this.logout = logout;
        this.validate = validate;
        this.forgot = forgot;
        this.reset = reset;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        var r = registerUser.execute(mapper.toCommand(request));
        return new RegisterUserResponse(r.id(), r.name(), r.email(), r.enabled(), r.emailVerified(), r.createdAt());
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servlet) {
        return mapper.toResponse(authenticate.execute(mapper.toCommand(request, ip(servlet), servlet.getHeader(HttpHeaders.USER_AGENT))));
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request, HttpServletRequest servlet) {
        return mapper.toResponse(refresh.execute(mapper.toCommand(request, ip(servlet), servlet.getHeader(HttpHeaders.USER_AGENT))));
    }

    @PostMapping("/logout")
    public LogoutResponse logout(@Valid @RequestBody LogoutRequest request, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization, HttpServletRequest servlet) {
        return mapper.toResponse(logout.execute(mapper.toCommand(request, authorization, ip(servlet), servlet.getHeader(HttpHeaders.USER_AGENT))));
    }

    @PostMapping("/validate")
    public TokenValidationResponse validate(@Valid @RequestBody ValidateTokenRequest request) {
        var r = validate.execute(new ValidateTokenCommand(request.accessToken()));
        return new TokenValidationResponse(r.valid(), r.userId(), r.email(), r.roles(), r.permissions(), r.issuedAt(), r.expiresAt());
    }

    @PostMapping("/forgot-password")
    public MessageResponse forgot(@Valid @RequestBody ForgotPasswordRequest request, HttpServletRequest servlet) {
        return MessageResponse.of(forgot.execute(new ForgotPasswordCommand(request.email(), ip(servlet), servlet.getHeader(HttpHeaders.USER_AGENT))).message());
    }

    @PostMapping("/reset-password")
    public MessageResponse reset(@Valid @RequestBody ResetPasswordRequest request, HttpServletRequest servlet) {
        return MessageResponse.of(reset.execute(new ResetPasswordCommand(request.resetToken(), request.newPassword(), request.passwordConfirmation(), ip(servlet), servlet.getHeader(HttpHeaders.USER_AGENT))).message());
    }

    private String ip(HttpServletRequest r) {
        var f = r.getHeader("X-Forwarded-For");
        return f != null && !f.isBlank() ? f.split(",")[0].trim() : r.getRemoteAddr();
    }
}
