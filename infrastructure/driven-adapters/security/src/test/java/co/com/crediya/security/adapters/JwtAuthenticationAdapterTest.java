package co.com.crediya.security.adapters;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.security.enums.SecurityConstants;
import co.com.crediya.security.util.KeysUtil;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static org.mockito.Mockito.*;

class JwtAuthenticationAdapterTest {

    private KeysUtil keysUtil;
    private JwtAuthenticationAdapter jwtAuthenticationAdapter;
    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws Exception {
        keysUtil = mock(KeysUtil.class);
        jwtAuthenticationAdapter = new JwtAuthenticationAdapter(keysUtil);


        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        keyPair = keyGen.generateKeyPair();
    }

    @Test
    void validateTokenShouldReturnTokenWhenValid() {

        String jwt = Jwts.builder()
                .subject("user@example.com")
                .claim("role", "ADMIN")
                .claim("permissions", List.of("READ", "WRITE"))
                .signWith(keyPair.getPrivate())
                .compact();

        String bearerToken = SecurityConstants.TYPE_TOKEN_START.getValue() + jwt;

        when(keysUtil.loadPublicKey()).thenReturn(Mono.just((RSAPublicKey) keyPair.getPublic()));

        StepVerifier.create(jwtAuthenticationAdapter.validateToken(bearerToken))
                .expectNextMatches(token ->
                        token.getSubject().equals("user@example.com") &&
                                token.getRole().equals("ADMIN") &&
                                token.getPermissions().containsAll(List.of("READ", "WRITE")) &&
                                token.getAccessToken().equals(bearerToken)
                )
                .verifyComplete();
    }

    @Test
    void validateTokenShouldThrowExceptionWhenJwtParsingFails() {
        String bearerToken = SecurityConstants.TYPE_TOKEN_START.getValue() + "malformed.jwt.token";

        when(keysUtil.loadPublicKey()).thenReturn(Mono.just((RSAPublicKey)keyPair.getPublic()));

        StepVerifier.create(jwtAuthenticationAdapter.validateToken(bearerToken))
                .expectErrorMatches(ex -> ex instanceof CrediyaUnathorizedException &&
                        ex.getMessage().equals(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage()))
                .verify();
    }
}
