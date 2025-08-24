package co.com.crediya.usecase.requests;

import co.com.crediya.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaIllegalArgumentException;
import co.com.crediya.model.Requests;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequestsValidator {

    private static final BigDecimal MIN_VALUE_AMOUNT = BigDecimal.ZERO;

    static Mono<Requests> validateCreateRequests( Requests requests ) {

        if( requests.getAmount() == null || requests.getAmount().compareTo(MIN_VALUE_AMOUNT) <= 0  ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_AMOUNT_INVALID.getMessage()));
        }

        if( requests.getDeadline() == null || requests.getDeadline().isBefore(LocalDate.now()) ) {
            return Mono.error( new CrediyaIllegalArgumentException( ExceptionMessages.FIELD_DEADLINE_INVALID.getMessage() ) );
        }

        if( requests.getUserDocument() == null || requests.getUserDocument().isEmpty() ) {
            return Mono.error( new CrediyaIllegalArgumentException( ExceptionMessages.FIELD_USER_DOCUMENT_REQUIRED.getMessage() ) );
        }

        return Mono.just(requests);


    }


}
