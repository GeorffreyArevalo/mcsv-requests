package co.com.crediya.api.mappers;


import co.com.crediya.api.dtos.loan.CreateLoanRequestDTO;
import co.com.crediya.api.dtos.loan.FindLoansResponseDTO;
import co.com.crediya.api.dtos.loan.LoanResponseDTO;
import co.com.crediya.model.Loan;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class LoanMapperTest {

    private final CreateLoanRequestDTO createLoanRequestDTO = new CreateLoanRequestDTO(
            BigDecimal.valueOf(10.0),
            12,
            "100688719923243",
            "LIBRE_INVERSION"
    );

    private final  Loan loan  = Loan.builder()
            .idLoanState(1L)
            .idTypeLoan(1L)
            .amount(new BigDecimal("10.0"))
            .monthTerm(12)
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
                        && loanResult.getMonthTerm().equals( createLoanRequestDTO.monthTerm() )
                        && loanResult.getAmount().equals( createLoanRequestDTO.amount())

                )
                .verifyComplete();
    }


    @Test
    void testModelToFindLoanResponse() {
        Mono<FindLoansResponseDTO> result = Mono.fromCallable(() -> loanMapper.modelToFindLoanResponse(loan));

        StepVerifier.create(result)
                .expectNextMatches( loanResponseResult ->
                        loanResponseResult.userDocument().equals(loan.getUserDocument())
                        && loanResponseResult.amount().equals(loan.getAmount())
                        && loanResponseResult.monthTerm().equals(loan.getMonthTerm())
                        && loanResponseResult.idTypeLoan().equals( loan.getIdTypeLoan())
                        && loanResponseResult.idLoanState().equals( loan.getIdLoanState())
                )
                .verifyComplete();
    }


    @Test
    void testModelToResponse() {
        Mono<LoanResponseDTO> result = Mono.fromCallable(() -> loanMapper.modelToResponse(loan));

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.amount().equals(loan.getAmount()) &&
                                dto.monthTerm().equals(loan.getMonthTerm()) &&
                                dto.notificationEmail().equals(loan.getNotificationEmail()) &&
                                dto.userDocument().equals(loan.getUserDocument()) &&
                                dto.idLoanState().equals(loan.getIdLoanState()) &&
                                dto.idTypeLoan().equals(loan.getIdTypeLoan())
                )
                .verifyComplete();
    }

}
