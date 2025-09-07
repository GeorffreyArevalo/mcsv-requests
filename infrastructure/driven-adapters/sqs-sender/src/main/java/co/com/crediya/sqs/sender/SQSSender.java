package co.com.crediya.sqs.sender;

import co.com.crediya.port.queue.SendQueuePort;
import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import co.com.crediya.sqs.sender.mappers.SqsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

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
            .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
            .doOnNext(response -> log.info("Message sent {}", response.messageId()))
            .then(Mono.empty());
    }

    private SendMessageRequest buildRequest(String message, String sqsUrl) {
        return SendMessageRequest.builder()
                .queueUrl(sqsUrl)
                .messageBody(message)
                .build();
    }
}
