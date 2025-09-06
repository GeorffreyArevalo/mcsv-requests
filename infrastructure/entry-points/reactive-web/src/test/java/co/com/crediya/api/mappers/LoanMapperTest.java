package co.com.crediya.api.mappers;


import co.com.crediya.api.dtos.loan.CreateLoanRequestDTO;
import co.com.crediya.api.dtos.loan.LoanResponseDTO;
import co.com.crediya.model.Loan;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

class LoanMapperTest {

    private final CreateLoanRequestDTO createLoanRequestDTO = new CreateLoanRequestDTO(
            BigDecimal.valueOf(10.0),
            LocalDate.now(),
            "100688719923243",
            "LIBRE_INVERSION"
    );

    private final  Loan loan  = Loan.builder()
            .idLoanState(1L)
            .idTypeLoan(1L)
            .amount(new BigDecimal("10.0"))
            .deadline(LocalDate.now())
            .userDocument("100688719923243")
            .notificationEmail("geoeffrey@arevalo.com")
            .build();

    private final LoanMapper loanMapper = Mappers.getMapper(LoanMapper.class);
    @Test
    void testCreateRequestToModel() {
        Mono<Loan> result = Mono.fromCallable(() -> loanMapper.createRequestToModel(createLoanRequestDTO, 1L));

        StepVerifier.create(result)
                .expectNextMatches( loanResult ->
                        loanResult.getUserDocument().equals(createLoanRequestDTO.userDocument())
                        && loanResult.getIdTypeLoan().equals( 1L )
                        && loanResult.getDeadline().equals( createLoanRequestDTO.deadline() )
                        && loanResult.getAmount().equals( createLoanRequestDTO.amount())

                )
                .verifyComplete();
    }


    @Test
    void testModelToResponse() {
        Mono<LoanResponseDTO> result = Mono.fromCallable(() -> loanMapper.modelToResponse(loan));

        StepVerifier.create(result)
                .expectNextMatches( loanResponseResult ->
                        loanResponseResult.userDocument().equals(loan.getUserDocument())
                        && loanResponseResult.amount().equals(loan.getAmount())
                        && loanResponseResult.deadline().equals(loan.getDeadline())
                        && loanResponseResult.idTypeLoan().equals( loan.getIdTypeLoan())
                        && loanResponseResult.idLoanState().equals( loan.getIdLoanState())
                )
                .verifyComplete();
    }

}
