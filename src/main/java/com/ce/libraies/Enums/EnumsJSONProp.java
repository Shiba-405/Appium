package com.ce.libraies.Enums;

import lombok.Getter;

@Getter
public enum EnumsJSONProp {
    PRODUCT("product"),
    NEWPRODUCTNAME("newProductName");

    private final String text;

    EnumsJSONProp(String text) {
        this.text = text;
    }

}