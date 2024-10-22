package com.example.ruleengine.controller;

import com.example.ruleengine.model.Node;
import com.example.ruleengine.service.RuleEngineService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleEngineService ruleEngineService;

    @PostMapping("/create")
    public Node createRule(@RequestBody String ruleString) {
        try {
            return ruleEngineService.createRule(ruleString);
        } catch (IllegalArgumentException e) {
            // Log the error and throw an appropriate HTTP response (e.g., 400 Bad Request)
            throw new IllegalArgumentException("Error creating rule: " + e.getMessage());
        }
    }

    @PostMapping("/evaluate")
    public boolean evaluateRule(@RequestBody String dataJson, @RequestParam("rule") String ruleString) {
        try {
            Node ruleAst = ruleEngineService.createRule(ruleString);
            JSONObject data = new JSONObject(dataJson);
            return ruleEngineService.evaluateRule(ruleAst, data);
        } catch (IllegalArgumentException e) {
            // Handle errors gracefully, like invalid rule or data
            throw new IllegalArgumentException("Error evaluating rule: " + e.getMessage());
        }
    }
}
