package co.com.crediya.model.gateways;

import co.com.crediya.model.Loan;
import reactor.core.publisher.Mono;

public interface LoanRepositoryPort {

    Mono<Loan> save(Loan loan);

}
