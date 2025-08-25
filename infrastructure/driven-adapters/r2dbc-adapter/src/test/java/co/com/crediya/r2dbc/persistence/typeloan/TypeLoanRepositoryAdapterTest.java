package co.com.crediya.r2dbc.persistence.typeloan;

import co.com.crediya.model.TypeLoan;
import co.com.crediya.r2dbc.entities.TypeLoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TypeLoanRepositoryAdapterTest {

    @InjectMocks
    TypeLoanRepositoryAdapter repositoryAdapter;

    @Mock
    TypeLoanRepository repository;

    @Mock
    ObjectMapper mapper;

    private TypeLoan typeLoan;

    private TypeLoanEntity typeLoanEntityOne;
    private TypeLoanEntity typeLoanEntityTwo;

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

        typeLoanEntityOne = TypeLoanEntity.builder()
                .id(1L)
                .name("Préstamo de Libre Inversión")
                .code("LIBRE_INVERSION")
                .autoValidation(true)
                .interestRate(new BigDecimal("0.0500"))
                .maxAmount(new BigDecimal("50000.00"))
                .minAmount(new BigDecimal("2000.00"))
                .build();

        typeLoanEntityTwo = TypeLoanEntity.builder()
                .id(2L)
                .name("Préstamo de Libre Inversión")
                .code("LIBRE_INVERSION")
                .autoValidation(true)
                .interestRate(new BigDecimal("0.0500"))
                .maxAmount(new BigDecimal("50000.00"))
                .minAmount(new BigDecimal("2000.00"))
                .build();
    }



    @Test
    @DisplayName("Must find type loan by id")
    void mustFindTypeLoanById() {

        when(repository.findById( typeLoanEntityOne.getId() )).thenReturn(Mono.just(typeLoanEntityOne));
        when(mapper.map(typeLoanEntityOne, TypeLoan.class)).thenReturn(typeLoan);

        Mono<TypeLoan> result = repositoryAdapter.findById( typeLoanEntityOne.getId() );

        StepVerifier.create(result)
                .expectNextMatches(loanSaved -> loanSaved.getName().equals(typeLoanEntityOne.getName())  )
                .verifyComplete();
    }


    @Test
    @DisplayName("Must find type loan by code")
    void mustFindTypeLoanByCode() {

        when(repository.findByCode( typeLoanEntityOne.getCode() )).thenReturn(Mono.just(typeLoanEntityOne));
        when(mapper.map(typeLoanEntityOne, TypeLoan.class)).thenReturn(typeLoan);

        Mono<TypeLoan> result = repositoryAdapter.findByCode( typeLoanEntityOne.getCode() );

        StepVerifier.create(result)
                .expectNextMatches(loanSaved -> loanSaved.getCode().equals(typeLoanEntityOne.getCode())  )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must find all type loans")
    void mustFindAllTypeLoans() {
        when(repository.findAll()).thenReturn(Flux.just(typeLoanEntityOne, typeLoanEntityTwo));
        when(mapper.map(typeLoanEntityOne, TypeLoan.class)).thenReturn(typeLoan);
        when(mapper.map(typeLoanEntityTwo, TypeLoan.class)).thenReturn(typeLoan);

        Flux<TypeLoan> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must save a type loan.")
    void mustSaveTypeLoan() {
        when(repository.save(typeLoanEntityOne)).thenReturn(Mono.just(typeLoanEntityOne));
        when(mapper.map(typeLoanEntityOne, TypeLoan.class)).thenReturn(typeLoan);
        when(mapper.map(typeLoan, TypeLoanEntity.class)).thenReturn(typeLoanEntityOne);

        Mono<TypeLoan> result = repositoryAdapter.save(typeLoan);

        StepVerifier.create(result)
                .expectNextMatches(savedLoan -> savedLoan.getCode().equals(typeLoanEntityOne.getCode()))
                .verifyComplete();
    }
}
