package com.atous.auth.application.port.out;

import java.util.List;

public interface TotpPort {
    String generateSecret();

    String provisioningUri(String issuer, String accountEmail, String secret);

    boolean verifyCode(String secret, String code);

    List<String> generateRecoveryCodes(int count);
}
