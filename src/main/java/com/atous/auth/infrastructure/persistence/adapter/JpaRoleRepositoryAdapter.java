package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.RoleRepositoryPort;
import com.atous.auth.domain.model.Role;
import com.atous.auth.domain.valueobject.RoleId;
import com.atous.auth.infrastructure.persistence.mapper.RolePersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataRoleRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class JpaRoleRepositoryAdapter implements RoleRepositoryPort {
    private final SpringDataRoleRepository repo;
    private final RolePersistenceMapper mapper;

    public JpaRoleRepositoryAdapter(SpringDataRoleRepository repo, RolePersistenceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Optional<Role> findById(RoleId id) {
        return repo.findById(id.value()).map(mapper::toDomain);
    }

    public Optional<Role> findByName(String name) {
        return repo.findByName(name).map(mapper::toDomain);
    }

    public List<Role> findAllByIds(Collection<RoleId> ids) {
        return repo.findAllById(ids.stream().map(RoleId::value).toList()).stream().map(mapper::toDomain).toList();
    }

    public List<Role> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    public Role save(Role r) {
        return mapper.toDomain(repo.save(mapper.toEntity(r)));
    }

    public void deleteById(RoleId id) {
        repo.deleteById(id.value());
    }
}
