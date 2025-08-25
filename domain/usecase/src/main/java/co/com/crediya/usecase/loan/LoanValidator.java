package co.com.crediya.usecase.loan;

import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaIllegalArgumentException;
import co.com.crediya.model.Loan;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanValidator {

    private static final BigDecimal MIN_VALUE_AMOUNT = BigDecimal.ZERO;

    static Mono<Loan> validateCreateLoan(Loan loan ) {

        if( loan.getAmount() == null || loan.getAmount().compareTo(MIN_VALUE_AMOUNT) <= 0  ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_AMOUNT_INVALID.getMessage()));
        }

        if( loan.getDeadline() == null || loan.getDeadline().isBefore(LocalDate.now()) ) {
            return Mono.error( new CrediyaIllegalArgumentException( ExceptionMessages.FIELD_DEADLINE_INVALID.getMessage() ) );
        }

        if( loan.getUserDocument() == null || loan.getUserDocument().isEmpty() ) {
            return Mono.error( new CrediyaIllegalArgumentException( ExceptionMessages.FIELD_USER_DOCUMENT_REQUIRED.getMessage() ) );
        }

        return Mono.just(loan);


    }


}
