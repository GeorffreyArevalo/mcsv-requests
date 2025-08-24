package co.com.crediya.usecase.loan;

import co.com.crediya.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.Loan;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.port.consumers.UserConsumerPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoanUseCase implements LoanServicePort {

    private final LoanRepositoryPort loanRepositoryPort;
    private final UserConsumerPort userConsumerPort;



    @Override
    public Mono<Loan> saveLoan(Loan loan) {

        return LoanValidator.validateCreateLoan(loan)
                .flatMap( validLoan -> {
                    return userConsumerPort.getUserByDocument(validLoan.getUserDocument())
                            .thenReturn(validLoan)
                            .onErrorMap( (e) -> new CrediyaResourceNotFoundException(ExceptionMessages.USER_WITH_DOCUMENT_NOT_FOUND.getMessage()));
                })
                .flatMap(loanRepositoryPort::save)
                .doOnSuccess( savedLoan -> {} );

    }
}
