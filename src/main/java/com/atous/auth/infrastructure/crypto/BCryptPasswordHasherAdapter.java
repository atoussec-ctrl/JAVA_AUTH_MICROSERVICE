package com.atous.auth.infrastructure.crypto;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.atous.auth.application.port.out.PasswordHasherPort;
import com.atous.auth.domain.valueobject.PasswordHash;

@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {
    private final PasswordEncoder encoder;
    public BCryptPasswordHasherAdapter(PasswordEncoder encoder){this.encoder=encoder;}

    @Override
    public PasswordHash hash(String raw){return PasswordHash.of(encoder.encode(raw));}

    @Override
    public boolean matches(String raw, PasswordHash hash){return encoder.matches(raw, hash.value());}
}
