package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.AuditLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SpringDataAuditLogRepository extends JpaRepository<AuditLogJpaEntity, UUID> {

}
