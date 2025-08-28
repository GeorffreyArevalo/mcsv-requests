package co.com.crediya.api.dtos.loan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateLoanRequestDTO(

        @NotNull(message = "is required!")
        BigDecimal amount,

        @NotNull(message = "is required!")
        LocalDate deadline,

        String notificationEmail,

        @NotBlank(message = "is required!")
        String userDocument,

        @NotBlank(message = "is required!")
        String codeTypeLoan
) {
}
