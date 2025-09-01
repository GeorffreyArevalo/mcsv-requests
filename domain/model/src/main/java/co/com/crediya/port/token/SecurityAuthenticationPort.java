package co.com.crediya.port.token;

import reactor.core.publisher.Mono;

public interface SecurityAuthenticationPort {

    Mono<String> getCurrentContextToken();
    Mono<String> getTokenSubject();

}
