//package com.ce.libraies;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import com.jayway.jsonpath.JsonPath;
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//
//public class RestAPICalls {
//    Map<String, String> validJson = new HashMap<>();
//    Response response;
//    String header, method;
//    String[][] arrli; // = {{"AccountNumber","1245896"},{"Type","GHMI"},{"Url","simpleuser001.com"},{"Date","07/25/1981"}};
//
//    public Response call(RequestSpecification request, String method, String endpoint) {
//
//        switch (method.toLowerCase()) {
//
//            case "post":
//                response = request.post(endpoint);
//                break;
//            case "get":
//                response = request.get(endpoint);
//                break;
//            case "put":
//                response = request.put("");
//                break;
//            case "delete":
//                response = request.delete("");
//                break;
//        }
//        return response;
//    }
//
//    public Response test(String restUrl, String method, Map<String, String> map, String counterAPI, TestContext testContext) {
//
//        RestAssured.baseURI = restUrl;
//        String testJSON = testContext.getMapTestData().get(counterAPI).trim();
//        String field = testJSON.contains("field") ? JsonPath.read(testJSON, "$.field").toString().trim() : "";
//        List<String> fieldList = Arrays.asList(JsonPath.read(testJSON, "$.headers").toString().trim().replaceAll("[{}]", "").split("\\|"));
//        List<String> fieldList1 = new ArrayList<>();
//        if (!field.equals("")) {
//            for (String fieldName : fieldList)
//                fieldList1.add(fieldName.replaceAll(field, testContext.getMapTestData().get(field)));
//            fieldList = fieldList1;
//        }
//
//        Map<String, String> requestHeaders = fieldList.stream()
//                .map(s -> s.split("="))
//                .collect(Collectors.toMap(s -> s[0].trim(), s -> s[1].trim()));
//
//        RequestSpecification request = RestAssured.given();
//        request.headers(requestHeaders);
//
//        // Add the Json to the body of the request
//        request.body(JsonPath.read(testJSON, "$.body").toString().trim());
//
//        // Post the request and check the response
//        return (call(request, method, JsonPath.read(testJSON, "$.endpoint").toString().trim()));
//        //return response;
//    }
//
//    public Object getKey(JSONObject json, String key) {
//        boolean exists = json.has(key);
//        Iterator<?> keys;
//        String nextKeys;
//        Object value = "";
//
//        if (!exists) {
//            keys = json.keys();
//            while (keys.hasNext()) {
//                nextKeys = (String) keys.next();
//                try {
//                    if (json.get(nextKeys) instanceof JSONObject) {
//                        Object getValue = getKey(json.getJSONObject(nextKeys), key);
//                        if (!getValue.toString().isEmpty()) {
//                            value = getValue;
//                            break;
//                        }
//                    } else if (json.get(nextKeys) instanceof JSONArray) {
//                        JSONArray jsonarray = json.getJSONArray(nextKeys);
//                        for (int i = 0; i < jsonarray.length(); i++) {
//                            String jsonArrayString = jsonarray.get(i).toString();
//                            JSONObject innerJSOn = new JSONObject(jsonArrayString);
//                            Object getValue = getKey(innerJSOn, key);
//                            if (!getValue.toString().isEmpty()) {
//                                value = getValue;
//                                break;
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    throw new RuntimeException("Key doesn't exist in response - " + key);
//                }
//            }
//        } else {
//            value = json.get(key);
//        }
//        return value;
//    }
//}