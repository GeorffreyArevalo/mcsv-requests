package co.com.crediya.enums;

public enum ExceptionMessages {

    FIELD_NAME_REQUIRED("Field name is required"),
    FIELD_LAST_NAME_REQUIRED("Field lastName is required"),
    FIELD_EMAIL_NOT_VALID("Field email is not valid"),
    FIELD_PAYMENT_OUT_RANGE("Filed basePayment is out of range"),
    FIELD_DOCUMENT_MUST_BE_ONLY_NUMBERS("Document must be only numbers"),
    USER_WITH_EMAIL_EXIST("User with email %s already exists"),
    USER_WITH_DOCUMENT_EXIST("User with document %s already exists");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
