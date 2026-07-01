package com.atous.auth.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SearchUsersRequest(String search, Boolean enabled, @Min(0) Integer page, @Min(1) @Max(100) Integer size) {
    public int normalizedPage(){return page == null ? 0 : page;}
    public int normalizedSize(){return size == null ? 20 : size;}
}
