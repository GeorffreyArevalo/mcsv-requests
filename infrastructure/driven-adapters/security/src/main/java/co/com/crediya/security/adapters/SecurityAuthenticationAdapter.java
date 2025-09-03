package co.com.crediya.security.adapters;

import co.com.crediya.model.Token;
import co.com.crediya.port.token.SecurityAuthenticationPort;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SecurityAuthenticationAdapter implements SecurityAuthenticationPort {

    public Mono<Token> getCurrentContextToken() {
        return Mono.deferContextual( ctx -> Mono.just(ctx.get("token")));
    }


}
