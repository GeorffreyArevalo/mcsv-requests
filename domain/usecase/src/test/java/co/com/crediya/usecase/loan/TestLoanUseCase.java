package co.com.crediya.usecase.loan;

import co.com.crediya.exceptions.CrediyaIllegalArgumentException;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.Loan;
import co.com.crediya.model.LoanState;
import co.com.crediya.model.gateways.LoanRepositoryPort;
import co.com.crediya.model.gateways.LoanStateRepositoryPort;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.consumers.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestLoanUseCase {

    @Mock
    private LoanRepositoryPort loanRepositoryPort;

    @Mock
    private LoanStateRepositoryPort loanStateRepositoryPort;

    @Mock
    private UserServicePort userConsumerPort;

    @InjectMocks
    private LoanUseCase loanUseCase;

    private Loan loan;



    private LoanState loanState;

    private User userConsumer;

    @BeforeEach
    void setUp() {

        loan = Loan.builder()
                .amount(new BigDecimal("10.0"))
                .deadline(LocalDate.of(2025, 11, 10))
                .notificationEmail("julian@gmail.com")
                .userDocument("12333123123")
                .idTypeLoan(1L)
                .idLoanState(1L)
                .build();

        loanState = LoanState.builder()
                .id(1L)
                .code("PENDIENTE_DE_REVISION")
                .description("El equipo de crédito está revisando los documentos y validando la información proporcionada.")
                .name("Pendiente de Revisión")
                .build();



        userConsumer = User.builder()
                .name("Julian")
                .email("julian@gmail.com")
                .basePayment(new BigDecimal("1.0"))
                .document("12333123123")
                .lastName("Arevalo")
                .build();

    }

    @Test
    @DisplayName("Must save a loan successfully")
    void mustSaveLoanSuccessfully() {

        when( userConsumerPort.getUserByDocument( loan.getUserDocument() ) ).thenReturn(Mono.just(userConsumer));
        when( loanStateRepositoryPort.findByCode(loanState.getCode()) ).thenReturn(Mono.just(loanState));
        when( loanRepositoryPort.save(loan) ).thenReturn(Mono.just(loan));

        StepVerifier.create( loanUseCase.saveLoan(loan) )
                .expectNextMatches(responseLoan -> responseLoan.getUserDocument().equals(loan.getUserDocument()))
                .verifyComplete();

    }


    @Test
    @DisplayName("Must return error if amount is not valid.")
    void mustReturnErrorIfAmountIsNotValid() {
        loan.setAmount(new BigDecimal("-1.0"));
        StepVerifier.create( loanUseCase.saveLoan(loan) )
                .expectError( CrediyaIllegalArgumentException.class )
                .verify();

    }

    @Test
    @DisplayName("Must return error if deadline is not valid.")
    void mustReturnErrorIfDeadlineIsNotValid() {
        loan.setDeadline(LocalDate.of(2024, 11, 10));
        StepVerifier.create( loanUseCase.saveLoan(loan) )
                .expectError( CrediyaIllegalArgumentException.class )
                .verify();

    }

    @Test
    @DisplayName("Must return error if user document is not valid.")
    void mustReturnErrorIfUserDocumentIsNotValid() {
        loan.setUserDocument("");
        StepVerifier.create( loanUseCase.saveLoan(loan) )
                .expectError( CrediyaIllegalArgumentException.class )
                .verify();

    }

    @Test
    @DisplayName("Must return error if user not exist.")
    void mustReturnErrorIfUserNotExist() {

        when( userConsumerPort.getUserByDocument(loan.getUserDocument()) ).thenReturn(Mono.error(new CrediyaResourceNotFoundException("")));

        StepVerifier.create( loanUseCase.saveLoan(loan) )
                .expectError( CrediyaResourceNotFoundException.class )
                .verify();

    }




}
