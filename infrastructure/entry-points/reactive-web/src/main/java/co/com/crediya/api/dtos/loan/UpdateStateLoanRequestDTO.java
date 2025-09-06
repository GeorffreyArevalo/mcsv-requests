package co.com.crediya.api.dtos.loan;

import jakarta.validation.constraints.NotBlank;

public record UpdateStateLoanRequestDTO(

        @NotBlank(message = "es required.")
        String codeState
) {
}
