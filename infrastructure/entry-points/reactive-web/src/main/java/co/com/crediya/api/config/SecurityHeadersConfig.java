package co.com.crediya.api.config;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.usecase.auth.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityHeadersConfig implements WebFilter {

    private final AuthUseCase authUseCase;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = exchange.getResponse().getHeaders();
        headers.set("Content-Security-Policy", "default-src 'self'; frame-ancestors 'self'; form-action 'self'");
        headers.set("Strict-Transport-Security", "max-age=31536000;");
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("Server", "");
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Referrer-Policy", "strict-origin-when-cross-origin");

        return Mono.just( request.getPath().value() )
                .log()
            .filter( path -> !path.contains("/openapi/") && !path.equals("/actuator/health") )
            .flatMap( path ->
                Mono.justOrEmpty( request.getHeaders().getFirst( HttpHeaders.AUTHORIZATION ) )
                .switchIfEmpty( Mono.error( new CrediyaUnathorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage())) )
                .flatMap( bearerToken -> authUseCase.authorize(
                        bearerToken,
                        request.getMethod().name(),
                        path
                ))
                .flatMap( token -> chain.filter(exchange).contextWrite( ctx ->
                        ctx.put("token", token)
                ))
            ).switchIfEmpty(
                chain.filter(exchange)
            );
    }

}
