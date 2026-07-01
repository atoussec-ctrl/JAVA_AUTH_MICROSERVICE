package com.atous.auth.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import com.atous.auth.domain.exception.InvalidEmailException;

class EmailTest {
    @Test
    void shouldNormalizeEmail() {
        assertThat(Email.of(" test@gmail.COM ").value().toLowerCase()).isEqualTo("test@gmail.com");
    }

    @Test
    void shouldRejectInvalidEmail() {
        assertThatThrownBy(() -> Email.of("invalid")).isInstanceOf(InvalidEmailException.class);
    }
}
