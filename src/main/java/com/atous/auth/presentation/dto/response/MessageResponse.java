package com.atous.auth.presentation.dto.response;

import java.time.Instant;

public record MessageResponse(String message, Instant timestamp) {
    public static MessageResponse of(String message){return new MessageResponse(message, Instant.now());}
}
