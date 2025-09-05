package co.com.crediya.api.dtos.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SearchLoansRequestDTO(

        @Min(value = 1, message = "must be greater than 0.")
        @NotNull(message = "is required.")
        Integer size,

        @Min(value = 0, message = "mustn't be negative.")
        @NotNull(message = "is required.")
        Integer page,

        @NotBlank(message = "is required.")
        String state
) {

}
