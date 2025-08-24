package co.com.crediya.usecase.requests;

import co.com.crediya.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.Requests;
import co.com.crediya.model.gateways.RequestsRepository;
import co.com.crediya.port.consumers.UserConsumerPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class RequestsUseCase implements RequestsServicePort {

    private final RequestsRepository requestsRepository;
    private final UserConsumerPort userConsumerPort;



    @Override
    public Mono<Requests> saveRequest(Requests requests) {

        return RequestsValidator.validateCreateRequests(requests)
                .flatMap( validRequests -> {
                    return userConsumerPort.findByDocument(validRequests.getUserDocument())
                            .thenReturn(validRequests)
                            .onErrorMap( (e) -> new CrediyaResourceNotFoundException(ExceptionMessages.USER_WITH_DOCUMENT_NOT_FOUND.getMessage()));
                })
                .flatMap(this::saveRequest)
                .doOnSuccess( savedRequests -> {} );

    }
}
