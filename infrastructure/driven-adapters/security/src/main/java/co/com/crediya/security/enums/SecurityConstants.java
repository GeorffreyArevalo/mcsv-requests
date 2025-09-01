package co.com.crediya.security.enums;

import lombok.Getter;

@Getter
public enum SecurityConstants {

    TYPE_ALGORITHM("RSA"),
    REGEX_START_END_KEY("-----\\w+ PUBLIC KEY-----"),
    REGEX_SPACES("\\s");

    private final String value;
    SecurityConstants(String value) {
        this.value = value;
    }

}
