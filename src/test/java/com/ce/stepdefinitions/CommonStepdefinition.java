package com.ce.stepdefinitions;

import com.ce.libraies.DriverManager;
import com.ce.libraies.FLUtilities;
import com.ce.libraies.TestContext;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class CommonStepdefinition extends FLUtilities {

    private static final Logger Log = LogManager.getLogger(CommonStepdefinition.class);
    private AppiumDriver driver;
    private TestContext testContext;
    private WebDriverWait appiumWait;
    public CommonStepdefinition(TestContext context) {
        testContext = context;
        driver = new DriverManager().getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("implicit_wait"))));
        appiumWait  = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("explicit_wait"))));

    }

    @Given("Open page {string}")
    public void userIsOnFLLoginPage(String url) {
        configProperties.setProperty("QA.url", url);
        System.out.println("url == " + url);
        openLoginPage(driver, testContext);
    }

    @Then("Verify Page Title is {string}")
    public void verifyPageTitleIs(String title) {
        Assert.assertEquals("Page title verification failed!", title, driver.getTitle().trim());
    }

    @Then("User Enters value {string} in  {string} {string} having {string} {string}")
    public void enterValueInTextboxHaving(String valueToSend, String wizardType, String fieldName, String locator, String attributeValue) {
        sendKeys(driver, elementByLocator(driver, locator, null, null, attributeValue), valueToSend);
        addPropertyValueInJSON(testContext.getTestCaseID(),testContext,fieldName,valueToSend);
    }

    @Then("User Enters value from JSON {string} in  {string} {string} having {string} {string}")
    public void enterValueInTextboxJSON(String valueToSend, String wizardType, String fieldName, String locator, String attributeValue) {
        sendKeys(driver, elementByLocator(driver, locator, null, null, attributeValue), testContext.getMapTestData().get(valueToSend));
    }

    @Then("User Clicks element {string} {string} having {string} {string}")
    public void clickButtonHaving(String fieldName, String wizardType, String locator, String attributeValue) {
        clickElement(driver, elementByLocator(driver, locator, null, null, attributeValue));
    }

    @Then("User Clicks element if present {string} {string} having {string} {string}")
    public void clicksElementIfPresent(String fieldName, String wizardType, String locator, String attributeValue) {
        List<WebElement> elements = elementsByLocator(driver, locator, null, null, attributeValue);
        if(elements.size() > 0) {
            clickElement(driver, elements.get(0));
        }
    }

    @Then("User Verifies Default Value {string} having {string} {string} is {string}")
    public void verifyValue(String wizardType, String locator, String attributeValue, String value) {
        Assert.assertTrue(value.equalsIgnoreCase(verifyValue(driver, elementByLocator(driver, locator, null, null, attributeValue))));
    }

    @Then("User Selects value {string} for {string} {string} having {string} {string}")
    public void selectValueDropDownHaving(String option, String fieldName, String wizardType, String locator, String attributeValue) {
        new Select(elementByLocator(driver, locator, null, null, attributeValue)).selectByVisibleText(option);
    }

    @Given("User is on login page for TestCase {string}")
    public void userIsOnLoginPage(String testCaseID) throws InterruptedException {
        commonSetup(testCaseID);
    }

    private void commonSetup(String testCaseID) {
        testContext.setTestCaseID(testCaseID);
        testContext.setScreenshotFolderName(testCaseID);
        System.out.println("Environment = " + testContext.getEnvironment());
        System.out.println("ApplicationType = " + testContext.getAppType());
        System.out.println("TestCaseID = " + testContext.getTestCaseID());
        System.out.println("CaptureScreenshot = " + testContext.getCaptureScreenshot());
        System.out.println("ScreenshotFolder = " + testContext.getScreenshotFolderName());
        testContext.setMapTestData(getTestData(testCaseID, testContext));
        Log.info("TEST CASE {} STARTED", testCaseID);
    }

    @Given("I am on the login screen")
    public void i_am_on_the_login_screen() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("SSM Successful");

    }

    @Then("User Verifies element {string} {string} having {string} {string}")
    public void userVerifiesElementHaving(String fieldName, String wizardType, String locator, String attributeValue) {
        WebElement element = elementByLocator(driver, locator, null, null, attributeValue);
        appiumWait.until(ExpectedConditions.visibilityOf(element));
        Assert.assertTrue(fieldName + " was not displayed", element.isDisplayed());
    }

    @Then("User Verifies text of {string} {string} Should be {string} having {string} {string}")
    public void userVerifiesTextOfShouldBeHaving(String fieldName, String wizardType, String expectedText, String locator, String attributeValue) {
        Assert.assertEquals("Text did not match", expectedText, elementByLocator(driver,locator,null,null,attributeValue).getText().trim());
    }

    @Then("User Verifies element is enabled {string} {string} having {string} {string}")
    public void userVerifiesElementIsEnabledHaving(String fieldName, String wizardType, String locator, String attributeValue) {
        Assert.assertTrue(fieldName + " was not enabled", elementByLocator(driver, locator, null, null, attributeValue).isEnabled());
    }

    @Then("User Verifies element is selected {string} {string} having {string} {string}")
    public void userVerifiesElementIsSelectedHaving(String fieldName, String wizardType, String locator, String attributeValue) {
        Assert.assertTrue(fieldName + " was not selected", elementByLocator(driver, locator, null, null, attributeValue).isSelected());

    }

    @Then("User Clicks back Button on Mobile")
    public void userClicksBackButtonOnMobile() {
        driver.navigate().back();
    }

    @Then("User Accepts alert")
    public void userAcceptsAlert() {
       // WebDriverWait appiumWait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("explicit_wait"))));
       try {
           Alert yes = appiumWait.until(ExpectedConditions.alertIsPresent());
           yes.accept();
       } catch (Exception e) {
           System.out.println("No Alert");
       }

    }

    @Then("User Dismiss alert")
    public void userDismissAlert() {
        try {
            Alert yes = appiumWait.until(ExpectedConditions.alertIsPresent());
            yes.dismiss();
        } catch (Exception e) {
            System.out.println("No Alert");
        }
    }

    @Then("User Clicks home Button on Mobile")
    public void userClicksHomeButtonOnMobile() {
        AndroidDriver driver1 = (AndroidDriver) driver;
        driver1.pressKey(new KeyEvent(AndroidKey.HOME));
    }

    @Then("User Waits for {int} milliseconds")
    public void userWaitsForMilliseconds(int time) throws InterruptedException {
        Thread.sleep(time);
    }

    @Then("User Verifies element in List {string} in {string} {string} having {string} {string}")
    public void userVerifiesElementInListInHaving(String expectdData, String fieldName, String wizardType, String locator, String attributeValue) {
        List<WebElement> elements = elementsByLocator(driver, locator, null, null, attributeValue);
        boolean isPresent = false;
        for (WebElement element : elements){
           if( element.getText().trim().equals(expectdData)){
               isPresent = true;
               break;
            }
        }
        Assert.assertTrue(expectdData+" not present in "+fieldName,isPresent);
    }
}
