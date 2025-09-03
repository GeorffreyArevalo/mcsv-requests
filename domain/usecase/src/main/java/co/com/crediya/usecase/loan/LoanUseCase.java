package co.com.crediya.usecase.loan;

import co.com.crediya.enums.LoanStateCodes;
import co.com.crediya.exceptions.CrediyaForbiddenException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Loan;
import co.com.crediya.model.Pageable;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.model.gateways.LoanStateRepositoryPort;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.token.SecurityAuthenticationPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoanUseCase {

    private final LoanRepositoryPort loanRepositoryPort;
    private final LoanStateRepositoryPort loanStateRepositoryPort;
    private final UserServicePort userServicePort;
    private final SecurityAuthenticationPort securityAuthenticationPort;


    public Mono<Loan> saveLoan(Loan loan) {
        return userServicePort.getUserByDocument(loan.getUserDocument())
            .flatMap( user ->
                securityAuthenticationPort.getCurrentContextToken()
                    .filter( token -> token.getSubject().equals(user.getEmail()) )
            )
            .switchIfEmpty( Mono.error(new CrediyaForbiddenException(ExceptionMessages.CREATE_LOAN_FORBIDDEN.getMessage())) )
            .then(loanStateRepositoryPort.findByCode(LoanStateCodes.PENDING_REVIEW.getStatus())
            .map( loanStatus ->  {
                loan.setIdLoanState(loanStatus.getId());
                return loan;
            }))
            .flatMap(loanRepositoryPort::saveLoan);
    }

    public Mono<Pageable<Loan>> findPageLoans(int size, int page ) {

        return loanRepositoryPort.findLoans(size, (page * size))
                .collectList()
                .zipWith( loanRepositoryPort.count(), ( loans, count ) ->
                    Pageable
                        .<Loan>builder()
                        .page(page)
                        .total(count)
                        .size(size)
                        .content(loans)
                        .build()
                );
    }
}
