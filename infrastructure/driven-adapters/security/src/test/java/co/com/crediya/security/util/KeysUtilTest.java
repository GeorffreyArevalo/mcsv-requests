package co.com.crediya.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KeysUtilTest {

    private RSAPublicKey generatedPublicKey;

    @BeforeEach
    void setUp() throws Exception {
        // Generar un par de claves RSA para la prueba
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        generatedPublicKey = (RSAPublicKey) keyPair.getPublic();
    }

    @Test
    void loadPublicKeyWithValidPemShouldReturnRSAPublicKey() {

        String base64Key = Base64.getEncoder().encodeToString(generatedPublicKey.getEncoded());
        String pemKey = "-----BEGIN PUBLIC KEY-----\n" +
                base64Key +
                "\n-----END PUBLIC KEY-----";

        ByteArrayResource resource = new ByteArrayResource(pemKey.getBytes());
        KeysUtil keysUtil = new KeysUtil(resource);

        RSAPublicKey result = keysUtil.loadPublicKey();

        assertThat(result).isNotNull();
        assertThat(result.getAlgorithm()).isEqualTo("RSA");
        assertThat(result.getModulus()).isEqualTo(generatedPublicKey.getModulus());
    }

    @Test
    void loadPublicKeyWithInvalidKeyShouldThrowException() {

        String invalidKey = "-----BEGIN PUBLIC KEY-----\nINVALID_KEY\n-----END PUBLIC KEY-----";
        ByteArrayResource resource = new ByteArrayResource(invalidKey.getBytes());
        KeysUtil keysUtil = new KeysUtil(resource);

        assertThrows(IllegalStateException.class, keysUtil::loadPublicKey);
    }
}
