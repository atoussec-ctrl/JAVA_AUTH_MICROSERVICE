package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.PasswordResetTokenRepositoryPort;
import com.atous.auth.domain.model.PasswordResetToken;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.infrastructure.persistence.mapper.PasswordResetTokenPersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataPasswordResetTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public class JpaPasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepositoryPort {
    private final SpringDataPasswordResetTokenRepository repo;
    private final PasswordResetTokenPersistenceMapper mapper;

    public JpaPasswordResetTokenRepositoryAdapter(SpringDataPasswordResetTokenRepository repo,
            PasswordResetTokenPersistenceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public PasswordResetToken save(PasswordResetToken t) {
        return mapper.toDomain(repo.save(mapper.toEntity(t)));
    }

    public Optional<PasswordResetToken> findByTokenHash(TokenHash h) {
        return repo.findByTokenHash(h.value()).map(mapper::toDomain);
    }

    @Transactional
    public void markUsed(TokenHash h) {
        repo.findByTokenHash(h.value()).ifPresent(e -> {
            e.setUsed(true);
            repo.save(e);
        });
    }
}
