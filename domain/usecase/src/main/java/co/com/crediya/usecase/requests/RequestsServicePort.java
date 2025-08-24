package co.com.crediya.usecase.requests;

import co.com.crediya.model.Requests;
import reactor.core.publisher.Mono;

public interface RequestsServicePort {

    Mono<Requests> saveRequest(Requests requests);

}
