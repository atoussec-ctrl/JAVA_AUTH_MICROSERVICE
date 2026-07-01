package com.atous.auth.application.dto.command;

public record RegisterUserCommand(String name, String email, String password, String passwordConfirmation) {}
