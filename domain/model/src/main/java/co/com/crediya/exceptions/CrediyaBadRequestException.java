package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class CrediyaBadRequestException extends CrediyaException {

    public CrediyaBadRequestException(String message) {
        super( ExceptionStatusCode.BAD_REQUEST, message, 400 );
    }


}
