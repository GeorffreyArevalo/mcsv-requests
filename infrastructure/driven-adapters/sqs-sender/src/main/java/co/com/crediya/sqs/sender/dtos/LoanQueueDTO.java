package co.com.crediya.sqs.sender.dtos;

import java.math.BigDecimal;

public record LoanQueueDTO(
        Long id,
        BigDecimal amount,
        BigDecimal interestRate,
        Integer term
) {
}
