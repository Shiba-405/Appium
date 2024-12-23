package com.ce.libraies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static com.ce.libraies.BaseClass.configProperties;

public class CapabilitiesManager {

    private static final Logger Log = LogManager.getLogger(CapabilitiesManager.class);

    public DesiredCapabilities getCaps(String udid) throws IOException {


        try{
            Log.info("getting capabilities");
            File file = new File(configProperties.getProperty("appium:app"));
            String absolutePath = file.getAbsolutePath();
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("udid", udid);
            cap.setCapability("platformName", configProperties.getProperty("platformName"));
            cap.setCapability("automationName", configProperties.getProperty("automationName"));
            cap.setCapability("uiautomator2ServerInstallTimeout", 300000);
            cap.setCapability("androidInstallTimeout", 900000);
            cap.setCapability("adbExecTimeout", 120000 );
            cap.setCapability("autoGrantPermissions", true);
            cap.setCapability("appium:app", absolutePath);
            cap.setCapability("unicodeKeyboard", true);
            cap.setCapability("resetKeyboard", true);
            cap.setCapability("appWaitDuration", 50000);
            cap.setCapability("uiautomator2ServerLaunchTimeout", 60000); // e.g., 60 seconds

            return cap;
        } catch(Exception e){
            e.printStackTrace();
            Log.fatal("Failed to load capabilities. ABORT!!" + e.toString());
            throw e;
        }
    }
}
