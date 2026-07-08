package com.atous.auth.application.dto.view;
import java.util.Set;
import java.util.UUID;

public record UserSummaryView(
    UUID id, 
    String name, 
    String email, 
    boolean enabled, 
    boolean emailVerified, 
    boolean mfaEnabled, 
    Set<String> roles, 
    Set<String> permissions) {}
