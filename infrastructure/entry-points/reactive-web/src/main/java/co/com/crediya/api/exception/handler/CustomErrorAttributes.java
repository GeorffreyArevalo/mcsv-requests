package co.com.crediya.api.exception.handler;

import co.com.crediya.api.exception.model.CustomError;
import co.com.crediya.exceptions.CrediyaException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);

        if( !(error instanceof CrediyaException customException) ) {
            return super.getErrorAttributes(request, options);
        }
        Map<String, Object> errorResponse = new HashMap<>();
        CustomError customError = CustomError.builder()
                .statusCode(customException.getStatusCode().getStatusCode())
                .message(customException.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        errorResponse.put("error", customError);
        return  errorResponse;



    }
}
