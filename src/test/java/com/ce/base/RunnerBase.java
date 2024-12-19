package com.ce.base;

import com.ce.libraies.BaseClass;
import com.ce.libraies.DriverManager;
import com.ce.libraies.ServerManager;
import com.ce.libraies.TestContext;
import io.appium.java_client.AppiumDriver;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.*;
import com.ce.base.RetryAnalyzer;


public class RunnerBase extends AbstractTestNGCucumberTests {
    private static final ThreadLocal<TestNGCucumberRunner> testNGCucumberRunner = new ThreadLocal<>();
    public static TestNGCucumberRunner getRunner(){
        return testNGCucumberRunner.get();
    }

    public static void setRunner(TestNGCucumberRunner testNGCucumberRunner1){
        testNGCucumberRunner.set(testNGCucumberRunner1);
    }

    @Parameters({"udid", "deviceName"})
    @BeforeClass(alwaysRun = true)
    public void setUpClass(@Optional ("emulator-5554") String udid,@Optional ("emulator-5554") String deviceName) throws Exception {
        ThreadContext.put("ROUTINGKEY", udid+ "_" + deviceName);
        TestContext testContext = new TestContext();
        testContext.setUdid(udid);
        testContext.setDeviceName(deviceName);
        BaseClass base = new BaseClass();
        base.loadConfigData(testContext);
        new ServerManager().startServer();
        new DriverManager().initializeDriver(udid);

        setRunner(new TestNGCucumberRunner(this.getClass()));
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios", retryAnalyzer = RetryAnalyzer.class)
    public void scenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) throws Throwable {
        getRunner().runScenario(pickle.getPickle());

    }

    @DataProvider
    public Object[][] scenarios() {

        TestNGCucumberRunner aa = getRunner();
        Object[][] scenarios = getRunner().provideScenarios();
        return getRunner().provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        TestContext testContext = new TestContext();

        if(testContext.getMobDriver() != null){
            testContext.getMobDriver().quit();

        }
        AppiumDriver driver = new DriverManager().getDriver();
        if(driver != null){
            driver.quit();

        }

        if(testContext.getService() != null){
            testContext.getService().stop();
        }
        if(testNGCucumberRunner != null){
            getRunner().finish();
        }
    }
}
