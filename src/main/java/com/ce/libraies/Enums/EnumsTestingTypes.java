package com.ce.libraies.Enums;

import lombok.Getter;

@Getter
public enum EnumsTestingTypes {
    ENUMSTESTINGTYPES("UI");
    private final String text;

    EnumsTestingTypes(String text) {
        this.text = text;
    }

}