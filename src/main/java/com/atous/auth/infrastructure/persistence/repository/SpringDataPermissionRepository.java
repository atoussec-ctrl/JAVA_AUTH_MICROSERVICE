package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.PermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SpringDataPermissionRepository extends JpaRepository<PermissionJpaEntity, UUID> {
    List<PermissionJpaEntity> findAllByNameIn(Collection<String> names);
}
