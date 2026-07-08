package com.atous.auth.infrastructure.email;

import com.atous.auth.application.port.out.EmailSenderPort;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Component
public class NoopEmailSenderAdapter implements EmailSenderPort {
    private static final Logger log = LoggerFactory.getLogger(NoopEmailSenderAdapter.class);

    public void sendPasswordResetEmail(String to, String token) {
        log.info("Password reset token generated for {}: {}", to, token);
    }

    public void sendWelcomeEmail(String to, String name) {
        log.info("Welcome email skipped for {}", to);
    }
}
