package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.MfaDeviceRepositoryPort;
import com.atous.auth.domain.model.MfaDevice;
import com.atous.auth.domain.valueobject.UserId;
import com.atous.auth.infrastructure.persistence.mapper.MfaDevicePersistenceMapper;
import com.atous.auth.infrastructure.persistence.repository.SpringDataMfaDeviceRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class JpaMfaDeviceRepositoryAdapter implements MfaDeviceRepositoryPort {
    private final SpringDataMfaDeviceRepository repo;
    private final MfaDevicePersistenceMapper mapper;

    public JpaMfaDeviceRepositoryAdapter(SpringDataMfaDeviceRepository repo, MfaDevicePersistenceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Optional<MfaDevice> findByUserId(UserId id) {
        return repo.findByUserId(id.value()).map(mapper::toDomain);
    }

    public MfaDevice save(MfaDevice d) {
        return mapper.toDomain(repo.save(mapper.toEntity(d)));
    }
}
