package co.com.crediya.security.util;

import co.com.crediya.security.enums.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeysUtil {

    private final Resource resourcePublicKey;

    public RSAPublicKey loadPublicKey() {
        try {
            String key = new String( resourcePublicKey.getInputStream().readAllBytes() )
                    .replaceAll(SecurityConstants.REGEX_START_END_KEY.getValue(), "")
                    .replaceAll(SecurityConstants.REGEX_SPACES.getValue(), "");

            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return (RSAPublicKey) KeyFactory.getInstance(SecurityConstants.TYPE_ALGORITHM.getValue()).generatePublic(spec);
        }catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }


    }


}
