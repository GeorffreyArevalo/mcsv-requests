package co.com.crediya.consumer.dtos.user;


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
public class UserConsumerResponseDTO {

    private String name;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private LocalDate dateOfBirth;
    private BigDecimal basePayment;

}