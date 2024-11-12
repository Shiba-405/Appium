package com.ce.stepdefinitions;

import com.ce.libraies.TestContext;
import com.ce.pages.E2EFlowDataPage;
import io.cucumber.java.en.Given;
import org.json.simple.parser.ParseException;

import java.io.IOException;


public class ExcelToJSONStepDefinitions {

    private final E2EFlowDataPage onE2EFlowDataPage;

    public ExcelToJSONStepDefinitions( TestContext context) {
     //   onE2EFlowDataPage = context.getPageObjectManager().getE2EFlowDataPage();
        onE2EFlowDataPage = new E2EFlowDataPage();
    }

    /**
     * Create input JSON, runner and feature files from given spec and created flow interface
     *
     * @param excelFile - Spec file provided
     */
    @Given("Create test cases for eApp flow with interface file {string}")
    public void createForesightTestDataInterface(String excelFile) throws IOException, ParseException {
        onE2EFlowDataPage.createForesightTestDataInterface(excelFile);
    }
}