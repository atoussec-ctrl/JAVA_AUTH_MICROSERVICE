package com.atous.auth.infrastructure.time;

import com.atous.auth.application.port.out.ClockProviderPort; import org.springframework.stereotype.Component; import java.time.Instant;

@Component public class SystemClockProviderAdapter implements ClockProviderPort { public Instant now(){return Instant.now();} }
