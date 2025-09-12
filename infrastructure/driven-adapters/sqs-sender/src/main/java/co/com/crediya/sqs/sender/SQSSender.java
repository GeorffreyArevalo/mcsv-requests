package co.com.crediya.sqs.sender;

import co.com.crediya.model.Loan;
import co.com.crediya.port.consumers.model.User;
import co.com.crediya.port.queue.SendQueuePort;
import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import co.com.crediya.sqs.sender.dtos.DebtCapacityQueueDTO;
import co.com.crediya.sqs.sender.dtos.LoanQueueDTO;
import co.com.crediya.sqs.sender.mappers.SqsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements SendQueuePort {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final SqsMapper sqsMapper;
    private final ObjectMapper objectMapper;

    public Mono<Void> sendNotificationChangeStateLoan(MessageNotificationQueue messageNotification) {

        return Mono.fromCallable( () -> {
                String message = objectMapper.writeValueAsString(sqsMapper.messageToSqsDto(messageNotification));
                return buildRequest(message, properties.queueNotificationUrl());
            })
            .doOnNext(messageRequest -> log.info("Message sent - notification {}", messageRequest.messageBody()))
            .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
            .then(Mono.empty());
    }

    @Override
    public Mono<Void> sendCalculateDebtCapacity(Loan loan, List<Loan> approvedLoans, User user) {
        return Mono.fromCallable( () -> {
            List<LoanQueueDTO> loans = approvedLoans.stream().map(sqsMapper::modelToQueueDto).toList();
            LoanQueueDTO currentLoan = sqsMapper.modelToQueueDto(loan);
            DebtCapacityQueueDTO debtCapacityQueueDTO = new DebtCapacityQueueDTO(user.getBasePayment(), user.getName(), user.getLastName(), loan.getNotificationEmail(), currentLoan, loans);
            return buildRequest( objectMapper.writeValueAsString(debtCapacityQueueDTO), properties.queueDebtCapacity() );
        })
        .doOnNext(messageRequest -> log.info("Message sent - debt capacity {}", messageRequest.messageBody()))
        .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
        .then(Mono.empty());
    }

    @Override
    public Mono<Void> sendIncreaseReports(String message) {
        return Mono.fromCallable( () -> buildRequest( objectMapper.writeValueAsString(message), properties.queueIncreaseReport() ))
                .doOnNext(messageRequest -> log.info("Message sent - reports {}", messageRequest.messageBody()))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .then(Mono.empty());
    }

    private SendMessageRequest buildRequest(String message, String sqsUrl) {
        return SendMessageRequest.builder()
                .queueUrl(sqsUrl)
                .messageBody(message)
                .build();
    }
}
