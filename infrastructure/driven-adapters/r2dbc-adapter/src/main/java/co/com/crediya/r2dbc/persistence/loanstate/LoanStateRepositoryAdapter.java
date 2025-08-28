package co.com.crediya.r2dbc.persistence.loanstate;

import co.com.crediya.model.LoanState;
import co.com.crediya.model.gateways.LoanStateRepositoryPort;
import co.com.crediya.r2dbc.entities.LoanStateEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanStateRepositoryAdapter extends ReactiveAdapterOperations<
        LoanState,
        LoanStateEntity,
        Long,
        LoanStateRepository
> implements LoanStateRepositoryPort {

    public LoanStateRepositoryAdapter(LoanStateRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanState.class));
    }


    @Override
    public Mono<LoanState> findByCode(String code) {
        return repository.findByCode(code).map( super::toEntity );
    }
}
