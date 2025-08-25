package co.com.crediya.r2dbc.persistence.loanstate;

import co.com.crediya.model.LoanState;
import co.com.crediya.r2dbc.entities.LoanStateEntity;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanStateRepositoryAdapterTest {

    @InjectMocks
    LoanStateRepositoryAdapter repositoryAdapter;

    @Mock
    LoanStateRepository repository;

    @Mock
    ObjectMapper mapper;

    private LoanState loanState;

    private LoanStateEntity loanStateEntityOne;
    private LoanStateEntity loanStateEntityTwo;

    @BeforeEach
    void setUp() {
        loanState = LoanState.builder()
                .id(1L)
                .code("PENDIENTE_DE_REVISION")
                .description("El equipo de crédito está revisando los documentos y validando la información proporcionada.")
                .name("Pendiente de Revisión")
                .build();

        loanStateEntityOne = LoanStateEntity.builder()
                .id(1L)
                .code("PENDIENTE_DE_REVISION")
                .description("El equipo de crédito está revisando los documentos y validando la información proporcionada.")
                .name("Pendiente de Revisión")
                .build();

        loanStateEntityTwo = LoanStateEntity.builder()
                .id(2L)
                .code("PENDIENTE_DE_REVISION")
                .description("El equipo de crédito está revisando los documentos y validando la información proporcionada.")
                .name("Pendiente de Revisión")
                .build();
    }



    @Test
    @DisplayName("Must find type loan by id")
    void mustFindTypeLoanById() {

        when(repository.findById( loanStateEntityOne.getId() )).thenReturn(Mono.just(loanStateEntityOne));
        when(mapper.map(loanStateEntityOne, LoanState.class)).thenReturn(loanState);

        Mono<LoanState> result = repositoryAdapter.findById( loanStateEntityOne.getId() );

        StepVerifier.create(result)
                .expectNextMatches(loanSaved -> loanSaved.getName().equals(loanStateEntityOne.getName())  )
                .verifyComplete();
    }


    @Test
    @DisplayName("Must find type loan by code")
    void mustFindTypeLoanByCode() {

        when(repository.findByCode( loanStateEntityOne.getCode() )).thenReturn(Mono.just(loanStateEntityOne));
        when(mapper.map(loanStateEntityOne, LoanState.class)).thenReturn(loanState);

        Mono<LoanState> result = repositoryAdapter.findByCode( loanStateEntityOne.getCode() );

        StepVerifier.create(result)
                .expectNextMatches(loanSaved -> loanSaved.getCode().equals(loanStateEntityOne.getCode())  )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must find all type loans")
    void mustFindAllTypeLoans() {
        when(repository.findAll()).thenReturn(Flux.just(loanStateEntityOne, loanStateEntityTwo));
        when(mapper.map(loanStateEntityOne, LoanState.class)).thenReturn(loanState);
        when(mapper.map(loanStateEntityTwo, LoanState.class)).thenReturn(loanState);

        Flux<LoanState> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must save a type loan.")
    void mustSaveTypeLoan() {
        when(repository.save(loanStateEntityOne)).thenReturn(Mono.just(loanStateEntityOne));
        when(mapper.map(loanStateEntityOne, LoanState.class)).thenReturn(loanState);
        when(mapper.map(loanState, LoanStateEntity.class)).thenReturn(loanStateEntityOne);

        Mono<LoanState> result = repositoryAdapter.save(loanState);

        StepVerifier.create(result)
                .expectNextMatches(savedLoan -> savedLoan.getCode().equals(loanStateEntityOne.getCode()))
                .verifyComplete();
    }
}
