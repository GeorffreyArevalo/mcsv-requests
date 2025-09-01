package co.com.crediya.consumer;

import co.com.crediya.consumer.dtos.user.UserConsumerResponseDTO;
import co.com.crediya.consumer.mappers.UserConsumerMapper;
import co.com.crediya.consumer.rest.UserRestConsumer;
import co.com.crediya.exceptions.CrediyaException;
import co.com.crediya.exceptions.CrediyaInternalServerErrorException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.port.consumers.model.User;
import co.com.crediya.port.token.SecurityAuthenticationPort;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestConsumerTest {

    private MockWebServer mockWebServer;
    private UserConsumerMapper userConsumerMapper;
    private SecurityAuthenticationPort securityAuthenticationPort;
    private UserRestConsumer userRestConsumer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        userConsumerMapper = Mockito.mock(UserConsumerMapper.class);
        securityAuthenticationPort = Mockito.mock(SecurityAuthenticationPort.class);

        userRestConsumer = new UserRestConsumer(webClient, userConsumerMapper, securityAuthenticationPort);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getUserByDocument_withValidToken_shouldReturnUser() {
        String token = "valid-token";
        String document = "123";

        User user = new User();

        when(securityAuthenticationPort.getCurrentContextToken()).thenReturn(Mono.just(token));
        when(userConsumerMapper.consumerToModel(any(UserConsumerResponseDTO.class))).thenReturn(user);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\":{}}") // cuerpo mínimo válido
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(userRestConsumer.getUserByDocument(document))
                .expectNext(user)
                .verifyComplete();

        verify(securityAuthenticationPort, times(1)).getCurrentContextToken();
        verify(userConsumerMapper, times(1)).consumerToModel(any(UserConsumerResponseDTO.class));
    }

    @Test
    void getUserByDocument_withoutTokenShouldThrowInternalServerErrorException() {

        String document = "123";

        when(securityAuthenticationPort.getCurrentContextToken()).thenReturn(Mono.empty());

        StepVerifier.create(userRestConsumer.getUserByDocument(document))
                .expectErrorMatches(throwable ->
                        throwable instanceof CrediyaInternalServerErrorException &&
                                throwable.getMessage().equals(ExceptionMessages.INTERNAL_SERVER_ERROR_GET_TOKEN.getMessage())
                )
                .verify();

        verify(securityAuthenticationPort, times(1)).getCurrentContextToken();
        verifyNoInteractions(userConsumerMapper);
    }

    @Test
    void getUserByDocumentWithNotFoundResponseShouldThrowCrediyaException() {

        String token = "valid-token";
        String document = "123";

        when(securityAuthenticationPort.getCurrentContextToken()).thenReturn(Mono.just(token));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"message\":\"User not found\"}")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(userRestConsumer.getUserByDocument(document))
                .expectErrorMatches(throwable ->
                        throwable instanceof CrediyaException &&
                                ((CrediyaException) throwable).getStatusCode() == ExceptionStatusCode.NOT_FOUND
                )
                .verify();

        verify(securityAuthenticationPort, times(1)).getCurrentContextToken();
    }

    @Test
    void getUserByDocumentWithInternalServerErrorShouldThrowCrediyaException() {

        String token = "valid-token";
        String document = "123";

        when(securityAuthenticationPort.getCurrentContextToken()).thenReturn(Mono.just(token));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"message\":\"Server error\"}")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(userRestConsumer.getUserByDocument(document))
                .expectErrorMatches(throwable ->
                        throwable instanceof CrediyaException &&
                                ((CrediyaException) throwable).getStatusCode() == ExceptionStatusCode.INTERNAL_SERVER_ERROR
                )
                .verify();

        verify(securityAuthenticationPort, times(1)).getCurrentContextToken();
    }
}