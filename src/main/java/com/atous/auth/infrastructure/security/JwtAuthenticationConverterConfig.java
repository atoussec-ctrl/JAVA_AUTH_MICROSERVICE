package com.atous.auth.infrastructure.security;

import org.springframework.context.annotation.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.*;

@Configuration
public class JwtAuthenticationConverterConfig {
    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return jwt -> {
            var authorities = new ArrayList<SimpleGrantedAuthority>();
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null) roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
            List<String> permissions = jwt.getClaimAsStringList("permissions");
            if (permissions != null) permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
            return new JwtAuthenticationToken(jwt, authorities);
        };
    }
}
