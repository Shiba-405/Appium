package com.ce.runner.TestCases;

import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.JUnitCore;
import com.ce.runner.TestCases.RunnerBase;


@CucumberOptions(
        plugin = {
                "rerun:target/failedrun.txt",
                "pretty",
                "html:target/cucumber-reports/cucumberReport.html", // HTML report
                "json:target/cucumber-reports/cucumber.json", // JSON report
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        features = {"classpath:"},
		tags = "@UI-smoke2",
        glue = {"com.ce.stepdefinitions"},
        monochrome = true,
        publish = true
)
public class UI_smoke2_RunTest extends RunnerBase{
    private static final Logger Log = LogManager.getLogger(UI_smoke2_RunTest.class);
    public static void main(String[] args) {
        // Add the UniqueTestCounter listener to your test run
        JUnitCore core = new JUnitCore();
        core.addListener(new UniqueTestCounter());

        // Run your tests
        core.run(UI_smoke2_RunTest.class);
    }
}
