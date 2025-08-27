package co.com.crediya.api.util;

import org.springframework.validation.Errors;

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


}
