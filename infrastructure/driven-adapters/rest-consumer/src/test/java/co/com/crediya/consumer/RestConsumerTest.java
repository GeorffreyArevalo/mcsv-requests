package co.com.crediya.consumer;


import co.com.crediya.consumer.mappers.UserConsumerMapper;
import co.com.crediya.consumer.rest.UserRestConsumer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    @Mock
    private static UserConsumerMapper userConsumerMapper;

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

        when( userConsumerMapper.toUserConsumer(any()) ).thenReturn(any());

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"state\" : \"ok\"}"));
        var response = restConsumer.getUserByDocument("1");

        StepVerifier.create(response)
                .expectNextMatches(objectResponse -> !objectResponse.getDocument().isBlank())
                .verifyComplete();
    }

}