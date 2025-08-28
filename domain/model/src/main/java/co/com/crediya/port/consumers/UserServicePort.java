package co.com.crediya.port.consumers;

import co.com.crediya.port.consumers.model.User;
import reactor.core.publisher.Mono;

public interface UserServicePort {

    Mono<User> getUserByDocument(String document );

}
