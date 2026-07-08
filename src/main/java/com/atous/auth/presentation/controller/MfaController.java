package com.atous.auth.presentation.controller;

import com.atous.auth.application.dto.command.*;
import com.atous.auth.application.port.in.*;
import com.atous.auth.presentation.dto.request.*;
import com.atous.auth.presentation.dto.response.*;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me/mfa")
public class MfaController {
    private final EnableMfaUseCase enable;
    private final VerifyMfaUseCase verify;
    private final DisableMfaUseCase disable;

    public MfaController(EnableMfaUseCase enable, VerifyMfaUseCase verify, DisableMfaUseCase disable) {
        this.enable = enable;
        this.verify = verify;
        this.disable = disable;
    }

    @PostMapping("/enable")
    public MfaSetupResponse enable(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody EnableMfaRequest r) {
        var x = enable.execute(new EnableMfaCommand(UUID.fromString(jwt.getSubject()), r.password()));
        return new MfaSetupResponse(x.secret(), x.qrCodeUri(), x.recoveryCodes());
    }

    @PostMapping("/verify")
    public MfaVerificationResponse verify(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody VerifyMfaRequest r) {
        var x = verify.execute(new VerifyMfaCommand(UUID.fromString(jwt.getSubject()), r.code()));
        return new MfaVerificationResponse(x.verified(), x.mfaEnabled(), x.verifiedAt());
    }

    @PostMapping("/disable")
    public MfaVerificationResponse disable(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody DisableMfaRequest r) {
        var x = disable.execute(new DisableMfaCommand(UUID.fromString(jwt.getSubject()), r.password(), r.code()));
        return new MfaVerificationResponse(x.verified(), x.mfaEnabled(), x.verifiedAt());
    }
}
