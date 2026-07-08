package com.atous.auth.domain.exception;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String m) {
        super(m);
    }
}
