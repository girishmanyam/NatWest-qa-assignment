package com.natwest.apitests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.natwest.apitests",
        plugin = {"pretty", "html:target/cucumber-report.html"},
        monochrome = true
)
public class RunTest extends AbstractTestNGCucumberTests {
}