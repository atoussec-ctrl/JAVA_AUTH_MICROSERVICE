package com.atous.auth.presentation.dto.request;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoleRequest(
    @NotBlank 
    @Size(min=2,max=80) 
    String name, 

    @Size(max=255) 
    String description, 

    Set<String> permissions
){}
