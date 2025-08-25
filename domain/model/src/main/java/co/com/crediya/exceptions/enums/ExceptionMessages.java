package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    FIELD_AMOUNT_INVALID("Field amount is not valid."),
    FIELD_DEADLINE_INVALID("Field deadline must be greater than the current one."),
    FIELD_USER_DOCUMENT_REQUIRED("Field user document is required."),
    USER_WITH_DOCUMENT_NOT_FOUND("User with document %s not found."),
    TYPE_LOAN_WITH_CODE_NOT_FOUND("Type loan with code %s not found.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }



}
