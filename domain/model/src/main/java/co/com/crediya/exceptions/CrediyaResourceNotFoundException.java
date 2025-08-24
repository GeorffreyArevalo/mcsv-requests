package co.com.crediya.exceptions;

import co.com.crediya.enums.ExceptionStatusCode;

public class CrediyaResourceNotFoundException extends CrediyaException {

    public CrediyaResourceNotFoundException(int statusCode, String message) {
        super(ExceptionStatusCode.NOT_FOUND, message);
    }

}
