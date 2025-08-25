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
public class TypeLoan {

    private Long id;
    private String code;
    private String name;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private  BigDecimal interestRate;
    private Boolean autoValidation;

}
