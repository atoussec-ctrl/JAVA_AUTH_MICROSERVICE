package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SpringDataRoleRepository extends JpaRepository<RoleJpaEntity, UUID> {
    Optional<RoleJpaEntity> findByName(String name);
}
