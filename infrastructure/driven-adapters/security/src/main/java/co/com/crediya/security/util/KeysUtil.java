package co.com.crediya.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class KeysUtil {

    @Value("${security.jwt.public-key-location}")
    private Resource resourcePublicKey;

    public RSAPublicKey loadPublicKey() {
        try {
            String key = new String( resourcePublicKey.getInputStream().readAllBytes() )
                    .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        }catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }


    }


}
