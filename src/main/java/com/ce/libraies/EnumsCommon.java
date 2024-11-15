package com.ce.libraies;

import lombok.Getter;

@Getter
public enum EnumsCommon {
    ABSOLUTE_CLIENTFILES_PATH(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Client\\"),
    FIELD("Common Tag"),
    JURISDICTION("Jurisdiction"),
    E2ETITLE("Title"),
    E2ETESTDATA("Test Data"),
    TOVISIBLE("ToVisible"),
    TOCLICKABLE("ToClickable"),
    TOINVISIBLE("ToInvisible"),
    ABSOLUTE_FILES_PATH(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\"),
    RESPONSE_FILES_PATH(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Responses\\"),
    REUSABLE_FILES_PATH(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\ReusableMethods\\"),
    FIREFOXBROWSER("Firefox"),
    FEATUREFILESPATH(System.getProperty("user.dir") + "/src/test/resources/features/"),
    RUNNERFILESPATH(System.getProperty("user.dir") + "/src/test/java/com/ce/runner/");

    private final String text;

    EnumsCommon(String text) {
        this.text = text;
    }

}
