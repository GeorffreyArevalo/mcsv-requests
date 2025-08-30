package co.com.crediya.consumer.rest;

import co.com.crediya.consumer.dtos.user.UserConsumerResponseDTO;
import co.com.crediya.consumer.dtos.user.UserGenericResponseDTO;
import co.com.crediya.consumer.mappers.UserConsumerMapper;
import co.com.crediya.exceptions.CrediyaException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.consumers.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserRestConsumer implements UserServicePort {
    private final WebClient client;

    private final UserConsumerMapper userConsumerMapper;

    @CircuitBreaker(name = "getUserByDocument")
    public Mono<User> getUserByDocument(String document) {
        return client
                .get()
                .uri("/api/v1/users/byDocument/{document}", document)
                .retrieve()
                .onStatus( HttpStatusCode::is4xxClientError, response ->
                    response.bodyToMono( UserGenericResponseDTO.class )
                    .flatMap( error ->
                            Mono.error(new CrediyaException(
                                    ExceptionStatusCode.NOT_FOUND, error.getMessage(), response.statusCode().value()
                            ))
                    )
                )
                .onStatus( HttpStatusCode::is5xxServerError, response ->
                    Mono.error(new CrediyaException(
                            ExceptionStatusCode.INTERNAL_SERVER_ERROR, ExceptionMessages.WBE_CLIENT_INTERNAL_SERVER_ERROR.getMessage(),  response.statusCode().value()
                    ))
                )
                .bodyToMono(new ParameterizedTypeReference<UserGenericResponseDTO<UserConsumerResponseDTO>>() {})
                .map( response -> userConsumerMapper.consumerToModel(response.getData()) );
    }
}
