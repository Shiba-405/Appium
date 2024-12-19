package com.ce.libraies;

import lombok.Getter;

@Getter
public enum EnumsCommon {
    ABSOLUTE_CLIENTFILES_PATH(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Client\\"),
    ABSOLUTE_FILES_PATH(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\"),
    REUSABLE_FILES_PATH(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\ReusableMethods\\"),
    FEATUREFILESPATH(System.getProperty("user.dir") + "/src/test/resources/features/"),
    RUNNERFILESPATH(System.getProperty("user.dir") + "/src/test/java/com/ce/runner/");
    private final String text;
    EnumsCommon(String text) {
        this.text = text;
    }

}
