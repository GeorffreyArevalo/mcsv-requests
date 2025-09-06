package co.com.crediya.security.util;

import co.com.crediya.exceptions.CrediyaInternalServerErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class KeysUtilTest {

    private String validPublicKey;
    private String invalidPublicKey;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        validPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
                publicKeyBase64 +
                "\n-----END PUBLIC KEY-----";

        invalidPublicKey = "-----BEGIN PUBLIC KEY-----\nINVALID_KEY\n-----END PUBLIC KEY-----";
    }

    @Test
    @DisplayName("must load successfully RSAPublicKey")
    void loadPublicKeyShouldReturnValidKey() {
        KeysUtil keysUtil = new KeysUtil(validPublicKey);

        StepVerifier.create(keysUtil.loadPublicKey())
                .assertNext(publicKey -> {
                    assertNotNull(publicKey);
                    assertEquals("RSA", publicKey.getAlgorithm());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("must throw exception if RSAPublicKey if invalid")
    void loadPublicKeyShouldThrowExceptionWhenInvalid() {
        KeysUtil keysUtil = new KeysUtil(invalidPublicKey);

        StepVerifier.create(keysUtil.loadPublicKey())
                .expectErrorMatches(CrediyaInternalServerErrorException.class::isInstance)
                .verify();
    }
}
