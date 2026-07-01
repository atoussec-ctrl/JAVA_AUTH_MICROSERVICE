package com.atous.auth.application.port.out;

import com.atous.auth.domain.model.AuditLog; public interface AuditLogRepositoryPort { AuditLog save(AuditLog auditLog); }
