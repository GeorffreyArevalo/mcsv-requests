package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class CrediyaInternalServerErrorException  extends CrediyaException{

    public CrediyaInternalServerErrorException(String message) {
        super( ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500 );
    }

}
