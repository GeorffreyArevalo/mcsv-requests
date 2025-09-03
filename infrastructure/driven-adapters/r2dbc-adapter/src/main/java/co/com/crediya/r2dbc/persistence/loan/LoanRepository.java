package co.com.crediya.r2dbc.persistence.loan;

import co.com.crediya.r2dbc.entities.LoanEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LoanRepository extends ReactiveCrudRepository<LoanEntity, Long>, ReactiveQueryByExampleExecutor<LoanEntity> {


    Flux<LoanEntity> findAllByIdLoanState( Long id, Pageable pageable );

}
