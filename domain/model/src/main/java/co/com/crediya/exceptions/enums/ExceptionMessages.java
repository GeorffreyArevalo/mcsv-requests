package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    WBE_CLIENT_INTERNAL_SERVER_ERROR("Internal server error in user server."),
    TYPE_LOAN_WITH_CODE_NOT_FOUND("Type loan with code %s not found.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }



}
