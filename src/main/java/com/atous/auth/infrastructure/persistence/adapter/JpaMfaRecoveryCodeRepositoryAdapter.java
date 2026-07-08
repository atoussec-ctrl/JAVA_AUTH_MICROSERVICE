package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.MfaRecoveryCodeRepositoryPort;
import com.atous.auth.domain.model.MfaRecoveryCode;
import com.atous.auth.domain.valueobject.TokenHash;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.mapper.MfaRecoveryCodePersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataMfaRecoveryCodeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaMfaRecoveryCodeRepositoryAdapter implements MfaRecoveryCodeRepositoryPort {
    private final SpringDataMfaRecoveryCodeRepository repo;
    private final MfaRecoveryCodePersistenceMapper mapper;

    public JpaMfaRecoveryCodeRepositoryAdapter(SpringDataMfaRecoveryCodeRepository repo,
            MfaRecoveryCodePersistenceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<MfaRecoveryCode> saveAll(List<MfaRecoveryCode> codes) {
        return repo.saveAll(codes.stream().map(mapper::toEntity).collect(Collectors.toList())).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    public Optional<MfaRecoveryCode> findActiveByUserIdAndHash(UserId userId, TokenHash hash) {
        return repo.findByUserIdAndCodeHashAndUsedFalse(userId.value(), hash.value()).map(mapper::toDomain);
    }

    public void markUsed(UUID id) {
        repo.findById(id).ifPresent(e -> {
            e.setUsed(true);
            repo.save(e);
        });
    }

    public void deleteAllByUserId(UserId userId) {
        repo.deleteAllByUserId(userId.value());
    }
}
