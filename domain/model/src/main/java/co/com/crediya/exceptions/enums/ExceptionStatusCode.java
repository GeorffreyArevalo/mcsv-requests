package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionStatusCode {

    BAD_REQUEST("400-BD"),
    FIELDS_BAD_REQUEST("400-BD-FIELDS"),
    NOT_FOUND("404-NF"),
    CREATED("201-CR"),
    INTERNAL_SERVER_ERROR("500-ISE"),
    FORBIDDEN("403-FB"),
    UNAUTHORIZED_INVALID_TOKEN("401-UA-IT"),
    OK("200-OK");

    private final String statusCode;

    ExceptionStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String status() {
        return statusCode;
    }

}
