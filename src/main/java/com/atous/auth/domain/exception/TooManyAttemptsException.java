package com.atous.auth.domain.exception;

public class TooManyAttemptsException extends DomainException {
    public TooManyAttemptsException(String m) {
        super(m);
    }
}
