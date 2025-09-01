package co.com.crediya.security.config;

import co.com.crediya.security.util.KeysUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeysUtil keysUtil;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .authorizeExchange( exchanges -> exchanges.anyExchange().authenticated() )
                .oauth2ResourceServer( oauth2 ->
                                oauth2.jwt( jwt -> jwt.publicKey( keysUtil.loadPublicKey() ) )

                ).build();

    }

}
