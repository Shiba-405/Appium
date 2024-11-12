package com.ce.runner;

import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.JUnitCore;
import com.ce.runner.RunnerBase;


@CucumberOptions(
        plugin = {
                "rerun:target/failedrun.txt",
                "pretty",
                "html:target/cucumber-reports/cucumberReport.html", // HTML report
                "json:target/cucumber-reports/cucumber.json", // JSON report
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        features = {"classpath:"},
        tags = "",
        glue = {"com.ce.stepdefinitions"},
        monochrome = true,
        publish = true
)
public class RunOriginalTest extends RunnerBase{
    private static final Logger Log = LogManager.getLogger(RunOriginalTest.class);
    public static void main(String[] args) {
        // Add the UniqueTestCounter listener to your test run
        JUnitCore core = new JUnitCore();
        core.addListener(new UniqueTestCounter());

        // Run your tests
        core.run(RunOriginalTest.class);
    }
}