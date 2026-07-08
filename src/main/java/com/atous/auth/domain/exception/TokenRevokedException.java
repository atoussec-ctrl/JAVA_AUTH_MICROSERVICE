package com.atous.auth.domain.exception;

public class TokenRevokedException extends DomainException {
    public TokenRevokedException(String m) {
        super(m);
    }
}
