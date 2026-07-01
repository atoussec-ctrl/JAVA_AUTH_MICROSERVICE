package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.MfaDeviceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SpringDataMfaDeviceRepository extends JpaRepository<MfaDeviceJpaEntity, UUID> {
    Optional<MfaDeviceJpaEntity> findByUserId(UUID userId);
}
