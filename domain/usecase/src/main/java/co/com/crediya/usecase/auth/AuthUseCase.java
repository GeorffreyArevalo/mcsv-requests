package co.com.crediya.usecase.auth;

import co.com.crediya.exceptions.CrediyaForbiddenException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Token;
import co.com.crediya.port.consumers.UserServicePort;
import co.com.crediya.port.token.JwtAuthenticationPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {

    private final JwtAuthenticationPort  jwtAuthenticationPort;
    private final UserServicePort userServicePort;

    public Mono<Token> authorize(String token, String method, String path) {

        return jwtAuthenticationPort
                .validateToken(token)
                .flatMap( modelToken ->
                        userServicePort.roleHasPermissionToPath( modelToken.getRole(), path, method, token )
                                .filter( Boolean::booleanValue )
                                .map( exists -> modelToken )
                )
                .switchIfEmpty(
                        Mono.error( new CrediyaForbiddenException(ExceptionMessages.DO_NOT_ACCESS_RESOURCE.getMessage()))
                );


    }


}
