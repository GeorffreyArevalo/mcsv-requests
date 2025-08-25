package co.com.crediya.consumer.rest;

import co.com.crediya.consumer.dtos.user.UserResponse;
import co.com.crediya.consumer.mappers.UserConsumerMapper;
import co.com.crediya.port.consumers.UserConsumerPort;
import co.com.crediya.port.consumers.model.UserConsumer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserRestConsumer implements UserConsumerPort {
    private final WebClient client;

    private final UserConsumerMapper userConsumerMapper;

    @CircuitBreaker(name = "getUserByDocument")
    public Mono<UserConsumer> getUserByDocument(String document) {
        return client
                .get()
                .uri("/api/v1/users/byDocument/{document}", document)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .map( userConsumerMapper::toUserConsumer );
    }
}
