package co.com.crediya.api.config;

import co.com.crediya.api.dtos.loan.CreateLoanRequestDTO;
import co.com.crediya.api.dtos.loan.LoanResponseDTO;
import co.com.crediya.api.mappers.LoanMapper;
import co.com.crediya.api.rest.loan.LoanHandler;
import co.com.crediya.api.rest.loan.LoanRouterRest;
import co.com.crediya.model.Loan;
import co.com.crediya.model.TypeLoan;
import co.com.crediya.ports.TransactionManagementPort;
import co.com.crediya.usecase.loan.LoanUseCase;
import co.com.crediya.usecase.typeloan.TypeLoanUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {LoanRouterRest.class, LoanHandler.class, PathsConfig.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoanUseCase loanUseCase;

    @MockitoBean
    private LoanMapper loanMapper;

    @MockitoBean
    private TypeLoanUseCase typeLoanUseCase;

    @MockitoBean
    private TransactionManagementPort transactionManagementPort;


    private final TypeLoan typeLoan = TypeLoan.builder()
            .id(1L)
                .name("Préstamo de Libre Inversión")
                .code("LIBRE_INVERSION")
                .autoValidation(true)
                .interestRate(new BigDecimal("0.0500"))
                .maxAmount(new BigDecimal("50000.00"))
                .minAmount(new BigDecimal("2000.00"))
                .build();

    private final CreateLoanRequestDTO createLoanRequest = new CreateLoanRequestDTO(
                new BigDecimal("10.0"),
                LocalDate.now(),
                        "geoeffrey@arevalo.com",
                        "100688719923243",
                        "LIBRE_INVERSION"
                        );

    private final LoanResponseDTO loanResponse = new LoanResponseDTO(
            new BigDecimal("10.0"),
            LocalDate.now(),
            "geoeffrey@arevalo.com",
            "100688719923243",
            1L,
            1L
    );

    private final Loan loan = Loan.builder()
            .idLoanState(1L)
                .idTypeLoan(1L)
                .amount(new BigDecimal("10.0"))
                .deadline(LocalDate.now())
                .userDocument("100688719923243")
                .notificationEmail("geoeffrey@arevalo.com")
                .build();

    @BeforeEach
    void setUp() {
        when( loanUseCase.saveLoan(loan) ).thenReturn(Mono.just(loan));
        when( typeLoanUseCase.findByCode(typeLoan.getCode()) ).thenReturn(Mono.just(typeLoan));
        when( loanMapper.modelToResponse(any()) ).thenReturn(loanResponse);
        when( loanMapper.createRequestToModel(any(), any()) ).thenReturn(loan);
        when(transactionManagementPort.inTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.post()
                .uri("/api/v1/loans")
                .bodyValue(createLoanRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}