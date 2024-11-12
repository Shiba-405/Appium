package com.ce.libraies;

import com.ce.pages.E2EFlowDataPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class PageObjectManager extends BaseClass {
    private static final Logger Log = LogManager.getLogger(PageObjectManager.class);
    private final WebDriver driver;
    private E2EFlowDataPage onE2EFlowDataPage;
    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public E2EFlowDataPage getE2EFlowDataPage() {
        try {
        //    return (onE2EFlowDataPage == null) ? onE2EFlowDataPage = new E2EFlowDataPage(driver) : onE2EFlowDataPage;
            return (onE2EFlowDataPage == null) ? onE2EFlowDataPage = new E2EFlowDataPage() : onE2EFlowDataPage;
        } catch (Exception e) {
            Log.error("Instance creations of E2EFlowDataPage Failed ", e);
            throw new FLException("Instance creations of E2EFlowDataPage Failed " + e.getMessage());
        }
    }


}
