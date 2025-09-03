package co.com.crediya.port.token;

import co.com.crediya.model.Token;
import reactor.core.publisher.Mono;

public interface JwtAuthenticationPort {

    Mono<Token> validateToken(String token);

}
