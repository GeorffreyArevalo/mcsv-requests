package co.com.crediya.r2dbc.persistence.typeloan;

import co.com.crediya.r2dbc.entities.TypeLoanEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TypeLoanRepository extends ReactiveCrudRepository<TypeLoanEntity, Long>, ReactiveQueryByExampleExecutor<TypeLoanEntity> {

    Mono<TypeLoanEntity> findByCode(String code);

}
