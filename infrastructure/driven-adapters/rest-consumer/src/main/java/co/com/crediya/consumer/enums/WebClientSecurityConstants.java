package co.com.crediya.consumer.enums;

import lombok.Getter;

@Getter
public enum WebClientSecurityConstants {

    TYPE_TOKEN("Bearer ");

    private final String value;
    WebClientSecurityConstants(String value) {
        this.value = value;
    }

}
