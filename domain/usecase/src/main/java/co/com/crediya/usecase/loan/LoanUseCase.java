package co.com.crediya.usecase.loan;

import co.com.crediya.enums.LoanStateCodes;
import co.com.crediya.exceptions.CrediyaForbiddenException;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Loan;
import co.com.crediya.model.LoanState;
import co.com.crediya.model.TypeLoan;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.model.gateways.LoanStateRepositoryPort;
import co.com.crediya.model.gateways.TypeLoanRepositoryPort;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.consumers.model.User;
import co.com.crediya.port.token.SecurityAuthenticationPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoanUseCase {

    private final LoanRepositoryPort loanRepositoryPort;
    private final LoanStateRepositoryPort loanStateRepositoryPort;
    private final TypeLoanRepositoryPort typeLoanRepositoryPort;
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

    public Flux<Loan> findPageLoans(int size, int page, String codeState ) {

        return loanStateRepositoryPort.findByCode(codeState)
            .switchIfEmpty( Mono.error( new CrediyaResourceNotFoundException(
                String.format(ExceptionMessages.STATE_LOAN_WITH_CODE_NOT_FOUND.getMessage(), codeState)
            )) )
            .flatMapMany( loanState -> loanRepositoryPort.findLoans(size, page, loanState.getId())

            ).flatMap( loan ->  {
                Mono<TypeLoan> monoTypeLoan = typeLoanRepositoryPort.findById(loan.getIdTypeLoan());
                Mono<LoanState> monoStateLoan = loanStateRepositoryPort.findById(loan.getIdLoanState());
                Mono<User> monoUser = userServicePort.getUserByDocument(loan.getUserDocument());

                return Mono.zip(monoTypeLoan, monoStateLoan, (type, state) -> {
                    loan.setState(state.getName());
                    loan.setType(type.getName());
                    loan.setInterestRate(type.getInterestRate());
                    return loan;
                }).zipWith( monoUser, ( fullLoan, user ) -> {
                    fullLoan.setBasePayment(user.getBasePayment());
                    fullLoan.setNameClient(user.getName());
                    return fullLoan;
                });
            });
    }

    public Mono<Long> countLoans() {
        return loanRepositoryPort.count();
    }

}
