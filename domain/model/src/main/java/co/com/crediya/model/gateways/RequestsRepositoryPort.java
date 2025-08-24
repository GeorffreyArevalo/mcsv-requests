package co.com.crediya.model.gateways;

import co.com.crediya.model.Requests;
import reactor.core.publisher.Mono;

public interface RequestsRepositoryPort {

    Mono<Requests> save(Requests requests);

}
