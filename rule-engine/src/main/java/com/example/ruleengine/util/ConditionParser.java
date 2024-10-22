package com.example.ruleengine.util;

public class ConditionParser {
    // Helper methods to parse condition strings
    public static String[] parseCondition(String condition) {
        // Parse condition "age > 30" into ["age", ">", "30"]
        return condition.split(" ");
    }
}
