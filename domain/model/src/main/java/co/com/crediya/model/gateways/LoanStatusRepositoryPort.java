package co.com.crediya.model.gateways;


import co.com.crediya.model.LoanState;
import reactor.core.publisher.Mono;

public interface LoanStatusRepositoryPort {

    Mono<LoanState> findByCode(String code);

}
