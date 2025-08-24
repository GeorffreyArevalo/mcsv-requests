package co.com.crediya.r2dbc.persistence.loan;

import co.com.crediya.model.Loan;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.r2dbc.entities.LoanEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanRepositoryAdapter extends ReactiveAdapterOperations<
        Loan,
        LoanEntity,
        Long,
        LoanRepository
> implements LoanRepositoryPort {
    public LoanRepositoryAdapter(LoanRepository repository, ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, Loan.class));
    }

    @Override
    public Mono<Loan> save(Loan loan) {
        return super.save(loan);
    }



}
