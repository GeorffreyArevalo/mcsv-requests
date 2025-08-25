package co.com.crediya.api.rest.loan;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.loan.CreateLoanRequest;
import co.com.crediya.api.dtos.loan.LoanResponse;
import co.com.crediya.model.Loan;
import co.com.crediya.model.TypeLoan;
import co.com.crediya.usecase.loan.LoanUseCase;
import co.com.crediya.usecase.typeloan.TypeLoanUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {LoanRouterRest.class, LoanHandler.class})
@EnableConfigurationProperties(PathsConfig.class)
@WebFluxTest
class LoanRouterRestTest {


    private static final String LOANS_PATH = "/api/v1/loans";
    private static final String LOANS_PATH_ID = "/api/v1/loans/{id}";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoanUseCase loanUseCase;

    @MockitoBean
    private TypeLoanUseCase typeLoanUseCase;

    private CreateLoanRequest createLoanRequest;
    private Loan loan;

    private TypeLoan typeLoan;

    @Autowired
    private PathsConfig pathsConfig;

    @BeforeEach
    void setUp() {


        typeLoan = TypeLoan.builder()
                .id(1L)
                .name("Préstamo de Libre Inversión")
                .code("LIBRE_INVERSION")
                .autoValidation(true)
                .interestRate(new BigDecimal("0.0500"))
                .maxAmount(new BigDecimal("50000.00"))
                .minAmount(new BigDecimal("2000.00"))
                .build();

        createLoanRequest = new CreateLoanRequest(
                new BigDecimal("10.0"),
                LocalDate.now(),
                "geoeffrey@arevalo.com",
                "100688719923243",
                "LIBRE_INVERSION"
        );

        loan = Loan.builder()
                .idLoanState(1L)
                .idTypeLoan(1L)
                .amount(new BigDecimal("10.0"))
                .deadline(LocalDate.now())
                .userDocument("100688719923243")
                .notificationEmail("geoeffrey@arevalo.com")
                .build();


    }


    @Test
    void shouldLoadUserPathPathProperties() {
        assertEquals(LOANS_PATH, pathsConfig.getLoans());
        assertEquals(LOANS_PATH_ID, pathsConfig.getLoansById());
    }


    @Test
    @DisplayName("Must save a loan successfully")
    void mustSaveLoanSuccessfully() {

        when( loanUseCase.saveLoan(loan) ).thenReturn(Mono.just(loan));
        when( typeLoanUseCase.findByCode(typeLoan.getCode()) ).thenReturn(Mono.just(typeLoan));

        webTestClient.post()
                .uri(LOANS_PATH)
                .bodyValue(createLoanRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LoanResponse.class)
                .value(loanResponse -> {
                            Assertions.assertThat(loanResponse).isNotNull();
                        }
                );
    }
}
