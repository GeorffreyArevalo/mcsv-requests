package co.com.crediya.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Loan {

    private BigDecimal amount;
    private LocalDate deadline;
    private String notificationEmail;
    private String userDocument;
    private String codeLoanState;
    private String codeTypeLoan;


}
