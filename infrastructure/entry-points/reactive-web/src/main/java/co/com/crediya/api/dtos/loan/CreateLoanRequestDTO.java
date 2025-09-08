package co.com.crediya.api.dtos.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateLoanRequestDTO(

        @NotNull(message = "is required!")
        @Min( value = 1)
        BigDecimal amount,

        @NotNull(message = "is required!")
        @Min(value = 3)
        Integer monthTerm,

        @NotBlank(message = "is required!")
        String userDocument,

        @NotBlank(message = "is required!")
        String codeTypeLoan
) {
}
