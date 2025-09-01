package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class CrediyaForbiddenException extends CrediyaException {

    public CrediyaForbiddenException(String message) {
        super( ExceptionStatusCode.FORBIDDEN, message, 403 );
    }

}
