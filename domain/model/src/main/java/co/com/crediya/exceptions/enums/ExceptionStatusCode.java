package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionStatusCode {

    BAD_REQUEST(400),
    NOT_FOUND(404);

    private final int statusCode;

    ExceptionStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
