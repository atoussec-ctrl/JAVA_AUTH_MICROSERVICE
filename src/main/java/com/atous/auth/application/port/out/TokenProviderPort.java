package com.atous.auth.application.port.out;

import com.atous.auth.application.dto.result.*; import com.atous.auth.domain.model.User; public interface TokenProviderPort { AccessTokenResult generateAccessToken(User user); TokenClaimsDto validate(String accessToken); }
