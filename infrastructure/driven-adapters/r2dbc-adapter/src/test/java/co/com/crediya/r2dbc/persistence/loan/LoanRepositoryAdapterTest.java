package co.com.crediya.r2dbc.persistence.loan;

import co.com.crediya.model.Loan;
import co.com.crediya.r2dbc.entities.LoanEntity;
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
import java.time.LocalDate;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoanRepositoryAdapterTest {

    @InjectMocks
    LoanRepositoryAdapter repositoryAdapter;

    @Mock
    LoanRepository repository;

    @Mock
    ObjectMapper mapper;

    private Loan loan;

    private LoanEntity loanEntityOne;
    private LoanEntity loanEntityTwo;

    @BeforeEach
    void setUp() {
        loan = Loan.builder()
                .amount(new BigDecimal("10.0"))
                .monthTerm(12)
                .notificationEmail("julian@gmail.com")
                .userDocument("12333123123")
                .idTypeLoan(1L)
                .idLoanState(1L)
                .build();

        loanEntityOne = LoanEntity.builder()
                .id(1L)
                .amount(new BigDecimal("10.0"))
                .monthTerm(12)
                .notificationEmail("julian@gmail.com")
                .userDocument("12333123123")
                .idTypeLoan(1L)
                .idLoanState(1L)
                .build();

        loanEntityTwo = LoanEntity.builder()
                .id(2L)
                .amount(new BigDecimal("10.0"))
                .monthTerm(12)
                .notificationEmail("julian@gmail.com")
                .userDocument("12333123123")
                .idTypeLoan(1L)
                .idLoanState(1L)
                .build();
    }



    @Test
    @DisplayName("Must find loan by id")
    void mustFindLoanById() {

        when(repository.findById( loanEntityOne.getId() )).thenReturn(Mono.just(loanEntityOne));
        when(mapper.map(loanEntityOne, Loan.class)).thenReturn(loan);

        Mono<Loan> result = repositoryAdapter.findById( loanEntityOne.getId() );

        StepVerifier.create(result)
                .expectNextMatches(loanSaved -> loanSaved.getIdTypeLoan().equals(loanEntityOne.getIdTypeLoan())  )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must find all loans")
    void mustFindAllLoans() {
        when(repository.findAll()).thenReturn(Flux.just(loanEntityOne, loanEntityTwo));
        when(mapper.map(loanEntityOne, Loan.class)).thenReturn(loan);
        when(mapper.map(loanEntityTwo, Loan.class)).thenReturn(loan);

        Flux<Loan> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must save a loan.")
    void mustSaveLoan() {

        when(repository.save(loanEntityOne)).thenReturn(Mono.just(loanEntityOne));
        when(mapper.map(loanEntityOne, Loan.class)).thenReturn(loan);
        when(mapper.map(loan, LoanEntity.class)).thenReturn(loanEntityOne);

        Mono<Loan> result = repositoryAdapter.save(loan);

        StepVerifier.create(result)
                .expectNextMatches(savedLoan -> savedLoan.getUserDocument().equals(loanEntityOne.getUserDocument()))
                .verifyComplete();
    }
}
