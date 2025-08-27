package co.com.crediya.r2dbc.helper;

import co.com.crediya.ports.TransactionManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionalBoundary implements TransactionManagement {

    private final TransactionalOperator operator;

    public <T> Mono<T> inTransaction(Mono<T> action) {
        return operator.execute(tr -> action).single();
    }
}