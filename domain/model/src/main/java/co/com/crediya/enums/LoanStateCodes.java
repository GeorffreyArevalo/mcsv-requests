package co.com.crediya.enums;

import lombok.Getter;

@Getter
public enum LoanStateCodes {

    PENDING_REVIEW("PENDIENTE_DE_REVISION");

    private final String status;

    LoanStateCodes(String status) {
        this.status = status;
    }

}
