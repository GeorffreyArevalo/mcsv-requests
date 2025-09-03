package co.com.crediya.api.dtos.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanResponseDTO(
        BigDecimal amount,
        LocalDate deadline,
        String notificationEmail,
        String userDocument,
        Long idLoanState,
        Long idTypeLoan,

        String state,
        String type,
        BigDecimal basePayment,
        String nameClient,
        BigDecimal interestRate
) {
}
