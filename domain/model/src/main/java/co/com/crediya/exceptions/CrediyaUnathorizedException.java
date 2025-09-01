package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class CrediyaUnathorizedException extends CrediyaException {

    public CrediyaUnathorizedException(String message) {
        super( ExceptionStatusCode.UNAUTHORIZED_INVALID_TOKEN, message, 401 );
    }

}
