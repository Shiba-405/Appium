package com.ce.libraies;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class DriverManager {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static final Logger Log = LogManager.getLogger(DriverManager.class);

    public AppiumDriver getDriver(){
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2){
        driver.set(driver2);
    }

    public void initializeDriver(String udid) throws Exception {
        AppiumDriver driver = null;

        if(driver == null){
            try{
                Log.info("initializing Appium driver");
                System.out.println("initializing Appium driver");


                       // driver = new AndroidDriver(new ServerManager().getServer().getUrl(), new CapabilitiesManager().getCaps());
                        driver = new AndroidDriver(new ServerManager().getServer().getUrl(), new CapabilitiesManager().getCaps(udid));


                if(driver == null){
                    throw new Exception("driver is null. ABORT!!!");
                }
                Log.info("Driver is initialized");
                this.driver.set(driver);
            } catch (IOException e) {
                e.printStackTrace();
                Log.fatal("Driver initialization failure. ABORT !!!!" + e.toString());
                throw e;
            }
        }

    }

}
