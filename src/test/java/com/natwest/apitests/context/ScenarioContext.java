package com.natwest.apitests.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenarioContext {

    private final Map<String, Object> contextMap = new HashMap<>();

    public void put(String apiName, Object value) {
        contextMap.put(apiName, value);
    }

    public String getString(String key) {
        return (String) contextMap.get(key);
    }


    public Object get(String key) {
        return contextMap.get(key);
    }

    public Response getResponse(String key) {
        return (Response) contextMap.get(key);
    }

    public List<Response> getResponses(String key) {
        return (List<Response>) contextMap.get(key);
    }

    public void clear() {
        contextMap.clear();
    }
}
