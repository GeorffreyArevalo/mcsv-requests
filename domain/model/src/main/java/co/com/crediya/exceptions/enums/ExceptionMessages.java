package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    WEB_CLIENT_INTERNAL_SERVER_ERROR("Internal server error in user server."),
    CREATE_LOAN_FORBIDDEN("Can't create a loan for other client."),
    INTERNAL_SERVER_ERROR_GET_TOKEN("An error occurred while getting the token."),
    DO_NOT_ACCESS_RESOURCE("Doesn't have access to this resource."),
    UNAUTHORIZED_SENT_TOKEN_INVALID("Doesn't access - Unauthorized Sent Token."),
    STATE_LOAN_WITH_CODE_NOT_FOUND("State loan with code %s not found."),
    TYPE_LOAN_WITH_CODE_NOT_FOUND("Type loan with code %s not found.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }



}
