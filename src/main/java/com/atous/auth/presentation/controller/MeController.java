package com.atous.auth.presentation.controller;

import com.atous.auth.application.dto.command.ChangePasswordCommand;
import com.atous.auth.application.port.in.*;
import com.atous.auth.presentation.dto.request.ChangePasswordRequest;
import com.atous.auth.presentation.dto.response.*;
import com.atous.auth.presentation.mapper.UserPresentationMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {
    private final GetAuthenticatedUserUseCase getMe;
    private final ChangePasswordUseCase changePassword;
    private final UserPresentationMapper mapper;

    public MeController(GetAuthenticatedUserUseCase getMe, ChangePasswordUseCase changePassword,
            UserPresentationMapper mapper) {
        this.getMe = getMe;
        this.changePassword = changePassword;
        this.mapper = mapper;
    }

    @GetMapping
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return mapper.toResponse(getMe.execute(UUID.fromString(jwt.getSubject())));
    }

    @PostMapping("/change-password")
    public MessageResponse changePassword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ChangePasswordRequest r,
            HttpServletRequest servlet) {
        changePassword.execute(new ChangePasswordCommand(UUID.fromString(jwt.getSubject()), r.currentPassword(),
                r.newPassword(), r.passwordConfirmation(), ip(servlet), servlet.getHeader(HttpHeaders.USER_AGENT)));
        return MessageResponse.of("Password changed successfully");
    }

    private String ip(HttpServletRequest r) {
        var f = r.getHeader("X-Forwarded-For");
        return f != null && !f.isBlank() ? f.split(",")[0].trim() : r.getRemoteAddr();
    }
}
