package co.com.crediya.consumer.rest;

import co.com.crediya.consumer.dtos.user.UserConsumerResponseDTO;
import co.com.crediya.consumer.dtos.user.UserGenericResponseDTO;
import co.com.crediya.consumer.mappers.UserConsumerMapper;
import co.com.crediya.exceptions.CrediyaException;
import co.com.crediya.exceptions.CrediyaInternalServerErrorException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.consumers.model.User;
import co.com.crediya.port.token.SecurityAuthenticationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRestConsumer implements UserServicePort {

    private final WebClient client;
    private final UserConsumerMapper userConsumerMapper;
    private final SecurityAuthenticationPort securityAuthenticationPort;

    @CircuitBreaker(name = "getUserByDocument")
    public Mono<User> getUserByDocument(String document) {
        log.info("Getting user by document {}", document);
        return securityAuthenticationPort.getCurrentContextToken()
            .switchIfEmpty(
                Mono.error( new CrediyaInternalServerErrorException(ExceptionMessages.INTERNAL_SERVER_ERROR_GET_TOKEN.getMessage()))
            )
            .flatMap( token -> client
                .get()
                .uri("/api/v1/users/byDocument/{document}", document)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus( HttpStatusCode::is4xxClientError, response -> response.bodyToMono( UserGenericResponseDTO.class )
                    .flatMap( error ->
                        Mono.error(new CrediyaException(
                            ExceptionStatusCode.NOT_FOUND, error.getMessage(), response.statusCode().value()
                        ))
                    )
                )
                .onStatus( HttpStatusCode::is5xxServerError, response ->
                    Mono.error(new CrediyaException(
                        ExceptionStatusCode.INTERNAL_SERVER_ERROR, ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.getMessage(),  response.statusCode().value()
                    ))
                )
                .bodyToMono(new ParameterizedTypeReference<UserGenericResponseDTO<UserConsumerResponseDTO>>() {})
                .map( response ->
                    userConsumerMapper.consumerToModel(response.getData())
                )
            );
    }
}
