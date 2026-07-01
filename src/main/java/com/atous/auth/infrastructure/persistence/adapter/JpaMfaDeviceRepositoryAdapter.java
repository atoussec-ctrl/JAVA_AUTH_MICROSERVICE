package com.atous.auth.infrastructure.persistence.adapter;

import com.atous.auth.application.port.out.MfaDeviceRepositoryPort; import com.atous.auth.domain.model.MfaDevice; import com.atous.auth.domain.valueobject.UserId; import org.springframework.stereotype.Repository; import java.util.Optional;

@Repository public class JpaMfaDeviceRepositoryAdapter implements MfaDeviceRepositoryPort { public Optional<MfaDevice> findByUserId(UserId id){return Optional.empty();} public MfaDevice save(MfaDevice d){return d;} }
