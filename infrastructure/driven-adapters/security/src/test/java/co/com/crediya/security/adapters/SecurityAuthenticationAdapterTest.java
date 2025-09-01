package co.com.crediya.security.adapters;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

class SecurityAuthenticationAdapterTest {

    private final SecurityAuthenticationAdapter adapter = new SecurityAuthenticationAdapter();

    private Jwt createJwt(String tokenValue, String subject) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);

        return new Jwt(
                tokenValue,
                Instant.now(),
                Instant.now().plusSeconds(3600),
                headers,
                claims
        );
    }

    @Test
    void getCurrentContextTokenWithJwtAuthenticationShouldReturnTokenValue() {
        Jwt jwt = createJwt("test-token-123", "user123");
        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);

        Mono<String> result = adapter.getCurrentContextToken()
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        StepVerifier.create(result)
                .expectNext("test-token-123")
                .verifyComplete();
    }

    @Test
    void getTokenSubjectWithJwtAuthenticationShouldReturnSubject() {
        Jwt jwt = createJwt("token-456", "subject-xyz");
        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);

        Mono<String> result = adapter.getTokenSubject()
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        StepVerifier.create(result)
                .expectNext("subject-xyz")
                .verifyComplete();
    }

    @Test
    void getCurrentContextTokenWithoutAuthenticationShouldReturnEmpty() {

        Mono<String> result = adapter.getCurrentContextToken();

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void getTokenSubjectWithoutAuthenticationShouldReturnEmpty() {

        Mono<String> result = adapter.getTokenSubject();

        StepVerifier.create(result)
                .verifyComplete();
    }
}
