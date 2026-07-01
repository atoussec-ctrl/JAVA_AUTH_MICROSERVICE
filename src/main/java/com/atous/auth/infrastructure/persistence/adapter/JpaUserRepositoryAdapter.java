package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.dto.view.PageView;
import com.atous.auth.application.port.out.UserRepositoryPort;
import com.atous.auth.domain.model.User;
import com.atous.auth.domain.valueobject.*;
import com.atous.auth.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository repo; private final UserPersistenceMapper mapper;
    public JpaUserRepositoryAdapter(SpringDataUserRepository repo, UserPersistenceMapper mapper){this.repo=repo;this.mapper=mapper;}
    public Optional<User> findById(UserId id){return repo.findById(id.value()).map(mapper::toDomain);}
    public Optional<User> findByEmail(Email email){return repo.findByEmail(email.value()).map(mapper::toDomain);}
    public boolean existsByEmail(Email email){return repo.existsByEmail(email.value());}
    public User save(User u){return mapper.toDomain(repo.save(mapper.toEntity(u)));}
    public PageView<User> search(String search, Boolean enabled, int page, int size){var p=repo.search(search==null||search.isBlank()?null:search, enabled, PageRequest.of(page,size)).map(mapper::toDomain); return new PageView<>(p.getContent(),p.getNumber(),p.getSize(),p.getTotalElements(),p.getTotalPages(),p.isFirst(),p.isLast());}
}
