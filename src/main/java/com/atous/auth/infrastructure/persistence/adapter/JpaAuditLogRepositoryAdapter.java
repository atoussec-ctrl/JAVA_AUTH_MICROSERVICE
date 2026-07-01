package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.AuditLogRepositoryPort; import com.atous.auth.domain.model.AuditLog; import org.springframework.stereotype.Repository;

@Repository public class JpaAuditLogRepositoryAdapter implements AuditLogRepositoryPort { public AuditLog save(AuditLog auditLog){return auditLog;} }
