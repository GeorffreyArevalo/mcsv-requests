package co.com.crediya.security.util;

import co.com.crediya.exceptions.CrediyaInternalServerErrorException;
import co.com.crediya.security.enums.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

class KeysUtilTest {

    private Resource validResource;
    private Resource invalidResource;

    @BeforeEach
    void setUp() throws Exception {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        byte[] encoded = keyPair.getPublic().getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(encoded);

        String pem = "-----BEGIN PUBLIC KEY-----\n"
                + base64Key
                + "\n-----END PUBLIC KEY-----";

        validResource = new ByteArrayResource(pem.getBytes());

        String invalidPem = "-----BEGIN PUBLIC KEY-----\nINVALID_KEY\n-----END PUBLIC KEY-----";
        invalidResource = new ByteArrayResource(invalidPem.getBytes());
    }

    @Test
    void loadPublicKeyShouldReturnValidKey() {
        KeysUtil keysUtil = new KeysUtil(validResource);

        StepVerifier.create(keysUtil.loadPublicKey())
                .expectNextMatches(key -> key.getAlgorithm().equals(SecurityConstants.TYPE_ALGORITHM.getValue()))
                .verifyComplete();
    }

    @Test
    void loadPublicKey_shouldThrowExceptionWhenInvalidKey() {
        KeysUtil keysUtil = new KeysUtil(invalidResource);

        StepVerifier.create(keysUtil.loadPublicKey())
                .expectErrorMatches(CrediyaInternalServerErrorException.class::isInstance)
                .verify();
    }
}
