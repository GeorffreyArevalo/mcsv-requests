package co.com.crediya.r2dbc.persistence.requests;

import co.com.crediya.model.Requests;
import co.com.crediya.model.gateways.RequestsRepositoryPort;
import co.com.crediya.r2dbc.entities.RequestsEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RequestsRepositoryAdapter extends ReactiveAdapterOperations<
        Requests,
        RequestsEntity,
        Long,
        RequestsRepository
> implements RequestsRepositoryPort {
    public RequestsRepositoryAdapter(RequestsRepository repository, ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, Requests.class));
    }

    @Override
    public Mono<Requests> save(Requests requests) {
        return super.save(requests);
    }



}
