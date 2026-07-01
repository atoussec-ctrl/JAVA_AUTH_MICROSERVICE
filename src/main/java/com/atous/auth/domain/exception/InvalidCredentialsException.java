package com.atous.auth.domain.exception;

public class InvalidCredentialsException extends DomainException { public InvalidCredentialsException(){super("Invalid credentials");} public InvalidCredentialsException(String m){super(m);} }
