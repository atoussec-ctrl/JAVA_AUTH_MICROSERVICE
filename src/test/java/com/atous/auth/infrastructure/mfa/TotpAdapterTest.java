package com.atous.auth.infrastructure.mfa;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class TotpAdapterTest {
    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final long FIXED_EPOCH_SECOND = 1_700_000_000L;

    private final TotpAdapter adapter = new TotpAdapter(() -> Instant.ofEpochSecond(FIXED_EPOCH_SECOND));

    @Test
    void shouldAcceptCodeComputedIndependentlyForCurrentTimeStep() {
        var secret = adapter.generateSecret();
        var counter = FIXED_EPOCH_SECOND / 30;
        var expectedCode = referenceHotp(base32Decode(secret), counter);

        assertThat(adapter.verifyCode(secret, expectedCode)).isTrue();
    }

    @Test
    void shouldAcceptCodeFromAdjacentTimeStepForClockDriftTolerance() {
        var secret = adapter.generateSecret();
        var counter = FIXED_EPOCH_SECOND / 30;
        var previousStepCode = referenceHotp(base32Decode(secret), counter - 1);

        assertThat(adapter.verifyCode(secret, previousStepCode)).isTrue();
    }

    @Test
    void shouldRejectCodeOutsideToleranceWindow() {
        var secret = adapter.generateSecret();
        var counter = FIXED_EPOCH_SECOND / 30;
        var farFutureCode = referenceHotp(base32Decode(secret), counter + 5);

        assertThat(adapter.verifyCode(secret, farFutureCode)).isFalse();
    }

    @Test
    void shouldRejectMalformedCodes() {
        var secret = adapter.generateSecret();
        assertThat(adapter.verifyCode(secret, "abcdef")).isFalse();
        assertThat(adapter.verifyCode(secret, "12345")).isFalse();
        assertThat(adapter.verifyCode(secret, null)).isFalse();
    }

    @Test
    void shouldGenerateDistinctBase32Secrets() {
        var first = adapter.generateSecret();
        var second = adapter.generateSecret();

        assertThat(first).isNotEqualTo(second);
        assertThat(first).matches("^[A-Z2-7]+$");
    }

    @Test
    void shouldGenerateRequestedNumberOfUniqueRecoveryCodes() {
        var codes = adapter.generateRecoveryCodes(8);

        assertThat(codes).hasSize(8);
        assertThat(new HashSet<>(codes)).hasSize(8);
    }

    @Test
    void provisioningUriShouldContainSecretAndIssuer() {
        var secret = adapter.generateSecret();
        var uri = adapter.provisioningUri("auth-service", "alice@example.com", secret);

        assertThat(uri).startsWith("otpauth://totp/");
        assertThat(uri).contains("secret=" + secret);
        assertThat(uri).contains("issuer=auth-service");
    }

    private String referenceHotp(byte[] key, long counter) {
        try {
            var counterBytes = ByteBuffer.allocate(8).putLong(counter).array();
            var mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            var hash = mac.doFinal(counterBytes);
            int offset = hash[hash.length - 1] & 0x0F;
            int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
            int otp = (int) (binary % 1_000_000);
            return String.format("%06d", otp);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] base32Decode(String encoded) {
        var out = new java.io.ByteArrayOutputStream();
        int bits = 0, value = 0;
        for (char ch : encoded.toCharArray()) {
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
