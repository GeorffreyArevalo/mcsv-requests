package co.com.crediya.model.gateways;


import co.com.crediya.model.LoanState;
import reactor.core.publisher.Mono;

public interface LoanStateRepositoryPort {

    Mono<LoanState> findByCode(String code);

}
