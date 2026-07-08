package com.atous.auth.application.port.out;

import com.atous.auth.domain.model.MfaRecoveryCode;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MfaRecoveryCodeRepositoryPort {
    List<MfaRecoveryCode> saveAll(List<MfaRecoveryCode> codes);

    Optional<MfaRecoveryCode> findActiveByUserIdAndHash(UserId userId, TokenHash hash);

    void markUsed(UUID id);

    void deleteAllByUserId(UserId userId);
}
