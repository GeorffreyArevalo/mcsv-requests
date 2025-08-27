package co.com.crediya.usecase.typeloan;

import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.TypeLoan;
import co.com.crediya.model.gateways.TypeLoanRepositoryPort;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestTypeLoanUseCase {

    private TypeLoan typeLoan;

    @Mock
    private TypeLoanRepositoryPort typeLoanRepositoryPort;

    @InjectMocks
    private TypeLoanUseCase typeLoanUseCase;



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
    }

    @Test
    @DisplayName("Must find type loan by code.")
    void mustFindTypeLoanByCode() {

        when( typeLoanRepositoryPort.findByCode( typeLoan.getCode() ) ).thenReturn(Mono.just(typeLoan) );

        StepVerifier.create( typeLoanUseCase.findByCode( typeLoan.getCode() ) )
                .expectNextMatches(responseTypeLoan -> responseTypeLoan.getCode().equals( typeLoan.getCode() ) )
                .verifyComplete();

    }

    @Test
    @DisplayName("Must return error if code doesn't exist.")
    void mustReturnErrorIfCodeDoesNotExist() {

        when( typeLoanRepositoryPort.findByCode( typeLoan.getCode() ) ).thenReturn(Mono.empty() );

        StepVerifier.create( typeLoanUseCase.findByCode( typeLoan.getCode() ) )
                .expectError(CrediyaResourceNotFoundException.class)
                .verify();

    }


}
