package co.com.crediya.usecase.loan;

import co.com.crediya.model.Loan;
import reactor.core.publisher.Mono;

public interface LoanServicePort {

    Mono<Loan> saveLoan(Loan loan);

}
