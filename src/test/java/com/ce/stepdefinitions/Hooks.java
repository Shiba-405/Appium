package com.ce.stepdefinitions;

import com.ce.libraies.DriverManager;
import com.ce.libraies.PageObjectManager;
import com.ce.libraies.FLUtilities;
import com.ce.libraies.TestContext;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.*;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class Hooks extends FLUtilities {
    private TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
    }

    @After
    public void quit(Scenario scenario) throws IOException {
        if (scenario.isFailed()) {
            byte[] screenshot = new DriverManager().getDriver().getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
    }
}