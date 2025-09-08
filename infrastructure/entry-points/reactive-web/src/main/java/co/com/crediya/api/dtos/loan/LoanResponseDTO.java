package co.com.crediya.api.dtos.loan;

import java.math.BigDecimal;

public record LoanResponseDTO(
        BigDecimal amount,
        Integer monthTerm,
        String notificationEmail,
        String userDocument,
        Long idLoanState,
        Long idTypeLoan
) {
}
