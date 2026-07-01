package com.atous.auth.domain.service;

import com.atous.auth.domain.exception.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PasswordPolicyServiceTest {
    private final PasswordPolicyService service = new PasswordPolicyService();
    @Test void shouldAcceptStrongPassword(){assertThatCode(() -> service.validate("Strong123", "Strong123")).doesNotThrowAnyException();}
    @Test void shouldRejectMismatch(){assertThatThrownBy(() -> service.validate("Strong123", "Other123")).isInstanceOf(InvalidPasswordException.class);}
}
