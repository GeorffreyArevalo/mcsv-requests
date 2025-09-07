package co.com.crediya.port.queue.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageNotificationQueue {

    private String email;
    private String stateLoan;
    private String clientName;
    private String clientLastName;
    private BigDecimal amount;

}
