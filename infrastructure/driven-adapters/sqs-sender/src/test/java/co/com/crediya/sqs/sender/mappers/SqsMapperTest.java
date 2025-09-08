package co.com.crediya.sqs.sender.mappers;

import co.com.crediya.model.Loan;
import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import co.com.crediya.sqs.sender.dtos.LoanQueueDTO;
import co.com.crediya.sqs.sender.dtos.MessageNotificationSqsDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class SqsMapperTest {

    private final SqsMapper mapper = Mappers.getMapper(SqsMapper.class);

    @Test
    void testMessageToSqsDto() {
        MessageNotificationQueue message = MessageNotificationQueue.builder()
                .email("test@email.com")
                .stateLoan("APPROVED")
                .clientName("Julian")
                .clientLastName("Arevalo")
                .amount(BigDecimal.valueOf(5000))
                .build();

        Mono<MessageNotificationSqsDTO> result = Mono.fromCallable(() -> mapper.messageToSqsDto(message));

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.email().equals(message.getEmail()) &&
                                dto.stateLoan().equals(message.getStateLoan()) &&
                                dto.name().equals(message.getClientName()) &&
                                dto.lastName().equals(message.getClientLastName()) &&
                                dto.amount().equals(message.getAmount())
                )
                .verifyComplete();
    }

    @Test
    void testModelToQueueDto() {
        Loan loan = Loan.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(10000))
                .interestRate(BigDecimal.valueOf(0.05))
                .monthTerm(12)
                .build();

        Mono<LoanQueueDTO> result = Mono.fromCallable(() -> mapper.modelToQueueDto(loan));

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.id().equals(loan.getId()) &&
                                dto.amount().equals(loan.getAmount()) &&
                                dto.interestRate().equals(loan.getInterestRate()) &&
                                dto.term().equals(loan.getMonthTerm())
                )
                .verifyComplete();
    }
}
