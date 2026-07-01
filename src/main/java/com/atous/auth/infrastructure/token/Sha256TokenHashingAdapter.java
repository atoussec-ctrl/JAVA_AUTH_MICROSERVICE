package com.atous.auth.infrastructure.token;

import com.atous.auth.application.port.out.TokenHashingPort; import com.atous.auth.domain.valueobject.TokenHash; import org.springframework.stereotype.Component; import java.nio.charset.StandardCharsets; import java.security.*; import java.util.HexFormat;

@Component public class Sha256TokenHashingAdapter implements TokenHashingPort { public TokenHash hash(String rawToken){try{var d=MessageDigest.getInstance("SHA-256").digest(rawToken.getBytes(StandardCharsets.UTF_8)); return TokenHash.of(HexFormat.of().formatHex(d));} catch(NoSuchAlgorithmException e){throw new IllegalStateException("SHA-256 unavailable", e);}} }
