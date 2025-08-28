package co.com.crediya.consumer.rest;

import co.com.crediya.consumer.dtos.user.UserConsumerResponseDTO;
import co.com.crediya.consumer.dtos.user.UserGenericResponseDTO;
import co.com.crediya.consumer.mappers.UserConsumerMapper;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.consumers.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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
                .bodyToMono(new ParameterizedTypeReference<UserGenericResponseDTO<UserConsumerResponseDTO>>() {})
                .map( response -> userConsumerMapper.consumerToModel(response.getData()) );
    }
}
