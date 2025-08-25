package co.com.crediya.usecase.loan;

import co.com.crediya.enums.LoanStateCodes;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.Loan;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.model.gateways.LoanStatusRepositoryPort;
import co.com.crediya.port.consumers.UserConsumerPort;
import co.com.crediya.ports.CrediyaLoggerPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoanUseCase implements LoanServicePort {

    private final LoanRepositoryPort loanRepositoryPort;
    private final LoanStatusRepositoryPort loanStatusRepositoryPort;
    private final UserConsumerPort userConsumerPort;
    private final CrediyaLoggerPort logger;

    @Override
    public Mono<Loan> saveLoan(Loan loan) {

        return LoanValidator.validateCreateLoan(loan)
                .flatMap( validLoan -> userConsumerPort.getUserByDocument(validLoan.getUserDocument())
                .thenReturn(validLoan)
                .doOnError(e -> logger.warn("User with document {} does not exist", validLoan.getUserDocument()))
                .onErrorMap( e ->  new CrediyaResourceNotFoundException(
                            String.format( ExceptionMessages.USER_WITH_DOCUMENT_NOT_FOUND.getMessage(), loan.getUserDocument() ))
                ))
                .flatMap( currentLoan ->  loanStatusRepositoryPort.findByCode(LoanStateCodes.PENDING_REVIEW.getStatus())
                .map( loanStatus ->  {
                    currentLoan.setIdLoanState(loanStatus.getId());
                    return currentLoan;
                }))
                .flatMap(loanRepositoryPort::save)
                .doOnSuccess( savedLoan -> logger.info("Loan={} saved successfully.", savedLoan ));

    }
}
