package co.com.crediya.consumer;


import co.com.crediya.consumer.dtos.user.UserConsumerResponseDTO;
import co.com.crediya.consumer.mappers.UserConsumerMapper;
import co.com.crediya.consumer.rest.UserRestConsumer;
import co.com.crediya.port.consumers.model.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


class RestConsumerTest {

    private static UserRestConsumer restConsumer;

    private static MockWebServer mockBackEnd;

    private static final UserConsumerMapper userConsumerMapper = Mappers.getMapper(UserConsumerMapper.class);

    private final User user = User.builder()
            .name("Georffrey")
            .lastName("Arevalo")
            .document("123")
            .email("arevalo@gmail.com")
            .build();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new UserRestConsumer(webClient, userConsumerMapper);
    }

    @AfterAll
    static void tearDown() throws IOException {

        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate the function testGet.")
    void validateTestGet() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"data\": {\"document\":\"123\", \"name\":\"Georffrey\", \"lastName\":\"Arevalo\"}}"));
        var response = restConsumer.getUserByDocument("1");

        StepVerifier.create(response)
                .expectNextMatches(userResponse -> userResponse.getDocument().equals(user.getDocument()))
                .verifyComplete();
    }

}