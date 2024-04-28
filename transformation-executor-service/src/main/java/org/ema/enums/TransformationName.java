package org.ema.enums;

import lombok.Getter;

@Getter
public enum TransformationName {
    CAPITALIZE_FILE("CAPITALIZE_FILE");

    private final String value;

    TransformationName(String value) {
        this.value = value;
    }

}
