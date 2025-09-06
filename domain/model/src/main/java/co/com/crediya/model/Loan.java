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

    private Long id;
    private BigDecimal amount;
    private LocalDate deadline;
    private String notificationEmail;
    private String userDocument;
    private Long idLoanState;
    private Long idTypeLoan;

    private String state;
    private String type;
    private BigDecimal basePayment;
    private String nameClient;
    private BigDecimal monthlyFee;
    private BigDecimal interestRate;

}
