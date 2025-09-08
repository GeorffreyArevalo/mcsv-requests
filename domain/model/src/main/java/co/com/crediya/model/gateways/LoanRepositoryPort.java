package co.com.crediya.model.gateways;

import co.com.crediya.model.Loan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanRepositoryPort {

    Mono<Loan> saveLoan(Loan loan);

    Flux<Loan> findLoans(int size, int page, Long idState);

    Mono<Loan> findById(Long id);

    Mono<Long>  count();

    Flux<Loan> findLoansByUserDocumentAndState( String userDocument, String state );

}
