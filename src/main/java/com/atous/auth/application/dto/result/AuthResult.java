package com.atous.auth.application.dto.result;
import java.time.Instant;

import com.atous.auth.application.dto.view.UserSummaryView;
public record AuthResult(
        String accessToken,
        String refreshToken, 
        String tokenType, 
        long expiresIn,
        Instant issuedAt, 
        Instant expiresAt,
       UserSummaryView user
    ) {}
