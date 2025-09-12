package co.com.crediya.port.queue;

import co.com.crediya.model.Loan;
import co.com.crediya.port.consumers.model.User;
import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SendQueuePort {

    Mono<Void> sendNotificationChangeStateLoan(MessageNotificationQueue messageNotification);

    Mono<Void> sendCalculateDebtCapacity(Loan loan, List<Loan> approvedLoans, User user);

    Mono<Void> sendIncreaseReports(String message);

}
