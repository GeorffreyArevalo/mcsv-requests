package co.com.crediya.model.gateways;

import co.com.crediya.model.TypeLoan;
import reactor.core.publisher.Mono;

public interface TypeLoanRepositoryPort {

    Mono<TypeLoan> findByCode(String code);

}
