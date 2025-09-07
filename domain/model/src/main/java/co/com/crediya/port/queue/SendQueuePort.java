package co.com.crediya.port.queue;

import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import reactor.core.publisher.Mono;

public interface SendQueuePort {

    Mono<Void> sendNotificationChangeStateLoan(MessageNotificationQueue messageNotification);

}
