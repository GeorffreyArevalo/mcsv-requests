package co.com.crediya.sqs.listener;

import co.com.crediya.sqs.listener.dtos.UpdateStateLoanListenerDTO;
import co.com.crediya.usecase.loan.LoanUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final LoanUseCase loanUseCase;
    private final ObjectMapper objectMapper;


    @Override
    public Mono<Void> apply(Message message) {
        log.info("Received SQS message - update state of loan: {}", message);
        return Mono.fromCallable( () ->  objectMapper.readValue(message.body(), UpdateStateLoanListenerDTO.class))
                .flatMap( data -> loanUseCase.updateLoanWithStateLoan(data.idLoan(), data.stateLoan()) )
                .then(Mono.empty());
    }
}
