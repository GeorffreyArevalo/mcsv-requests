package co.com.crediya.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanEntity {

    @Id
    private Long id;
    private BigDecimal amount;
    private Integer monthTerm;
    private String notificationEmail;
    private String userDocument;
    private Long idTypeLoan;
    private Long idLoanState;

}
