package co.com.crediya.security.adapters;


import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Token;
import co.com.crediya.port.token.JwtAuthenticationPort;
import co.com.crediya.security.enums.SecurityConstants;
import co.com.crediya.security.util.KeysUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationAdapter implements JwtAuthenticationPort {

    private final KeysUtil keysUtil;

    public Mono<Token> validateToken(String token) {

        return Mono.just(token)
            .filter( bearerToken -> bearerToken.startsWith(SecurityConstants.TYPE_TOKEN_START.getValue()) )
            .switchIfEmpty( Mono.error(new CrediyaUnathorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage())) )
            .map( bearerToken -> bearerToken.replaceAll(SecurityConstants.TYPE_TOKEN_START.getValue(), ""))
            .zipWith( keysUtil.loadPublicKey(), ( bearerToken, publicKey ) ->
                Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(bearerToken)
                    .getPayload()
            ).map( claims ->
                Token.builder()
                    .role(claims.get("role", String.class))
                    .permissions(getPermissions(claims))
                    .accessToken(token)
                    .subject(claims.getSubject())
                    .build()
            ).onErrorResume( error ->
                Mono.error(new CrediyaUnathorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage()))
            );

    }

    private List<String> getPermissions(Claims claims) {
        return ((List<?>) claims.get("permissions"))
            .stream()
            .map(Object::toString)
            .toList();
    }



}
