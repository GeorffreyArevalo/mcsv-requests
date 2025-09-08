package co.com.crediya.sqs.sender.dtos;

import java.math.BigDecimal;
import java.util.List;

public record DebtCapacityQueueDTO(
        BigDecimal clientBasePayment,
        String clientName,
        String clientLastName,
        String clientEmail,
        LoanQueueDTO currentLoan,
        List<LoanQueueDTO> approvedLoans
) {
}
