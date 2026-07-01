package com.atous.auth.infrastructure.token;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@SuppressWarnings("unused") 
public class JwtKeyConfig {
    @Bean
    RSAKey rsaKey() {
        try {
            var gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            var kp = gen.generateKeyPair();
            return new RSAKey.Builder((RSAPublicKey) kp.getPublic()).privateKey((RSAPrivateKey) kp.getPrivate())
                    .keyID(UUID.randomUUID().toString()).build();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to generate RSA key", e);
        }
    }

    @Bean
    JWKSource<SecurityContext> jwkSource(RSAKey key) {
        return new ImmutableJWKSet<>(new JWKSet(key));
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> source) {
        return new NimbusJwtEncoder(source);
    }

    @Bean
    JwtDecoder jwtDecoder(RSAKey key) throws Exception {
        return NimbusJwtDecoder.withPublicKey(key.toRSAPublicKey()).build();
    }
}
