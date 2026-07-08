package com.atous.auth.domain.exception;

public class InvalidMfaCodeException extends DomainException {
    public InvalidMfaCodeException(String m) {
        super(m);
    }
}
