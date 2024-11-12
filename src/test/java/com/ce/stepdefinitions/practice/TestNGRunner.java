package com.ce.stepdefinitions.practice;

import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

public class TestNGRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add("src/test/resources/testng.xml"); // Path to your testng.xml
        testng.setTestSuites(suites);
        testng.run();
    }
}

