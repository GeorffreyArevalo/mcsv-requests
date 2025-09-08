package co.com.crediya.api.dtos.loan;

import java.math.BigDecimal;

public record FindLoansResponseDTO(
        BigDecimal amount,
        Integer monthTerm,
        String notificationEmail,
        String userDocument,
        Long idLoanState,
        Long idTypeLoan,

        String state,
        String type,
        BigDecimal basePayment,
        String nameClient,
        BigDecimal interestRate,
        BigDecimal monthlyFee
) {
}
