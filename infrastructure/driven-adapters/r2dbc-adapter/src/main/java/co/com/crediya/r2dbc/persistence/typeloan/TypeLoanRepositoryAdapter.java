package co.com.crediya.r2dbc.persistence.typeloan;

import co.com.crediya.model.TypeLoan;
import co.com.crediya.model.gateways.TypeLoanRepositoryPort;
import co.com.crediya.r2dbc.entities.TypeLoanEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TypeLoanRepositoryAdapter extends ReactiveAdapterOperations<
        TypeLoan,
        TypeLoanEntity,
        Long,
        TypeLoanRepository
> implements TypeLoanRepositoryPort {
    public TypeLoanRepositoryAdapter(TypeLoanRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, TypeLoan.class));
    }


    @Override
    public Mono<TypeLoan> findByCode(String code) {
        return repository.findByCode(code).map( super::toEntity );
    }
}
