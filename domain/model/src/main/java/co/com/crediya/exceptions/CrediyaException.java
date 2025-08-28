package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import lombok.Getter;

@Getter
public class CrediyaException extends RuntimeException {

    private final ExceptionStatusCode statusCode;
    private final int status;

    public CrediyaException(ExceptionStatusCode statusCode, String message, int status ) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }

}
