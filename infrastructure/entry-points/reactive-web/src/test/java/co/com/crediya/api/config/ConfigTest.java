package co.com.crediya.api.config;

import co.com.crediya.api.dtos.loan.CreateLoanRequestDTO;
import co.com.crediya.api.dtos.loan.LoanResponseDTO;
import co.com.crediya.api.mappers.LoanMapper;
import co.com.crediya.api.rest.loan.LoanHandler;
import co.com.crediya.api.rest.loan.LoanRouterRest;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.model.Loan;
import co.com.crediya.model.Token;
import co.com.crediya.model.TypeLoan;
import co.com.crediya.usecase.auth.AuthUseCase;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        LoanRouterRest.class,
        LoanHandler.class,
        PathsConfig.class,
        ValidatorUtil.class
})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoanUseCase loanUseCase;

    @MockitoBean
    private AuthUseCase authUseCase;

    @MockitoBean
    private LoanMapper loanMapper;

    @MockitoBean
    private TypeLoanUseCase typeLoanUseCase;


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
            1L,
            "APROVADA",
            "MIN",
            BigDecimal.TEN,
            "Juan",
            new BigDecimal("0.05")
    );

    private final Loan loan = Loan.builder()
            .idLoanState(1L)
                .idTypeLoan(1L)
                .amount(new BigDecimal("10.0"))
                .deadline(LocalDate.now())
                .userDocument("100688719923243")
                .notificationEmail("geoeffrey@arevalo.com")
                .build();

    private final Token token = Token.builder()
            .accessToken("21313")
            .subject("julian@gmail.com")
            .role("ADMIN")
            .permissions(List.of())
            .build();

    private final String bearerToken = "Bearer valid-token";

    @BeforeEach
    void setUp() {
        when( loanUseCase.saveLoan(loan) ).thenReturn(Mono.just(loan));
        when( typeLoanUseCase.findByCode(typeLoan.getCode()) ).thenReturn(Mono.just(typeLoan));
        when( loanMapper.modelToResponse(any()) ).thenReturn(loanResponse);
        when( loanMapper.createRequestToModel(any(), any()) ).thenReturn(loan);
        when( authUseCase.authorize(bearerToken, "POST", "/api/v1/loans") ).thenReturn( Mono.just(token) );

    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.post()
                .uri("/api/v1/loans")
                .header("Authorization", bearerToken)
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