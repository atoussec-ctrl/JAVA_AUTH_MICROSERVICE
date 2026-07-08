package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.AuditLogRepositoryPort;
import com.atous.auth.domain.model.AuditLog;
import com.atous.auth.infrastructure.persistence.mapper.AuditLogPersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataAuditLogRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaAuditLogRepositoryAdapter implements AuditLogRepositoryPort {
    private final SpringDataAuditLogRepository repo;
    private final AuditLogPersistenceMapper mapper;

    public JpaAuditLogRepositoryAdapter(SpringDataAuditLogRepository repo, AuditLogPersistenceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public AuditLog save(AuditLog auditLog) {
        return mapper.toDomain(repo.save(mapper.toEntity(auditLog)));
    }
}
