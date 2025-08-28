package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class CrediyaResourceNotFoundException extends CrediyaException {

    public CrediyaResourceNotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }

}
