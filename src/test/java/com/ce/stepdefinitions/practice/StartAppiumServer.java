package com.ce.stepdefinitions.practice;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class StartAppiumServer {
    public static AppiumDriverLocalService startServer(int port, String udid) {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("udid", udid);

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(port)
                .withCapabilities(cap)
                .withLogFile(new File("AppiumLog_" + port + ".txt"));

        return AppiumDriverLocalService.buildService(builder);
    }

    public static void main(String[] args) {
        AppiumDriverLocalService appiumServer1 = startServer(4723, "emulator-5554");
        AppiumDriverLocalService appiumServer2 = startServer(4725, "emulator-5556");

        appiumServer1.start();
        appiumServer2.start();
    }
}
