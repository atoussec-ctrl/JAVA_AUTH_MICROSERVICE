package com.atous.auth.domain.exception;

public class MfaRequiredException extends DomainException {
    public MfaRequiredException(String m) {
        super(m);
    }
}
