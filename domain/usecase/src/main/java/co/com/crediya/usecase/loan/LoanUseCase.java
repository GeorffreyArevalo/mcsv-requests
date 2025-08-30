package co.com.crediya.usecase.loan;

import co.com.crediya.enums.LoanStateCodes;
import co.com.crediya.model.Loan;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.model.gateways.LoanStateRepositoryPort;
import co.com.crediya.port.consumers.UserServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoanUseCase {

    private final LoanRepositoryPort loanRepositoryPort;
    private final LoanStateRepositoryPort loanStateRepositoryPort;
    private final UserServicePort userServicePort;


    public Mono<Loan> saveLoan(Loan loan) {

        return userServicePort.getUserByDocument(loan.getUserDocument())
                .then(loanStateRepositoryPort.findByCode(LoanStateCodes.PENDING_REVIEW.getStatus())
                .map( loanStatus ->  {
                    loan.setIdLoanState(loanStatus.getId());
                    return loan;
                }))
                .flatMap(loanRepositoryPort::saveLoan);
    }
}
