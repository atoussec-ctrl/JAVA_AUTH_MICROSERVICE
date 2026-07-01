package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.RefreshTokenRepositoryPort;
import com.atous.auth.domain.model.RefreshToken;
import com.atous.auth.domain.valueobject.*;
import com.atous.auth.infrastructure.persistence.mapper.RefreshTokenPersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataRefreshTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public class JpaRefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {
    private final SpringDataRefreshTokenRepository repo; private final RefreshTokenPersistenceMapper mapper;
    public JpaRefreshTokenRepositoryAdapter(SpringDataRefreshTokenRepository repo, RefreshTokenPersistenceMapper mapper){this.repo=repo;this.mapper=mapper;}
    public RefreshToken save(RefreshToken t){return mapper.toDomain(repo.save(mapper.toEntity(t)));}
    public Optional<RefreshToken> findByTokenHash(TokenHash h){return repo.findByTokenHash(h.value()).map(mapper::toDomain);}
    @Transactional public void revoke(TokenHash h){repo.findByTokenHash(h.value()).ifPresent(e->{e.setRevoked(true); repo.save(e);});}
    @Transactional public void revokeAllByUserId(UserId id){repo.findAllByUserId(id.value()).forEach(e->{e.setRevoked(true); repo.save(e);});}
}
