package co.com.crediya.api.exception.handler;

import co.com.crediya.api.dtos.CrediyaResponseDTO;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.exceptions.CrediyaException;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    private Mono<Void> buildFailureResponse(ServerWebExchange exchange, HttpStatus status, CrediyaResponseDTO<?> error) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(error))
                .map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes))
                .flatMap(buffer -> exchange.getResponse().writeWith(Mono.just(buffer)))
                .onErrorResume(e -> {
                    log.error("Internal ServerError", e);
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                });
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.just(exchange.getResponse())
            .map(response -> {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response;
            }).flatMap(response -> {
                if (ex instanceof CrediyaException crediyaException) {
                    log.warn("Crediya Exception: {}", crediyaException.getMessage());
                    return buildFailureResponse(exchange, HttpStatus.resolve(crediyaException.getStatus()), HandlersResponseUtil.buildBodyFailureResponse(
                            crediyaException.getStatusCode().status(), crediyaException.getMessage(), null
                    ));
                }
                if (ex instanceof ConstraintViolationException fieldValidationException) {
                    log.warn("Fields invalid Exception: {}", fieldValidationException.getMessage());
                    return toListErrors(fieldValidationException.getConstraintViolations())
                    .flatMap(fieldErrors -> buildFailureResponse(exchange, HttpStatus.BAD_REQUEST, HandlersResponseUtil.buildBodyFailureResponse(
                                ExceptionStatusCode.FIELDS_BAD_REQUEST.status(), "Request invalid fields", fieldErrors
                        )));
                }
                log.error("Internal Server Error", ex);
                return buildFailureResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR,
                        HandlersResponseUtil.buildBodyFailureResponse(ExceptionStatusCode.INTERNAL_SERVER_ERROR.status(), "Internal Server Error", null)
                );
            });
    }
    private Mono<List<String>> toListErrors(Set<ConstraintViolation<?>> violations) {
        return Flux.fromIterable(violations)
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collectList();
    }
}
