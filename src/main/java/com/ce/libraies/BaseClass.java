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

}