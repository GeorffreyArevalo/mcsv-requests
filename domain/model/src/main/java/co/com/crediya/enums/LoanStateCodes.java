package co.com.crediya.enums;

import lombok.Getter;

@Getter
public enum LoanStateCodes {

    APPLICATION_RECEIVED("SOLICITUD_RECIBIDA"),
    PENDING_REVIEW("PENDIENTE_DE_REVISION"),
    UNDER_REVIEW("EN_ESTUDIO"),
    APPROVED("APROBADA"),
    REJECTED("RECHAZADA"),
    PENDING_SIGNATURE("EN_FIRMA"),
    CANCELED("CANCELADA"),
    DISBURSED("DESEMBOLSADA");

    private final String status;

    LoanStateCodes(String status) {
        this.status = status;
    }

}
