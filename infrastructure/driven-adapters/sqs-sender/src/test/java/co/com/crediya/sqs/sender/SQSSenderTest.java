package co.com.crediya.sqs.sender;

import co.com.crediya.model.Loan;
import co.com.crediya.port.consumers.model.User;
import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import co.com.crediya.sqs.sender.dtos.MessageNotificationSqsDTO;
import co.com.crediya.sqs.sender.mappers.SqsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SQSSenderTest {

    private SQSSenderProperties properties;
    private SqsAsyncClient client;
    private SqsMapper sqsMapper;
    private ObjectMapper objectMapper;
    private SQSSender sqsSender;

    @BeforeEach
    void setup() {
        properties = mock(SQSSenderProperties.class);
        client = mock(SqsAsyncClient.class);
        sqsMapper = mock(SqsMapper.class);
        objectMapper = new ObjectMapper();

        sqsSender = new SQSSender(properties, client, sqsMapper, objectMapper);

        when(properties.queueNotificationUrl()).thenReturn("http://fake-queue/notification");
        when(properties.queueDebtCapacity()).thenReturn("http://fake-queue/debtcapacity");
    }

    @Test
    void sendNotificationChangeStateLoan_ShouldSendSuccessfully() throws Exception {

        MessageNotificationQueue notification = MessageNotificationQueue.builder()
                .email("julian@test.com")
                .stateLoan("APROBADO")
                .clientLastName("Arevalo")
                .clientName("Julian")
                .amount(BigDecimal.TEN)
                .build();

        MessageNotificationSqsDTO dto = new MessageNotificationSqsDTO(
                notification.getEmail(),
                notification.getStateLoan(),
                notification.getClientName(),
                notification.getClientLastName(),
                notification.getAmount()
        );

        when(sqsMapper.messageToSqsDto(notification)).thenReturn(dto);

        SendMessageResponse response = SendMessageResponse.builder().messageId("123").build();
        when(client.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        StepVerifier.create(sqsSender.sendNotificationChangeStateLoan(notification))
                .verifyComplete();

        verify(client, times(1)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void sendCalculateDebtCapacity_ShouldSendSuccessfully() {

        Loan loan = Loan.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .interestRate(BigDecimal.valueOf(0.05))
                .monthTerm(12)
                .notificationEmail("loan@test.com")
                .build();

        Loan approvedLoan = Loan.builder()
                .id(2L)
                .amount(BigDecimal.valueOf(2000))
                .interestRate(BigDecimal.valueOf(0.08))
                .monthTerm(24)
                .notificationEmail("approved@test.com")
                .build();

        User user = User.builder()
                .document("12345")
                .name("Julian")
                .lastName("Arevalo")
                .basePayment(BigDecimal.valueOf(500))
                .build();

        when(sqsMapper.modelToQueueDto(any(Loan.class)))
                .thenAnswer(invocation -> {
                    Loan l = invocation.getArgument(0);
                    return new co.com.crediya.sqs.sender.dtos.LoanQueueDTO(
                            l.getId(), l.getAmount(), l.getInterestRate(), l.getMonthTerm()
                    );
                });

        SendMessageResponse response = SendMessageResponse.builder().messageId("456").build();
        when(client.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        StepVerifier.create(sqsSender.sendCalculateDebtCapacity(loan, List.of(approvedLoan), user))
                .verifyComplete();

        verify(client, times(1)).sendMessage(any(SendMessageRequest.class));
    }

}
