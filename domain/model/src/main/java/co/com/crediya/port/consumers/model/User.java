package co.com.crediya.port.consumers.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder( toBuilder = true )
public class User {

    private String document;
    private String name;
    private String lastName;
    private String email;
    private BigDecimal basePayment;


}
