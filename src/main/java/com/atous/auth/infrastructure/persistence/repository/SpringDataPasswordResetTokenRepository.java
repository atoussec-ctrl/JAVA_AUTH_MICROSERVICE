package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.PasswordResetTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SpringDataPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenJpaEntity, UUID> {
    Optional<PasswordResetTokenJpaEntity> findByTokenHash(String tokenHash);
}
