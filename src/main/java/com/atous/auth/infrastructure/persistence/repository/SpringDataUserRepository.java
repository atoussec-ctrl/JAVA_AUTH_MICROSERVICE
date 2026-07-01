package com.atous.auth.infrastructure.persistence.repository;

import com.atous.auth.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
       select u from UserJpaEntity u
       where (:enabled is null or u.enabled = :enabled)
         and (:search is null or lower(u.email) like lower(concat('%', :search, '%')) or lower(u.name) like lower(concat('%', :search, '%')))
       """)
    Page<UserJpaEntity> search(@Param("search") String search, @Param("enabled") Boolean enabled, Pageable pageable);
}
