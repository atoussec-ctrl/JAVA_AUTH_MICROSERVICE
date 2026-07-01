package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshTokenJpaEntity, UUID> {
    Optional<RefreshTokenJpaEntity> findByTokenHash(String tokenHash);
    List<RefreshTokenJpaEntity> findAllByUserId(UUID userId);
}
