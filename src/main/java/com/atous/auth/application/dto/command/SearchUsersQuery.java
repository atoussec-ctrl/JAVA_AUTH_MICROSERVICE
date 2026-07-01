package com.atous.auth.application.dto.command;

public record SearchUsersQuery(String search, Boolean enabled, int page, int size) {}
