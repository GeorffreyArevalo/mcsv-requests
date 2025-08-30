package co.com.crediya.api.dtos.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateLoanRequestDTO(

        @NotNull(message = "is required!")
        @Min( value = 1)
        BigDecimal amount,

        @NotNull(message = "is required!")
        @PastOrPresent( message = "must be after today")
        LocalDate deadline,

        String notificationEmail,

        @NotBlank(message = "is required!")
        String userDocument,

        @NotBlank(message = "is required!")
        String codeTypeLoan
) {
}
