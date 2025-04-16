package com.natwest.apitests.context;

public class ScenarioContextHolder {
    private static final ThreadLocal<ScenarioContext> threadLocalContext =
            ThreadLocal.withInitial(ScenarioContext::new);

    public static ScenarioContext threadScenarioContext() {
        return threadLocalContext.get();
    }

    public static void clear() {
        threadLocalContext.remove();
    }
}

