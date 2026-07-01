package com.atous.auth.application.port.out;

import com.atous.auth.domain.model.PasswordResetToken;
import com.atous.auth.domain.valueobject.TokenHash;
import java.util.Optional;

public interface PasswordResetTokenRepositoryPort { PasswordResetToken save(PasswordResetToken t); Optional<PasswordResetToken> findByTokenHash(TokenHash h); void markUsed(TokenHash h); }
