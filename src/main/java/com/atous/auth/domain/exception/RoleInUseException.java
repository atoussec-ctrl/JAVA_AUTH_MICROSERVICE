package com.atous.auth.domain.exception;

public class RoleInUseException extends DomainException {
    public RoleInUseException(String m) {
        super(m);
    }
}
