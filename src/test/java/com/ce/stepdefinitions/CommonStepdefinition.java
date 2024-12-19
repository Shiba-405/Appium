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
import java.util.Map;

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
        List<WebElement> elements = elementsByLocator(driver, locator, attributeValue);
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
        driver.executeScript("mobile: pressKey", Map.ofEntries(Map.entry("keycode", 4)));
    }

    @Then("User Accepts alert")
    public void userAcceptsAlert() {
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
        List<WebElement> elements = elementsByLocator(driver, locator, attributeValue);
        boolean isPresent = false;
        for (WebElement element : elements){
           if( element.getText().trim().equals(expectdData)){
               isPresent = true;
               break;
            }
        }
        Assert.assertTrue(expectdData+" not present in "+fieldName,isPresent);
    }

    @Then("User Scroll up by {int} from point x:{int} and y:{int}")
    public void userScrollUpByFromPointXAndY(int toEnd, int startX, int startY) {
        scrollLeftWithCoordinates(driver,startX,startY,startX,startY-toEnd);
    }

    @Then("User Scroll left by {int} from point x:{int} and y:{int}")
    public void userScrollLeftByFromPointXAndY(int toEnd, int startX, int startY) {
        scrollLeftWithCoordinates(driver,startX,startY,startX-toEnd,startY);

    }

    @Then("User Taps on Point  x:{int} and y:{int}")
    public void userTapsOnPointXAndY(int pointX, int pointY) {
        tapOnPoint(driver,pointX,pointY);
    }


    @Then("User Verifies element by attribute in List {string} in which List having {string} {string} and Element having attribute {string} and value {string}")
    public void userVerifiesElementByAttributeInList(String fieldName, String locator, String attributeValue, String expAttribute, String expValue) {
        boolean value =verifyElementAttribute(driver,locator,attributeValue,expAttribute,expValue);
        Assert.assertTrue("In "+fieldName+" Element "+expValue+" not present", value);
    }
}
