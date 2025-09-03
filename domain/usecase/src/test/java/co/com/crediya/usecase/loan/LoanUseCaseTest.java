package co.com.crediya.usecase.loan;

import co.com.crediya.enums.LoanStateCodes;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.Loan;
import co.com.crediya.model.LoanState;
import co.com.crediya.model.Token;
import co.com.crediya.model.TypeLoan;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.model.gateways.LoanStateRepositoryPort;
import co.com.crediya.model.gateways.TypeLoanRepositoryPort;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.consumers.model.User;
import co.com.crediya.port.token.SecurityAuthenticationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanUseCaseTest {

    private LoanRepositoryPort loanRepositoryPort;
    private LoanStateRepositoryPort loanStateRepositoryPort;
    private TypeLoanRepositoryPort typeLoanRepositoryPort;
    private UserServicePort userServicePort;
    private SecurityAuthenticationPort securityAuthenticationPort;
    private LoanUseCase loanUseCase;

    private Token token;

    @BeforeEach
    void setUp() {
        loanRepositoryPort = mock(LoanRepositoryPort.class);
        loanStateRepositoryPort = mock(LoanStateRepositoryPort.class);
        typeLoanRepositoryPort = mock(TypeLoanRepositoryPort.class);
        userServicePort = mock(UserServicePort.class);
        securityAuthenticationPort = mock(SecurityAuthenticationPort.class);

        loanUseCase = new LoanUseCase(
                loanRepositoryPort,
                loanStateRepositoryPort,
                typeLoanRepositoryPort,
                userServicePort,
                securityAuthenticationPort
        );

        token = Token.builder()
                .accessToken("21313")
                .subject("julian@gmail.com")
                .role("ADMIN")
                .permissions(List.of())
                .build();
    }

    @Test
    void saveLoanShouldSaveSuccessfully() {
        Loan loan = new Loan();
        loan.setUserDocument("123");

        User user = new User();
        user.setEmail("julian@gmail.com");

        LoanState pending = new LoanState();
        pending.setId(1L);

        when(userServicePort.getUserByDocument("123")).thenReturn(Mono.just(user));
        when(securityAuthenticationPort.getCurrentContextToken()).thenReturn(Mono.just((token)));
        when(loanStateRepositoryPort.findByCode(LoanStateCodes.PENDING_REVIEW.getStatus())).thenReturn(Mono.just(pending));
        when(loanRepositoryPort.saveLoan(any(Loan.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(loanUseCase.saveLoan(loan))
                .expectNextMatches(saved -> saved.getIdLoanState().equals(1L))
                .verifyComplete();
    }

    @Test
    void findPageLoansShouldReturnLoans() {
        Loan loan = new Loan();
        loan.setIdTypeLoan(10L);
        loan.setIdLoanState(20L);
        loan.setUserDocument("123");

        LoanState state = new LoanState();
        state.setId(20L);
        state.setName("PENDING");

        TypeLoan typeLoan = new TypeLoan();
        typeLoan.setId(10L);
        typeLoan.setName("Personal Loan");
        typeLoan.setInterestRate(new BigDecimal("5.5"));

        User user = new User();
        user.setName("John Doe");
        user.setBasePayment(new BigDecimal("1000.0"));

        when(loanStateRepositoryPort.findByCode("PENDING")).thenReturn(Mono.just(state));
        when(loanRepositoryPort.findLoans(10, 0, 20L)).thenReturn(Flux.just(loan));
        when(typeLoanRepositoryPort.findById(10L)).thenReturn(Mono.just(typeLoan));
        when(loanStateRepositoryPort.findById(20L)).thenReturn(Mono.just(state));
        when(userServicePort.getUserByDocument("123")).thenReturn(Mono.just(user));

        StepVerifier.create(loanUseCase.findPageLoans(10, 0, "PENDING"))
                .expectNextMatches(result ->
                        result.getState().equals("PENDING") &&
                                result.getType().equals("Personal Loan") &&
                                result.getNameClient().equals("John Doe")
                )
                .verifyComplete();
    }

    @Test
    void findPageLoansShouldThrowResourceNotFoundWhenStateMissing() {
        when(loanStateRepositoryPort.findByCode("UNKNOWN")).thenReturn(Mono.empty());

        StepVerifier.create(loanUseCase.findPageLoans(10, 0, "UNKNOWN"))
                .expectErrorMatches(ex -> ex instanceof CrediyaResourceNotFoundException &&
                        ex.getMessage().contains("UNKNOWN"))
                .verify();
    }

    @Test
    void countLoansShouldReturnCount() {
        when(loanRepositoryPort.count()).thenReturn(Mono.just(5L));

        StepVerifier.create(loanUseCase.countLoans())
                .expectNext(5L)
                .verifyComplete();
    }
}



