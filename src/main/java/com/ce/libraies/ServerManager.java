package com.ce.libraies;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

public class ServerManager {
    private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();
    private static final Logger Log = LogManager.getLogger(ServerManager.class);

    public AppiumDriverLocalService getServer(){
        return server.get();
    }

    public void startServer(){
        Log.info("starting appium server");
        AppiumDriverLocalService server = null;

        server = new AppiumServiceBuilder().usingAnyFreePort().build();
       try{ server.start();}
       catch (Exception e){
           server.start();
       }
        if(server == null || !server.isRunning()){
           Log.fatal("Appium server not started. ABORT!!!");
            throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
        }
        server.clearOutPutStreams();
        this.server.set(server);
        Log.info("Appium server started");
    }

    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }


}
