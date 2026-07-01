package com.atous.auth.application.port.out;

import com.atous.auth.domain.valueobject.TokenHash; public interface TokenHashingPort { TokenHash hash(String rawToken); }
