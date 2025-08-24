package co.com.crediya.exceptions;

import co.com.crediya.enums.ExceptionStatusCode;

public class CrediyaIllegalArgumentException extends CrediyaException {

    public CrediyaIllegalArgumentException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message);
    }

}
