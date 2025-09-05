package co.com.crediya.security.config;


import co.com.crediya.security.util.KeysUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.public-key-location}")
    private String resourcePublicKey;

    @Bean
    public KeysUtil keysUtil() {
        return new KeysUtil(resourcePublicKey);
    }


}
