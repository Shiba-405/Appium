package com.ce.libraies;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class BaseClass {

    private AppiumDriver driver = null;
    private static final Logger Log = LogManager.getLogger(BaseClass.class);
    protected static Properties configProperties = null;
    WebDriverWait wait;

    /**
     * This method reads config.properties file and to set different variables at global level in the Framework.
     *
     * @param testContext The TestContext class reference
     */
    public void loadConfigData(TestContext testContext) {
        readConfigFile();
        //  setEnvironment(testContext);
    }

    /**
     * @param testContext The TestContext class reference
     * @return The WebDriver instance
     * <p>
     * Browser: Value for Browser shall be read from TestContext class. To execute scripts on local system, value for 'execution.type' should be
     * 'local' in Config.properties file. During CI-CD, execution shall happen on Virtual Machine and name for Virtual Machine shall be picked from
     * ADO Pipeline
     * <p>
     * Implicit Wait: Value for Implicit Wait can be changed from config.properties file.
     */
    public AppiumDriver getMobDriver(TestContext testContext,String udid) {
        try {
            openMobileApp(udid);

        } catch (Exception e) {
            Log.error("Loading WebDriver failed ", e);
            throw new FLException("Loading WebDriver failed >>>> " + e.getMessage());
        }

        Log.info("Driver Loaded Successfully.");
        System.out.println("Driver Loaded Successfully.");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("implicit_wait"))));
        Log.info("Implicit Wait Set as {} Seconds", configProperties.getProperty("implicit_wait"));
        testContext.setWait(new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("implicit_wait")))));
        return driver;
    }

    /**
     * This method reads config.properties file and values shall be saved in key-value pair in 'configProperties' object
     * To retrieve the value user needs to be passed valid key. To view all keys please refer 'config.properties' file.
     */
    private void readConfigFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/test/resources/other/config.properties"))) {
            configProperties = new Properties();
            configProperties.load(reader);
            reader.close();
            Log.info("Read Properties File Successfully");
        } catch (FileNotFoundException e) {
            Log.error("Properties File Could Not Find ", e);
            throw new FLException("Properties File Could Not Find" + e.getMessage());
        } catch (IOException e) {
            Log.error("Reading Properties File Failed ", e);
            throw new FLException("Reading Properties File Failed " + e.getMessage());
        }
    }

    /**
     * @return ChromeOptions object having different Chrome Browser properties to manipulate.
     */
    private ChromeOptions getChromeOptions() {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("autofill.profile_enabled", false);
        preferences.put("download.prompt_for_download", false);
        preferences.put("download.extensions_to_open", "applications/pdf");
        preferences.put("plugins.plugins_disabled", "Chrome PDF Viewer");
        preferences.put("plugins.always_open_pdf_externally", true);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", preferences);
        if (Boolean.parseBoolean(configProperties.getProperty("headlessExecution.switch")))
            chromeOptions.addArguments("headless", "--disable-gpu", "--window-size=1920,1080", "--ignore-certificate-errors");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--remote-allow-origins=*");

        return chromeOptions;
    }

    /**
     * @return FirefoxOptions object having different Firefox Browser properties to manipulate.
     */
    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (Boolean.parseBoolean(configProperties.getProperty("headlessExecution.switch")))
            options.addArguments("--headless");

        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
        profile.setPreference("browser.download.viewableInternally.enabledTypes", "");
        profile.setPreference("dom.webnotifications.enabled", false);
        profile.setPreference("print.always_print_silent", true);
        profile.setPreference("browser.download.alwaysOpenPanel", false);
        profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
        profile.setPreference("pdfjs.disabled", true);

        options.setProfile(profile);

        return options;
    }

    /**
     * @return EdgeOptions object having different Edge Browser properties to manipulate.
     */
    protected EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        options.setCapability("disable-popup-blocking", true);
        options.setCapability("browser.show_hub_popup_on_download_start", true);
        options.setCapability("download.prompt_for_download", false);
        options.setCapability("plugins.always_open_pdf_externally", true);
        options.setCapability("download.extensions_to_open", "applications/pdf");
        options.setCapability("download.prompt_for_download", false);

        return options;
    }

    /**
     * This method opens up Firelight Application landing page either in QA or QANext environment.
     *
     * @param driver      The WebDriver instance
     * @param testContext The TestContext class reference
     */
    protected void openLoginPage(WebDriver driver, TestContext testContext) {

        String url = configProperties.getProperty("QA.url");
        System.out.println("URL = " + url);

        if (configProperties.getProperty("browser").equalsIgnoreCase("Edge")) {
            driver.get("edge://settings/content/pdfDocuments");

            // Execute JavaScript to enable the option
            ((JavascriptExecutor) driver).executeScript("document.querySelector('input[type=\"checkbox\"]').click();");
        }
        driver.get(url);
    }

    /**
     * Test Data is stored in JSON object with key-value pair. To retrieve test data/JSON object, user needs to pass current test case ID
     * from feature file under Given step, e.g. "End2End_TC_02". Once specific test data for current test case ID is fetched then it will be stored
     * in HashMap, further to fetch test data from HashMap, user needs to pass valid key.
     *
     * @param testCaseID  Current Test-Case ID
     * @param testContext The TestContext class reference
     * @return TestData for current Test-Case ID in key-value format of HashMap.
     */
    protected HashMap<String, String> getTestData(String testCaseID, TestContext testContext) {
        HashMap<String, String> testData = new HashMap<>();
        try {
            String testDataFile = configProperties.getProperty("End2End.testdata.filePath");
            System.out.println("TestDataFile = " + testDataFile);
            JSONObject parentJsonObject = (JSONObject) readJsonObjectFromFile(testDataFile).get("testData");

            copyJsonObjectValuesToMap((JSONObject) parentJsonObject.get(testCaseID), testData);

            Log.info("Test Data Read Successfully");
        } catch (IOException e) {
            Log.error("Could Not Read JSON File ", e);
            throw new FLException("Could Not Read JSON File" + e.getMessage());
        } catch (ParseException e) {
            Log.error("Could Not Parse JSON File ", e);
            throw new FLException("Could Not Parse JSON File " + e.getMessage());
        } catch (Exception e) {
            Log.error("Mapping Test Data Failed ", e);
            throw new FLException("Mapping Test Data Failed " + e.getMessage());
        }

        return testData;
    }

    private JSONObject readJsonObjectFromFile(String testDataFile) throws IOException, ParseException {
        Object object = new JSONParser().parse(new FileReader(testDataFile));
        return (JSONObject) object;
    }

    private void copyJsonObjectValuesToMap(JSONObject jsonObject, Map<String, String> map) {
        jsonObject.forEach((key, value) -> map.put(key.toString(), value.toString()));
    }

    /**
     * This method adds new property in test data/JSON object during execution.
     *
     * @param testCaseID  Current Test-Case ID
     * @param testContext The TestContext class reference
     * @param property    New property in test data/JSON object
     * @param value       Value for newly added property
     * @return Update test Data after adding new property for current Test-Case ID in key-value format of HashMap.
     */
    protected HashMap<String, String> addPropertyValueInJSON(String testCaseID, TestContext testContext, String property, String value) {
        HashMap<String, String> testData = new HashMap<>();
        try {
            String JSONFileToUpdate = configProperties.getProperty("End2End.testdata.filePath");
            Object object = new JSONParser().parse(new FileReader(JSONFileToUpdate));
            JSONObject entireJsonObject = (JSONObject) object; //converting into JSON Object
            JSONObject parentJsonObject = (JSONObject) entireJsonObject.get("testData");

            JSONObject currentJSONObjectToWrite = (JSONObject) parentJsonObject.get(testCaseID);
            currentJSONObjectToWrite.put(property, value);

            FileWriter file = new FileWriter(JSONFileToUpdate);
            file.write(entireJsonObject.toJSONString());
            file.close();

            testContext.setMapTestData(getTestData(testContext.getTestCaseID(), testContext));
            Log.info("New Property Value added Successfully");
        } catch (IOException e) {
            Log.error("Could Not Read JSON File ", e);
            throw new FLException("Could Not Read JSON File" + e.getMessage());
        } catch (ParseException e) {
            Log.error("Could Not Parse JSON File ", e);
            throw new FLException("Could Not Parse JSON File " + e.getMessage());
        } catch (Exception e) {
            Log.error("Adding Property Value in JSON File Failed ", e);
            throw new FLException("Mapping Test Data Failed " + e.getMessage());
        }

        return testData;
    }


    /**
     * This method adds new property in test data/JSON object during execution.
     *
     * @param testCaseID  Current Test-Case ID
     * @param testContext The TestContext class reference
     * @param property    New property in test data/JSON object
     * @param value       Value for newly added property
     * @return Update test Data after adding new property for current Test-Case ID in key-value format of HashMap.
     */
    protected HashMap<String, String> addPropertyValueInJSON(String testCaseID, TestContext testContext, String property, HashMap<String, String> value) {
        HashMap<String, String> testData = new HashMap<>();
        try {
            String JSONFileToUpdate = configProperties.getProperty("End2End.testdata.filePath");
            Object object = new JSONParser().parse(new FileReader(JSONFileToUpdate));
            JSONObject entireJsonObject = (JSONObject) object; //converting into JSON Object
            JSONObject parentJsonObject = (JSONObject) entireJsonObject.get("testData");

            JSONObject currentJSONObjectToWrite = (JSONObject) parentJsonObject.get(testCaseID);
            currentJSONObjectToWrite.put(property, value);

            FileWriter file = new FileWriter(JSONFileToUpdate);
            file.write(entireJsonObject.toJSONString());
            file.close();

            testContext.setMapTestData(getTestData(testContext.getTestCaseID(), testContext));
            Log.info("New Property Value added Successfully");
        } catch (IOException e) {
            Log.error("Could Not Read JSON File ", e);
            throw new FLException("Could Not Read JSON File" + e.getMessage());
        } catch (ParseException e) {
            Log.error("Could Not Parse JSON File ", e);
            throw new FLException("Could Not Parse JSON File " + e.getMessage());
        } catch (Exception e) {
            Log.error("Adding Property Value in JSON File Failed ", e);
            throw new FLException("Mapping Test Data Failed " + e.getMessage());
        }

        return testData;
    }

    /**
     * captureScreenshot.switch with value either true or false has been added. The value can be updated either from config.properties file or
     * ADO pipeline. If set as true, then ONLY screenshots shall be captured.
     *
     * @param driver           The WebDriver reference
     * @param testContext      The TestContext class reference
     * @param appendTestStatus if parameter passed as true, then Test Case Status as Passed or Failed shall be appended to screenshot file name
     *                         along with Timestamp. false should be passed to capture the screenshot at step level wherein Test Case Status
     *                         is not required to append.
     */
    public void captureScreenshot(WebDriver driver, TestContext testContext, boolean appendTestStatus) {
        if (testContext.getBrowser().equalsIgnoreCase(EnumsCommon.FIREFOXBROWSER.getText())) {
            waitForPageToLoad(driver);
        }

        if (!Boolean.parseBoolean(testContext.getCaptureScreenshot()))
            return;

        try {
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String tcStatus = "";
            if (appendTestStatus) {
                tcStatus = "FAILED";
                if (!testContext.getScenario().isFailed()) {
                    tcStatus = "PASSED";
                }
            }

            String fileName = truncateScenarioName(testContext.getScenario().getName()) + "_" + getDate("forFileName") + "_" + tcStatus;
            File destination = new File(configProperties.get("screenshotFolder.path") + testContext.getScreenshotFolderName() + "/" + fileName + ".png");
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            Log.error("Copy File From Source To Destination Failed ", e);
            throw new FLException("Copy File From Source To Destination Failed " + e.getMessage());
        } catch (Exception e) {
            Log.error("Screenshot Capture Failed ", e);
            throw new FLException("Screenshot Capture Failed " + e.getMessage());
        }
    }

    /**
     * This method truncates scenario name having long Scenario name in the feature file. Scenario name will be given to file name of screenshots
     * captured. Windows allows only 232 characters for file name, so truncate scenario name is required.
     *
     * @param scenarioName Name of the Scenario
     * @return Truncated scenario name
     */
    private String truncateScenarioName(String scenarioName) {
        return scenarioName.length() > 200 ? scenarioName.substring(0, 200) : scenarioName;
    }

    /**
     * This method returns Date in the specified format.
     *
     * @param dateForWhat Specific format to form the Date
     * @return Date in the specified format
     */
    protected String getDate(String dateForWhat) {
        try {
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat dateFormat;

            switch (dateForWhat) {
                case "newAppName":
                    dateFormat = new SimpleDateFormat("ddMMssSSS");
                    break;

                case "forFileName":
                    dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ssSSS");
                    break;

                case "pdfFileName":
                    dateFormat = new SimpleDateFormat("dd_MM_mm_ssSSS");
                    break;

                case "forFolderName":
                    dateFormat = new SimpleDateFormat("dd_MM_yyyy");
                    break;

                case "forPDFVerification":
                    dateFormat = new SimpleDateFormat("M/dd/yyyy");
                    break;

                default:
                    throw new FLException("Invalid value for: " + dateForWhat);
            }

            dateFormat.setCalendar(calendar);

            return dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            Log.error("Getting Date Format Failed ", e);
            throw new FLException("Getting Date Format Failed " + e.getMessage());
        }
    }

    /**
     * This method closes the browser instance at the end of test case execution. Also, it calls captureScreenshot method to capture the screenshot
     * with test case execution status either Passed or Failed with timestamp.
     *
     * @param testContext The TestContext class reference
     */
    protected void closeBrowser(TestContext testContext) {
        if (testContext.getScenario().isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            testContext.getScenario().attach(screenshot, "image/png", "Screenshot on Failure");
            Log.info("TEST CASE {} is FAILED", testContext.getTestCaseID());
        } else {
            if (testContext.getTestCaseID() != null)
                Log.info("TEST CASE {} is PASSED", testContext.getTestCaseID());
        }

        // captureScreenshot(driver, testContext, true);
        driver.quit();
        System.out.println("Driver Quit Successfully");
    }

    /**
     * This method is waits till the webpage is completely loaded
     *
     * @param driver The WebDriver reference
     */
    public static void waitForPageToLoad(WebDriver driver) {
        String loaderClassName = "ITSpinner";
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(60)) // Maximum wait time
                .pollingEvery(Duration.ofMillis(100)) // Polling interval
                .ignoring(Exception.class);

        wait.until(webdriver -> {
            Long loaderCount = (Long) ((JavascriptExecutor) driver).executeScript(
                    "return document.getElementsByClassName('" + loaderClassName + "').length;");
            return loaderCount == 0;
        });
    }

    protected boolean isAttributePresent(WebElement element, String attribute) {
        try {
            String value = element.getAttribute(attribute);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void openMobileApp(String udid) {
        DesiredCapabilities cap = getDesiredCapabilities(udid);

        AppiumDriverLocalService service = new AppiumServiceBuilder().usingAnyFreePort().build();
        service.start();
        new TestContext().setService(service);
        AndroidDriver androidDriver = new AndroidDriver(service.getUrl(), cap);
        if (androidDriver.isDeviceLocked()) {
            androidDriver.unlockDevice();
        }
        driver = androidDriver;
        new TestContext().setMobDriver(driver);

        System.out.println("driver not null");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("implicit_wait"))));
        System.out.println("Application Started");
    }

    private static DesiredCapabilities getDesiredCapabilities(String udid) {
        TestContext testContext = new TestContext();
        File file = new File(configProperties.getProperty("appium:app"));
        String absolutePath = file.getAbsolutePath();
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("udid", udid);
        cap.setCapability("platformName", configProperties.getProperty("platformName"));
        cap.setCapability("automationName", configProperties.getProperty("automationName"));
        cap.setCapability("uiautomator2ServerInstallTimeout", 10000);
        cap.setCapability("adbExecTimeout", 120000 /*Integer.parseInt(configProperties.getProperty("adbExecTimeout"))*/);
        cap.setCapability("appium:app", absolutePath);
        cap.setCapability("unicodeKeyboard", true);
        cap.setCapability("resetKeyboard", true);
        return cap;
    }

    private static DesiredCapabilities getDesiredCapabilities1() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("deviceName", "Shiba Shankar");
        //cap.setCapability("deviceName","sdk gphone64_x86_64");
        cap.setCapability("udid", "GYOVVS7DBIQ4WSEY");
        //cap.setCapability("udid","emulator-5554");
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");
        //cap.setCapability("platformVersion","15");
        cap.setCapability("automationName", "uiAutomator2");
        cap.setCapability("adbExecTimeout", 50000);
        cap.setCapability("appium:app", "D:\\project\\NewFramework_Appium\\src\\test\\resources\\testdata\\App\\com.ecwid.android-6.2-APK4Fun.com.apk");


        return cap;
    }

}