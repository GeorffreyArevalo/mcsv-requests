package co.com.crediya.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Loan {

    private Long id;
    private BigDecimal amount;
    private Integer monthTerm;
    private String notificationEmail;
    private String userDocument;
    private Long idLoanState;
    private Long idTypeLoan;

    private String state;
    private String type;
    private BigDecimal basePayment;
    private String nameClient;
    private String lastNameClient;
    private BigDecimal monthlyFee;
    private BigDecimal interestRate;

}
