package co.com.crediya.r2dbc.persistence.loanstate;

import co.com.crediya.r2dbc.entities.LoanStateEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoanStateRepository extends ReactiveCrudRepository<LoanStateEntity, Long>, ReactiveQueryByExampleExecutor<LoanStateEntity> {

    Mono<LoanStateEntity> findByCode(String code);

}
