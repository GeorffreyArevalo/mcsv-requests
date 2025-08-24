package co.com.crediya.port.consumers;

import co.com.crediya.port.consumers.model.UserConsumer;
import reactor.core.publisher.Mono;

public interface UserConsumerPort {

    Mono<UserConsumer> findByDocument( String document );

}
