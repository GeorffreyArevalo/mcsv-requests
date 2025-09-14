package co.com.crediya.enums;

import lombok.Getter;

@Getter
public enum MessagesConstants {

    MESSAGE_INCREASE_REPORTS_APPROVED("count_approved_reports");

    private final String value;

    MessagesConstants(String value) {
        this.value = value;
    }
}
