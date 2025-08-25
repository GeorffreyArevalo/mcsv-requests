package co.com.crediya.usecase.typeloan;

import co.com.crediya.model.TypeLoan;
import reactor.core.publisher.Mono;

public interface TypeLoanServicePort {

    Mono<TypeLoan> findByCode(String code);

}
