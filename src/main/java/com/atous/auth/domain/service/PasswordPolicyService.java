package com.atous.auth.domain.service;

import com.atous.auth.domain.exception.InvalidPasswordException;

public final class PasswordPolicyService {
    public void validate(String password, String confirmation) {
        if (password == null || password.isBlank())
            throw new InvalidPasswordException("Password is required");
        if (!password.equals(confirmation))
            throw new InvalidPasswordException("Password confirmation does not match");
        if (password.length() < 8 || password.length() > 128)
            throw new InvalidPasswordException("Password must have between 8 and 128 characters");
        boolean up = password.chars().anyMatch(Character::isUpperCase),
                low = password.chars().anyMatch(Character::isLowerCase),
                digit = password.chars().anyMatch(Character::isDigit);
        if (!up || !low || !digit)
            throw new InvalidPasswordException("Password must contain uppercase, lowercase and number");
    }
}
