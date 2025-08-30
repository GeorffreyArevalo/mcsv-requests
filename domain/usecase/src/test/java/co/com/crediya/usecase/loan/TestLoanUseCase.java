package co.com.crediya.usecase.loan;

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
        when( loanRepositoryPort.saveLoan(loan) ).thenReturn(Mono.just(loan));

        StepVerifier.create( loanUseCase.saveLoan(loan) )
                .expectNextMatches(responseLoan -> responseLoan.getUserDocument().equals(loan.getUserDocument()))
                .verifyComplete();

    }




}
