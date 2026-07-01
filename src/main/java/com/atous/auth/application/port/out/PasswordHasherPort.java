package com.atous.auth.application.port.out;

import com.atous.auth.domain.valueobject.PasswordHash; public interface PasswordHasherPort { PasswordHash hash(String rawPassword); boolean matches(String rawPassword, PasswordHash passwordHash); }
