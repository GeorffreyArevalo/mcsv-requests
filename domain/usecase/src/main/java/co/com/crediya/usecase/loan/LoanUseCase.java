package co.com.crediya.usecase.loan;

import co.com.crediya.enums.LoanStateCodes;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
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

        return LoanValidator.validateCreateLoan(loan)
                .flatMap( validLoan -> userServicePort.getUserByDocument(validLoan.getUserDocument())
                .thenReturn(validLoan)
                .onErrorMap( e ->  new CrediyaResourceNotFoundException(
                            String.format( ExceptionMessages.USER_WITH_DOCUMENT_NOT_FOUND.getMessage(), loan.getUserDocument() ))
                ))
                .flatMap( currentLoan ->  loanStateRepositoryPort.findByCode(LoanStateCodes.PENDING_REVIEW.getStatus())
                .map( loanStatus ->  {
                    currentLoan.setIdLoanState(loanStatus.getId());
                    return currentLoan;
                }))
                .flatMap(loanRepositoryPort::saveLoan);

    }
}
