package co.com.crediya.sqs.sender.dtos;

import java.math.BigDecimal;

public record MessageNotificationSqsDTO(
        String email,
        String stateLoan,
        String name,
        String lastName,
        BigDecimal amount
) {
}
