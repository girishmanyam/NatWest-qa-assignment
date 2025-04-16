package com.natwest.apitests.hooks;


import com.natwest.apitests.context.ScenarioContextHolder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void beforeScenario() {
        logger.info("Starting the test scenario...");
        if (logger.isDebugEnabled()) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
    }

    @After
    public void clearClient() {
        ScenarioContextHolder.clear();
    }
}


