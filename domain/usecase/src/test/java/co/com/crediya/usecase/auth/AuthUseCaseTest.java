package co.com.crediya.usecase.auth;

import co.com.crediya.exceptions.CrediyaForbiddenException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Token;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.token.JwtAuthenticationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthUseCaseTest {

    private JwtAuthenticationPort jwtAuthenticationPort;
    private UserServicePort userServicePort;
    private AuthUseCase authUseCase;

    private Token token;

    @BeforeEach
    void setUp() {
        jwtAuthenticationPort = mock(JwtAuthenticationPort.class);
        userServicePort = mock(UserServicePort.class);

        authUseCase = new AuthUseCase(jwtAuthenticationPort, userServicePort);

        token = Token.builder()
                .accessToken("mock-token")
                .subject("julian@gmail.com")
                .role("ADMIN")
                .build();
    }

    @Test
    void authorizeShouldReturnTokenWhenPermissionGranted() {
        when(jwtAuthenticationPort.validateToken("Bearer token"))
                .thenReturn(Mono.just(token));

        when(userServicePort.roleHasPermissionToPath(
                eq("ADMIN"),
                eq("/api/loans"),
                eq("GET"),
                eq("Bearer token")
        )).thenReturn(Mono.just(true));

        StepVerifier.create(authUseCase.authorize("Bearer token", "GET", "/api/loans"))
                .expectNextMatches(result ->
                        result.getRole().equals("ADMIN") &&
                                result.getSubject().equals("julian@gmail.com")
                )
                .verifyComplete();
    }

    @Test
    void authorizeShouldThrowForbiddenWhenPermissionDenied() {
        when(jwtAuthenticationPort.validateToken("Bearer token"))
                .thenReturn(Mono.just(token));

        when(userServicePort.roleHasPermissionToPath(
                eq("ADMIN"),
                eq("/api/loans"),
                eq("POST"),
                eq("Bearer token")
        )).thenReturn(Mono.just(false));

        StepVerifier.create(authUseCase.authorize("Bearer token", "POST", "/api/loans"))
                .expectErrorMatches(ex -> ex instanceof CrediyaForbiddenException &&
                        ex.getMessage().equals(ExceptionMessages.DO_NOT_ACCESS_RESOURCE.getMessage()))
                .verify();
    }

    @Test
    void authorizeShouldThrowForbiddenWhenPermissionEmpty() {
        when(jwtAuthenticationPort.validateToken("Bearer token"))
                .thenReturn(Mono.just(token));

        when(userServicePort.roleHasPermissionToPath(
                any(),
                any(),
                any(),
                any()
        )).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.authorize("Bearer token", "PUT", "/api/loans"))
                .expectErrorMatches(ex -> ex instanceof CrediyaForbiddenException &&
                        ex.getMessage().equals(ExceptionMessages.DO_NOT_ACCESS_RESOURCE.getMessage()))
                .verify();
    }

    @Test
    void authorizeShouldPropagateErrorWhenInvalidToken() {
        when(jwtAuthenticationPort.validateToken("invalid"))
                .thenReturn(Mono.error(new RuntimeException("Invalid token")));

        StepVerifier.create(authUseCase.authorize("invalid", "GET", "/api/loans"))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Invalid token"))
                .verify();
    }
}
