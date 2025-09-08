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
import co.com.crediya.port.queue.SendQueuePort;
import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import co.com.crediya.port.token.SecurityAuthenticationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanUseCaseTest {

    @Mock
    private LoanRepositoryPort loanRepositoryPort;

    @Mock
    private LoanStateRepositoryPort loanStateRepositoryPort;

    @Mock
    private TypeLoanRepositoryPort typeLoanRepositoryPort;

    @Mock
    private UserServicePort userServicePort;

    @Mock
    private SecurityAuthenticationPort securityAuthenticationPort;

    @Mock
    private SendQueuePort sendQueuePort;

    @InjectMocks
    private LoanUseCase loanUseCase;

    private Token token;
    private TypeLoan typeLoan;

    @BeforeEach
    void setUp() {
        token = Token.builder()
                .accessToken("21313")
                .subject("julian@gmail.com")
                .role("ADMIN")
                .build();

        typeLoan = TypeLoan.builder()
                .id(1L)
                .name("Préstamo de Libre Inversión")
                .code("LIBRE_INVERSION")
                .autoValidation(true)
                .interestRate(new BigDecimal("0.0500"))
                .maxAmount(new BigDecimal("50000.00"))
                .minAmount(new BigDecimal("2000.00"))
                .build();
    }

    @Test
    void saveLoanShouldSaveSuccessfully() {
        Loan loan = new Loan();
        loan.setUserDocument("123");
        loan.setIdTypeLoan(1L);
        User user = new User();
        user.setEmail("julian@gmail.com");

        LoanState pending = new LoanState();
        pending.setId(1L);

        when(userServicePort.getUserByDocument("123")).thenReturn(Mono.just(user));
        when(securityAuthenticationPort.getCurrentContextToken()).thenReturn(Mono.just((token)));
        when(loanStateRepositoryPort.findByCode(LoanStateCodes.PENDING_REVIEW.getStatus())).thenReturn(Mono.just(pending));
        when(loanRepositoryPort.saveLoan(any(Loan.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        when(loanRepositoryPort.findLoansByUserDocumentAndState(any(String.class), any(String.class)))
                .thenReturn(Flux.empty());

        when(sendQueuePort.sendCalculateDebtCapacity(any(Loan.class), any(List.class), any(User.class)) ).thenReturn(Mono.empty());
        when(typeLoanRepositoryPort.findById(any(Long.class))  ).thenReturn( Mono.just(typeLoan) );

        StepVerifier.create(loanUseCase.saveLoan(loan))
                .expectNextMatches(saved -> saved.getIdLoanState().equals(1L))
                .verifyComplete();
    }

    @Test
    void updateLoanWithStateLoanShouldUpdateSuccessfully() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setIdLoanState(5L);

        LoanState state = new LoanState();
        state.setId(10L);
        state.setName("APPROVED");

        when(loanRepositoryPort.findById(1L)).thenReturn(Mono.just(loan));
        when(loanStateRepositoryPort.findByCode("APPROVED")).thenReturn(Mono.just(state));
        when(loanRepositoryPort.saveLoan(any(Loan.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(loanUseCase.updateLoanWithStateLoan(1L, "APPROVED"))
                .expectNextMatches(updated -> updated.getIdLoanState().equals(10L))
                .verifyComplete();
    }

    @Test
    void updateLoanWithStateLoanShouldThrowWhenLoanNotFound() {
        when(loanRepositoryPort.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(loanUseCase.updateLoanWithStateLoan(99L, "APPROVED"))
                .expectError(CrediyaResourceNotFoundException.class)
                .verify();
    }

    @Test
    void updateStateLoanShouldSendMessageSuccessfully() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setUserDocument("123");
        loan.setIdLoanState(20L);

        LoanState state = new LoanState();
        state.setId(20L);
        state.setName("APPROVED");

        User user = new User();
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("test@email.com");

        when(loanRepositoryPort.findById(1L)).thenReturn(Mono.just(loan));
        when(loanStateRepositoryPort.findByCode("APPROVED")).thenReturn(Mono.just(state));
        when(loanRepositoryPort.saveLoan(any(Loan.class))).thenReturn(Mono.just(loan));
        when(userServicePort.getUserByDocument("123")).thenReturn(Mono.just(user));
        when(loanStateRepositoryPort.findById(20L)).thenReturn(Mono.just(state));
        when(sendQueuePort.sendNotificationChangeStateLoan(any(MessageNotificationQueue.class))).thenReturn(Mono.empty());

        StepVerifier.create(loanUseCase.updateStateLoan(1L, "APPROVED"))
                .expectNext(loan)
                .verifyComplete();
    }

    @Test
    void findPageLoansShouldReturnLoans() {
        Loan loan = Loan.builder()
                .idTypeLoan(10L)
                .idLoanState(20L)
                .userDocument("123")
                .amount(BigDecimal.valueOf(1000000.0))
                .monthTerm(11)
                .build();

        LoanState state = LoanState.builder()
                .id(20L)
                .name("PENDING")
                .build();

        TypeLoan typeLoan = TypeLoan.builder()
                .id(10L)
                .name("Personal Loan")
                .interestRate(BigDecimal.valueOf(5.5))
                .build();

        User user = User.builder()
                .name("John Doe")
                .basePayment(BigDecimal.valueOf(1000.0))
                .build();

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
