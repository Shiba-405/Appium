package com.ce.pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ce.libraies.Enums.EnumsTestingTypes;
import com.ce.libraies.EnumsCommon;
import com.ce.libraies.FLException;
import com.ce.libraies.FLUtilities;
import io.appium.java_client.AppiumDriver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.*;
import java.util.*;

import static org.openqa.selenium.support.PageFactory.initElements;

@EqualsAndHashCode(callSuper = true)
@Data
public class E2EFlowDataPage extends FLUtilities {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JSONObject jsonObject = new JSONObject();
    JSONObject jsonTestData = new JSONObject();

    public String getCellValue(Cell cell) {
        // Initialize a string to store the cell value
        String excelValue = "";

        // Check if the cell is not null and its type is STRING
        if (cell != null && cell.getCellType() == CellType.STRING) {
            // Get the string value of the cell and trim any leading or trailing whitespace
            excelValue = cell.getStringCellValue().trim();

            // Check if the cell is not null and its type is NUMERIC
        } else if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            // Convert the numeric value of the cell to a string and trim any leading or trailing whitespace
            excelValue = String.valueOf(((XSSFCell) cell).getRawValue()).trim();
            // If the cell is null or neither STRING nor NUMERIC
        }
        return excelValue;
    }

    public void createForesightTestDataInterface(String excelFile) throws IOException, ParseException {
        // Define the file path using a common absolute path and the provided Excel file name
        String filePath = EnumsCommon.ABSOLUTE_FILES_PATH.getText() + excelFile;
        boolean flag = true;
        Object obj;
        JSONParser parser = new JSONParser();
        String testDataFile = EnumsCommon.ABSOLUTE_FILES_PATH.getText() + "e2e-TestData.json";

        List<String> testTypes = Arrays.asList(EnumsTestingTypes.ENUMSTESTINGTYPES.getText().split(", "));
        // Use try-with-resources to ensure the FileInputStream and XSSFWorkbook are closed properly
         FileInputStream file = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(file) ;

            // Delete existing runner and feature files
            deleteRunnerFeature(EnumsCommon.RUNNERFILESPATH.getText() + "TestCases");
            deleteRunnerFeature(EnumsCommon.FEATUREFILESPATH.getText() + "TestCases");
            File jsonFilePath = new File(testDataFile);
            if (jsonFilePath.length() == 0)
                flag = false;

            if (flag) {
                obj = parser.parse(new FileReader(testDataFile));
                jsonTestData = (JSONObject) obj;
                jsonTestData = (JSONObject) jsonTestData.get("testData");
            }

            for (String testingType : testTypes) {
                Sheet sheet = workbook.getSheet(testingType);
                Iterator<Row> iterator = sheet.iterator();

                // Retrieve the header row
                Row headerRow = iterator.next().getSheet().getRow(0);

                // Find the column indexes for specific headers in the header row
                int featureNameIndex = findColumnIndex(headerRow, "Feature Name");
                int descriptionIndex = findColumnIndex(headerRow, "Description");
                int scenarioIndex = findColumnIndex(headerRow, "Scenario");
                int testCaseNameIndex = findColumnIndex(headerRow, "TestCaseName");
                int testCaseSheetIndex = findColumnIndex(headerRow, "TestCaseSheet");
                int executeIndex = findColumnIndex(headerRow, "Execute");

                // Iterate through the rows of the sheet, starting from the second row
                while (iterator.hasNext()) {
                    String tagNames = "";
                    Row currentRow = iterator.next();
                    // Retrieve cell values from the current row based on the header indexes
                    String featureName = getCellValue(currentRow.getCell(featureNameIndex));
                    String description = getCellValue(currentRow.getCell(descriptionIndex));
                    String scenario = getCellValue(currentRow.getCell(scenarioIndex));
                    String testCaseName = getCellValue(currentRow.getCell(testCaseNameIndex));
                    String testCaseSheet = getCellValue(currentRow.getCell(testCaseSheetIndex));
                    String execute = getCellValue(currentRow.getCell(executeIndex));

                    // Check if the 'Execute' column value is 'yes'
                    if (execute.equalsIgnoreCase("yes")) {
                        createRunnerFile(testingType, scenario);
                        switch (testingType) {
                            case "UI":
                                createUIFeatureFile(featureName, description, scenario, testCaseName, testCaseSheet, testingType, jsonTestData);
                                break;
                        }
                    }
                }
            }
            jsonObject.put("testData", jsonTestData);
            FileWriter jsonTestData1 = new FileWriter(testDataFile);
            BufferedWriter writer = new BufferedWriter(jsonTestData1);
            writer.write(gson.toJson(jsonObject));
            writer.close();

            // Create a unique counter which will keep track of rerun count
            createUniqueCounter("ForeSightTest");
            // Create a RunnerBase which will keep track for testng.xml
            createRunnerBase("ForeSightTest");
//        } catch (IOException e) {
//            // Handle exceptions related to file access
//            throw new FLException("File is inaccessible: " + e.getMessage());
//        } catch (Exception e) {
//            // Handle any other exceptions that may occur
//            throw new FLException("Reading Properties File Failed: " + e.getMessage());
//        }
    }

    /**
     * Create feature file based on module and jurisdiction
     *
     * @param featureName  - Feature name
     * @param description  - module name
     * @param scenario     - product name
     * @param testCaseName - Spec from client
     */
    public void createUIFeatureFile(String featureName, String description, String scenario, String testCaseName, String testCaseSheet, String testingType, JSONObject jsonTestData) throws IOException {
        List<String> lines = new ArrayList<>();
        File tempFile = null;
        String line;

            lines.add("Feature: " + featureName + "\n");
            lines.add("\t" + description + "\n");
            lines.add("\t@" + testingType + "-" + scenario);
            lines.add("\tScenario: " + scenario + " - " + testCaseName);
            lines.add("\t\tGiven User is on login page for TestCase \"" + testingType + "-" + scenario + "\"");

            String filePath = EnumsCommon.ABSOLUTE_CLIENTFILES_PATH.getText() + testCaseSheet;
            // Read excel file to create test data
            FileInputStream file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            Sheet sheet = workbook.getSheet(scenario);
            Iterator<Row> iterator = sheet.iterator();

            Row headerRow = iterator.next().getSheet().getRow(0);

            int stepsIndex = findColumnIndex(headerRow, "Steps");
            int locatorTypeIndex = findColumnIndex(headerRow, "Locator Type");
            int commonTagIndex = findColumnIndex(headerRow, "Common Tag");
            int wizardControlTypesIndex = findColumnIndex(headerRow, "Wizard Control Types");
            int testDataIndex = findColumnIndex(headerRow, "Test Data");
            int attributeIndex = findColumnIndex(headerRow, "Attribute");
            int fieldNameIndex = findColumnIndex(headerRow, "Field Name");
            int fileNameIndex = findColumnIndex(headerRow, "File Name");
            int stepsRangeIndex = findColumnIndex(headerRow, "Steps Range");

            // Iterate through the rows of the sheet, starting from the second row
            JSONObject tempJson = new JSONObject();
            JSONObject tempReusableJson = new JSONObject();
            int countReused = 1;

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();

                // Retrieve cell values from the current row based on the header indexes
                String steps = getCellValue(currentRow.getCell(stepsIndex));
                String locatorType = getCellValue(currentRow.getCell(locatorTypeIndex));
                String commonTag = getCellValue(currentRow.getCell(commonTagIndex)).replaceAll("\"", "'");
                String wizardControlTypes = getCellValue(currentRow.getCell(wizardControlTypesIndex));
                String testData = getCellValue(currentRow.getCell(testDataIndex));
                String attribute = getCellValue(currentRow.getCell(attributeIndex));
                String fieldName = getCellValue(currentRow.getCell(fieldNameIndex));
                String fileName = getCellValue(currentRow.getCell(fileNameIndex));
                String stepRange = getCellValue(currentRow.getCell(stepsRangeIndex));

                if (testData.contains("https")) {
                    configProperties.setProperty("QA.url", testData);
                    System.out.println("url = " + testData);
                }

                lines.add("\t\t" + createUIStep(steps, locatorType, commonTag, wizardControlTypes, testData, attribute, fieldName, lines, fileName, stepRange, tempReusableJson, testingType, scenario));
                if (steps.equals("Use Reusable Method")) {
                    Map<String, String> tempMap = (Map<String, String>) jsonTestData.get(testingType + "-" + fileName);
                    tempJson.putAll(tempMap);
                }
                if (!(fieldName.isEmpty() || testData.isEmpty())) {
                    Map<String, String> reusedJson = new HashMap<>();
                    reusedJson.put(fieldName, testData);
                    tempReusableJson.put(countReused, reusedJson);
                    tempJson.put(fieldName, testData);
                }
                countReused++;
            }

            tempFile = new File(EnumsCommon.FEATUREFILESPATH.getText() + "/TestCases/" + testingType + "-" + scenario + "_" + "Test.feature");
            tempFile.getParentFile().mkdirs();
            FileWriter featureFile = new FileWriter(tempFile);
            BufferedWriter writer = new BufferedWriter(featureFile);
            for (String line1 : lines)
                writer.write(line1 + "\n");
            writer.close();
            System.out.println("Feature File Created");
//            System.out.println("url = " + configProperties.getProperty("QA.url"));
            updateJSON(jsonTestData, testingType, scenario, tempJson);
//            masterJson.put(testingType + "-" + scenario, tempJson);
//        } catch (IOException e) {
//            throw new FLException("File is inaccessible" + e.getMessage());
//        } catch (Exception e) {
//            throw new FLException("Reading Properties File Failed" + e.getMessage());
//        }
    }

    public void updateJSON(JSONObject jsonTestData, String testingType, String scenario, JSONObject tempJson) {
        if (jsonTestData.containsKey(testingType + "-" + scenario)) {
            JSONObject tempClientData = (JSONObject) jsonTestData.get(testingType + "-" + scenario);

            for (Object temp : tempJson.keySet()) {
                if (tempClientData.containsKey(temp.toString()) && !(tempClientData.get(temp.toString()).toString().isEmpty())) {
                    try {
                        JSONObject tempClientJsonData = (JSONObject) tempClientData.get(temp.toString());
                        JSONObject tempClientNewJsonData = (JSONObject) tempJson.get(temp.toString());
                        for (Object tempJsonKey : tempClientJsonData.keySet()) {
                            if (tempClientJsonData.containsKey(tempJsonKey.toString())) {
                                if (!tempClientJsonData.get(tempJsonKey.toString()).toString().equalsIgnoreCase(tempClientNewJsonData.get(tempJsonKey.toString()).toString()))
                                    tempClientJsonData.put(tempJsonKey.toString(), tempClientNewJsonData.get(tempJsonKey.toString()).toString());
                            }
                        }
                        tempClientData.put(temp.toString(), tempClientJsonData);
                    } catch (Exception e) {
                        if (!tempClientData.get(temp.toString()).toString().equalsIgnoreCase(tempJson.get(temp.toString()).toString()))
                            tempClientData.put(temp.toString(), tempJson.get(temp.toString()).toString());
                    }
                } else
                    tempClientData.put(temp.toString(), tempJson.get(temp.toString()));
            }
            jsonTestData.put(testingType + "-" + scenario, tempClientData);
        } else
            jsonTestData.put(testingType + "-" + scenario, tempJson);
    }


    public String createUIStep(String steps, String locatorType, String commonTag, String wizardControlTypes, String testData, String attribute, String fieldName, List<String> lines, String fileName, String stepsRange, JSONObject tempReusableJson, String testingType, String scenario) throws IOException {
        switch (steps) {
            case "Open page":
                return "When " + steps + " \"" + testData + "\"";
            case "Verify Page Title":
                return "Then " + steps + " is \"" + testData + "\"";
            case "Enters value":
            case "Enters value from JSON":
                return "Then User " + steps + " \"" + testData + "\" in " + " \"" + wizardControlTypes + "\" \"" + fieldName + "\" having \"" + locatorType + "\" \"" + commonTag + "\"";
            case "Clicks element":
            case "Clicks element if present":
                return "Then User " + steps + " \"" + fieldName + "\"" + " \"" + wizardControlTypes + "\" having \"" + locatorType + "\" \"" + commonTag + "\"";
            case "Verifies Default Value":
                return "Then User " + steps + " \"" + fieldName + "\"" + " \"" + wizardControlTypes + "\" having \"" + locatorType + "\" \"" + commonTag + "\" is \"" + testData + "\"";
            case "Selects value":
                return "Then User " + steps + " \"" + testData + "\" for \"" + fieldName + "\"" + " \"" + wizardControlTypes + "\" having \"" + locatorType + "\" \"" + commonTag + "\"";
            case "Verifies element":
            case "Verifies element is enabled":
            case "Verifies element is selected":
                return "Then User " + steps + " \"" + fieldName + "\"" + " \"" + wizardControlTypes + "\" having \"" + locatorType + "\" \"" + commonTag + "\"";
            case "Verifies text":
                return "Then User " + steps + " of \"" + fieldName + "\"" + " \"" + wizardControlTypes + "\" Should be \"" + testData + "\" having \"" + locatorType + "\" \"" + commonTag + "\"";
            case "Clicks back Button on Mobile":
            case "Clicks home Button on Mobile":
            case "Accepts alert":
            case "Dismiss alert":
            case "Open notification on Mobile":
                return "Then User " + steps;
            case "Verifies element in List":
                return "Then User " + steps + " \""+ testData +"\" in \""+ fieldName + "\"" + " \"" + wizardControlTypes + "\" having \"" + locatorType + "\" \"" + commonTag + "\"";
            case "Create Reusable Method":
                String reusableFile = EnumsCommon.REUSABLE_FILES_PATH.getText() + fileName + ".txt";
                File file = new File(reusableFile);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                FileWriter data = new FileWriter(reusableFile);
                BufferedWriter writer = new BufferedWriter(data);
                JSONObject tempJson = new JSONObject();
                int startRange = Integer.parseInt(stepsRange.substring(0, stepsRange.indexOf("-")));
                int endRange = Integer.parseInt(stepsRange.substring(stepsRange.indexOf("-") + 1));
                writer.flush();
                for (int count = startRange; count <= endRange; count++) {
                    writer.write(lines.get(count + 3) + "\n");
                    if (tempReusableJson.containsKey(count)) {
                        Map<String, String> tempMap = (Map<String, String>) tempReusableJson.get(count);
                        tempJson.put(tempMap.keySet().toArray()[0], tempMap.get(tempMap.keySet().toArray()[0]));
                    }
                }
                writer.close();
                updateJSON(jsonTestData, testingType, fileName, tempJson);
                return "";
            case "Use Reusable Method":
                String line;
                StringBuilder linesResult = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(EnumsCommon.REUSABLE_FILES_PATH.getText() + fileName + ".txt"));
                while ((line = reader.readLine()) != null) {
                    linesResult.append(line.trim() + "\n\t\t");
                }
                reader.close();
                return linesResult.toString();
            case "":
                return "";
            default:
                throw new FLException(steps + " Did not match");

        }
    }

    /**
     * Create runner file based on module and jurisdiction
     */
    public void createRunnerFile(String testingType, String scenario) {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(EnumsCommon.RUNNERFILESPATH.getText() + "RunOriginalTest.java"));
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("com.ce.runner", "com.ce.runner.TestCases");
                line = line.replaceAll("RunOriginalTest", testingType + "_" + scenario + "_" + "RunTest");
                line = replaceLine(line, "tags = ", "\t\ttags = \"@" + testingType + "-" + scenario + "\",");
                lines.add(line);
            }
            reader.close();
            File tempFile = new File(EnumsCommon.RUNNERFILESPATH.getText() + "TestCases/" + testingType + "_" + scenario + "_" + "RunTest.java");
            tempFile.getParentFile().mkdirs();
            FileWriter runnerFile = new FileWriter(tempFile);
            BufferedWriter writer = new BufferedWriter(runnerFile);
            for (String line1 : lines)
                writer.write(line1 + "\n");
            writer.close();
            System.out.println("Runner File Created");
        } catch (IOException e) {
            throw new FLException("File is inaccessible" + e.getMessage());
        }
    }

    /**
     * Create runner file for each re-run
     */
    public void createUniqueCounter(String directory) {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(EnumsCommon.RUNNERFILESPATH.getText() + "UniqueTestCounter.java"));
            while ((line = reader.readLine()) != null) {
                line = replaceLine(line, "package com.ce.runner;", "package com.ce.runner.TestCases;");
                lines.add(line);
            }
            reader.close();
            File tempFile = new File(EnumsCommon.RUNNERFILESPATH.getText() + "/TestCases/UniqueTestCounter.java");
            tempFile.getParentFile().mkdirs();
            FileWriter runnerFile = new FileWriter(tempFile);
            BufferedWriter writer = new BufferedWriter(runnerFile);
            for (String line1 : lines)
                writer.write(line1 + "\n");
            writer.close();
        } catch (IOException e) {
            throw new FLException("File is inaccessible" + e.getMessage());
        }
    }

    public void createRunnerBase(String directory) {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(EnumsCommon.RUNNERFILESPATH.getText() + "RunnerBase.java"));
            while ((line = reader.readLine()) != null) {
                line = replaceLine(line, "package com.ce.runner;", "package com.ce.runner.TestCases;");
                lines.add(line);
            }
            reader.close();
            File tempFile = new File(EnumsCommon.RUNNERFILESPATH.getText() + "/TestCases/RunnerBase.java");
            tempFile.getParentFile().mkdirs();
            FileWriter runnerFile = new FileWriter(tempFile);
            BufferedWriter writer = new BufferedWriter(runnerFile);
            for (String line1 : lines)
                writer.write(line1 + "\n");
            writer.close();
        } catch (IOException e) {
            throw new FLException("File is inaccessible" + e.getMessage());
        }
    }

    /**
     * Replace line with given parameters
     *
     * @param line         - original line
     * @param toBeReplaced - Substring which needs to be replaced
     * @param replacement  - Replace by given string
     * @return Replaced string - Replaced string
     */
    public String replaceLine(String line, String toBeReplaced, String replacement) {
        return line.contains(toBeReplaced) ? replacement : line;
    }

}

