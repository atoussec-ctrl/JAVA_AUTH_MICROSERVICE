package com.atous.auth.infrastructure.mfa;

import com.atous.auth.application.port.out.ClockProviderPort;
import com.atous.auth.application.port.out.TotpPort;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/** RFC 4226 (HOTP) / RFC 6238 (TOTP) implementation compatible with standard authenticator apps. */
@Component
public class TotpAdapter implements TotpPort {
    private static final String ALGORITHM = "HmacSHA1";
    private static final int DIGITS = 6;
    private static final int TIME_STEP_SECONDS = 30;
    private static final int WINDOW = 1;
    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    private final SecureRandom random = new SecureRandom();
    private final ClockProviderPort clock;

    public TotpAdapter(ClockProviderPort clock) {
        this.clock = clock;
    }

    @Override
    public String generateSecret() {
        var bytes = new byte[20];
        random.nextBytes(bytes);
        return base32Encode(bytes);
    }

    @Override
    public String provisioningUri(String issuer, String accountEmail, String secret) {
        var label = URLEncoder.encode(issuer + ":" + accountEmail, StandardCharsets.UTF_8);
        var issuerParam = URLEncoder.encode(issuer, StandardCharsets.UTF_8);
        return "otpauth://totp/" + label + "?secret=" + secret + "&issuer=" + issuerParam + "&algorithm=SHA1"
                + "&digits=" + DIGITS + "&period=" + TIME_STEP_SECONDS;
    }

    @Override
    public boolean verifyCode(String secret, String code) {
        if (code == null || !code.matches("\\d{6}") || secret == null)
            return false;
        var key = base32Decode(secret);
        var counter = clock.now().getEpochSecond() / TIME_STEP_SECONDS;
        for (int i = -WINDOW; i <= WINDOW; i++) {
            if (hotp(key, counter + i).equals(code))
                return true;
        }
        return false;
    }

    @Override
    public List<String> generateRecoveryCodes(int count) {
        var codes = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            var bytes = new byte[5];
            random.nextBytes(bytes);
            codes.add(base32Encode(bytes));
        }
        return codes;
    }

    private String hotp(byte[] key, long counter) {
        try {
            var counterBytes = ByteBuffer.allocate(8).putLong(counter).array();
            var mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(key, ALGORITHM));
            var hash = mac.doFinal(counterBytes);
            int offset = hash[hash.length - 1] & 0x0F;
            int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
            int otp = (int) (binary % Math.pow(10, DIGITS));
            return String.format("%0" + DIGITS + "d", otp);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to compute TOTP code", e);
        }
    }

    private String base32Encode(byte[] data) {
        var sb = new StringBuilder();
        int bits = 0, value = 0;
        for (byte b : data) {
            value = (value << 8) | (b & 0xFF);
            bits += 8;
            while (bits >= 5) {
                sb.append(BASE32_ALPHABET.charAt((value >>> (bits - 5)) & 0x1F));
                bits -= 5;
            }
        }
        if (bits > 0)
            sb.append(BASE32_ALPHABET.charAt((value << (5 - bits)) & 0x1F));
        return sb.toString();
    }

    private byte[] base32Decode(String encoded) {
        var clean = encoded.trim().toUpperCase().replace("=", "");
        var out = new ByteArrayOutputStream();
        int bits = 0, value = 0;
        for (char ch : clean.toCharArray()) {
            int idx = BASE32_ALPHABET.indexOf(ch);
            if (idx < 0)
                continue;
            value = (value << 5) | idx;
            bits += 5;
            if (bits >= 8) {
                out.write((value >>> (bits - 8)) & 0xFF);
                bits -= 8;
            }
        }
        return out.toByteArray();
    }
}
