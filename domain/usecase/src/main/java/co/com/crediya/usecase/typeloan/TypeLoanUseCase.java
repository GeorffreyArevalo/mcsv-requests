package co.com.crediya.usecase.typeloan;

import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.TypeLoan;
import co.com.crediya.model.gateways.TypeLoanRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TypeLoanUseCase implements TypeLoanServicePort {

    private final TypeLoanRepositoryPort typeLoanRepositoryPort;

    @Override
    public Mono<TypeLoan> findByCode(String code) {
        return typeLoanRepositoryPort.findByCode(code)
                .switchIfEmpty(Mono.error(new CrediyaResourceNotFoundException(
                        String.format(ExceptionMessages.TYPE_LOAN_WITH_CODE_NOT_FOUND.getMessage(), code)
                )));
    }
}
