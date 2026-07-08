package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.MfaRecoveryCodeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SpringDataMfaRecoveryCodeRepository extends JpaRepository<MfaRecoveryCodeJpaEntity, UUID> {
    Optional<MfaRecoveryCodeJpaEntity> findByUserIdAndCodeHashAndUsedFalse(UUID userId, String codeHash);

    void deleteAllByUserId(UUID userId);
}
