package com.atous.auth.infrastructure.token;

import com.atous.auth.application.port.out.RefreshTokenGeneratorPort; import org.springframework.stereotype.Component; import java.security.SecureRandom; import java.util.Base64;

@Component public class SecureRefreshTokenGeneratorAdapter implements RefreshTokenGeneratorPort { private static final int BYTES=64; private final SecureRandom random=new SecureRandom(); public String generate(){byte[] b=new byte[BYTES]; random.nextBytes(b); return Base64.getUrlEncoder().withoutPadding().encodeToString(b);} }
