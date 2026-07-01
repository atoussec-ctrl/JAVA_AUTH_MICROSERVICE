package com.atous.auth.application.port.out;

import com.atous.auth.domain.model.MfaDevice; import com.atous.auth.domain.valueobject.UserId; import java.util.Optional; public interface MfaDeviceRepositoryPort { Optional<MfaDevice> findByUserId(UserId id); MfaDevice save(MfaDevice d); }
