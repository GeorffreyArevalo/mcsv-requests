package co.com.crediya.api.dtos.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateLoanRequest(
        BigDecimal amount,
        LocalDate deadline,
        String notificationEmail,
        String userDocument,
        String codeTypeLoan
) {
}
