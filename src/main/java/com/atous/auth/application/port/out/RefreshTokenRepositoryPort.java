package com.atous.auth.application.port.out;

import com.atous.auth.domain.model.RefreshToken;
import com.atous.auth.domain.valueobject.*;
import java.util.Optional;

public interface RefreshTokenRepositoryPort { RefreshToken save(RefreshToken t); Optional<RefreshToken> findByTokenHash(TokenHash h); void revoke(TokenHash h); void revokeAllByUserId(UserId id); }
