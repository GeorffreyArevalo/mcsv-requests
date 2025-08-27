package co.com.crediya.ports;

import reactor.core.publisher.Mono;

public interface TransactionManagement {

    public <T> Mono<T> inTransaction(Mono<T> action);
}
