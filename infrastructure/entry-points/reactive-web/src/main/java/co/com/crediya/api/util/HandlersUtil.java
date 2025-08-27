package co.com.crediya.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HandlersUtil {

    private HandlersUtil(){}

    public static Map<String, Object> getFieldErrors( Errors errors ) {
        Map<String, Object> fieldErrors = new HashMap<>();
        errors.getFieldErrors().forEach(fieldError -> fieldErrors.put(
                fieldError.getField(),
                String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage())
        ));
        return fieldErrors;

    }

    public static Map<String, Object> buildBodyResponse( Boolean state, Integer statusCode, String keyData, Object data ) {
        return Map.of(
                "success", state,
                "statusCode", statusCode,
                "timestamp", LocalDateTime.now(),
                keyData, data
        );
    }

    public static Errors validateRequestsErrors(Object body, String className, Validator validator) {
        Errors errors = new BeanPropertyBindingResult(body, className);
        validator.validate(body, errors);
        return errors;
    }

    public static Mono<ServerResponse> buildBadRequestResponse(Errors errors) {
        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON).bodyValue(HandlersUtil.buildBodyResponse(
                        false, HttpStatus.BAD_REQUEST.value(), "error", HandlersUtil.getFieldErrors(errors)
                ));
    }


}
