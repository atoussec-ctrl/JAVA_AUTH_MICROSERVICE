package com.atous.auth.domain.valueobject;

import java.util.regex.Pattern;

import com.atous.auth.domain.exception.InvalidEmailException;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE);

    public Email {
        if (value == null || value.isBlank())
            throw new InvalidEmailException("Invalid email");
        value = value.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(value).matches())
            throw new InvalidEmailException("Invalid email");
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
