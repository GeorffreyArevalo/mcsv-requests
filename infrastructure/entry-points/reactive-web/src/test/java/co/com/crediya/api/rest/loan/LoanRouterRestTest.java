package co.com.crediya.api.rest.loan;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.loan.CreateLoanRequest;
import co.com.crediya.api.dtos.loan.LoanResponse;
import co.com.crediya.api.mappers.LoanMapper;
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
import static org.mockito.ArgumentMatchers.any;
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

    @MockitoBean
    private LoanMapper loanMapper;

    private CreateLoanRequest createLoanRequest;
    private CreateLoanRequest createBadLoanRequest;
    private LoanResponse loanResponse;
    private Loan loan;
    private Loan badLoan;

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

        createBadLoanRequest = new CreateLoanRequest(
                new BigDecimal("10.0"),
                LocalDate.now(),
                "geoeffrey@arevalo.com",
                "",
                "LIBRE_INVERSION"
        );

        loanResponse = new LoanResponse(
                new BigDecimal("10.0"),
                LocalDate.now(),
                "geoeffrey@arevalo.com",
                "100688719923243",
                1L,
                1L
        );

        loan = Loan.builder()
                .idLoanState(1L)
                .idTypeLoan(1L)
                .amount(new BigDecimal("10.0"))
                .deadline(LocalDate.now())
                .userDocument("100688719923243")
                .notificationEmail("geoeffrey@arevalo.com")
                .build();

        badLoan = Loan.builder()
                .idLoanState(1L)
                .idTypeLoan(1L)
                .amount(new BigDecimal("10.0"))
                .deadline(LocalDate.now())
                .userDocument("")
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

        when( loanMapper.modelToResponse( any(Loan.class) ) ).thenReturn( loanResponse );
        when( loanMapper.createRequestToModel( any( CreateLoanRequest.class ), any() ) ).thenReturn( loan );

        webTestClient.post()
                .uri(LOANS_PATH)
                .bodyValue(createLoanRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.statusCode").isEqualTo(201)
                .jsonPath("$.data.userDocument").isEqualTo(loan.getUserDocument())
                .jsonPath("$.data.notificationEmail")
                .value( email -> {
                    Assertions.assertThat(email).isEqualTo(loan.getNotificationEmail());
                } );
    }


    @Test
    @DisplayName("Must return error when save a loan.")
    void testListenSaveUserWithError() {

        when( loanUseCase.saveLoan(any(Loan.class)) ).thenReturn( Mono.just(loan) );
        when( loanMapper.modelToResponse( any(Loan.class) ) ).thenReturn( loanResponse );
        when( loanMapper.createRequestToModel( any( CreateLoanRequest.class ), any(Long.class) )).thenReturn( badLoan );
        when( typeLoanUseCase.findByCode(typeLoan.getCode()) ).thenReturn(Mono.just(typeLoan));


        webTestClient.post()
                .uri(LOANS_PATH)
                .bodyValue(createBadLoanRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.statusCode").isEqualTo(400)
                .jsonPath("$.error")
                .value( error -> {
                    Assertions.assertThat(error).isNotNull();
                } );
    }
}
