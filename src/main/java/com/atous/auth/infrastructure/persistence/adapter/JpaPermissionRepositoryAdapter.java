package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.PermissionRepositoryPort;
import com.atous.auth.domain.model.Permission;
import com.atous.auth.infrastructure.persistence.mapper.PermissionPersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataPermissionRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class JpaPermissionRepositoryAdapter implements PermissionRepositoryPort {
    private final SpringDataPermissionRepository repo; private final PermissionPersistenceMapper mapper;
    public JpaPermissionRepositoryAdapter(SpringDataPermissionRepository repo, PermissionPersistenceMapper mapper){this.repo=repo;this.mapper=mapper;}
    public List<Permission> findAllByNames(Collection<String> names){return names==null||names.isEmpty()?List.of():repo.findAllByNameIn(names).stream().map(mapper::toDomain).toList();}
}
