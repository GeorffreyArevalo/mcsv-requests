package co.com.crediya.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("types_loan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TypeLoanEntity {

    @Id
    private Long id;
    private String code;
    private String name;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal interestRate;
    private Boolean autoValidation;


}
