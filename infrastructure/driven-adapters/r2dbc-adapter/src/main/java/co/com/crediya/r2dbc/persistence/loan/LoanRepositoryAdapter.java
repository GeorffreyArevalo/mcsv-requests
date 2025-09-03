package co.com.crediya.r2dbc.persistence.loan;

import co.com.crediya.model.Loan;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.r2dbc.entities.LoanEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanRepositoryAdapter extends ReactiveAdapterOperations<
        Loan,
        LoanEntity,
        Long,
        LoanRepository
> implements LoanRepositoryPort {

    TransactionalOperator transactionalOperator;

    public LoanRepositoryAdapter(LoanRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, Loan.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Loan> saveLoan(Loan loan) {
        return transactionalOperator.execute( tr -> super.save(loan)).single();
    }

    @Override
    public Flux<Loan> findLoans(int size, int page, Long idState) {
        return repository.findAllByIdLoanState(idState, PageRequest.of(page, size)).map(super::toEntity);
    }

    @Override
    public Mono<Long> count() {
        return repository.count();
    }

}
