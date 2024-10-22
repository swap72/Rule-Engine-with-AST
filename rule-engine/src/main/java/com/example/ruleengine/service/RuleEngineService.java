package com.example.ruleengine.service;

import com.example.ruleengine.model.Node;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class RuleEngineService {

    // Create an AST from the rule string
    public Node createRule(String ruleString) {
        // Validate the rule string first
        validateRuleString(ruleString);

        // Clean up the input by replacing parentheses with space-padded versions
        ruleString = ruleString.replaceAll("\\(", " ( ")
                               .replaceAll("\\)", " ) ")
                               .replaceAll("\\s+", " ").trim();

        Stack<Node> stack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        String[] tokens = ruleString.split(" ");

        for (int index = 0; index < tokens.length; index++) {
            String token = tokens[index];

            // Handle opening parentheses
            if (token.equals("(")) {
                operatorStack.push(token);
            }
            // Handle closing parentheses
            else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    applyOperator(stack, operatorStack.pop());
                }
                // Pop the opening '('
                operatorStack.pop();
            }
            // Handle logical operators
            else if (token.equalsIgnoreCase("AND") || token.equalsIgnoreCase("OR")) {
                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                    applyOperator(stack, operatorStack.pop());
                }
                operatorStack.push(token);
            }
            // Handle comparison operators
            else if (token.equals(">") || token.equals("<") || token.equals("=")) {
                // Ensure we have an operand before the operator
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid rule: Missing left operand for the operator " + token);
                }

                String leftOperand = stack.pop().getValue(); // Get the left operand
                String rightOperand = tokens[++index]; // Get the next token as the right operand

                // Create a condition node for the comparison
                String condition = leftOperand + " " + token + " " + rightOperand;
                Node conditionNode = new Node("operand", null, null, condition);
                stack.push(conditionNode);
            }
            // Handle operands (e.g., "age", "30", "department", "'Sales'")
            else {
                // Push operands directly to the stack
                stack.push(new Node("operand", null, null, token));
            }
        }

        // Apply the remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            applyOperator(stack, operatorStack.pop());
        }

        // At the end, the stack should contain exactly one element (the root of the AST)
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid rule: Stack does not have a single root node");
        }

        return stack.pop();
    }

    // Helper to apply operators like AND/OR and create nodes
    private void applyOperator(Stack<Node> stack, String operator) {
        if (stack.size() < 2) {
            throw new IllegalArgumentException("Invalid rule: Not enough operands for the operator " + operator);
        }
        Node right = stack.pop();
        Node left = stack.pop();
        Node operatorNode = new Node("operator", left, right, operator);
        stack.push(operatorNode);
    }

    // Helper method to define precedence of operators
    private int precedence(String operator) {
        if (operator.equalsIgnoreCase("AND")) {
            return 2;
        } else if (operator.equalsIgnoreCase("OR")) {
            return 1;
        }
        return 0;
    }

    // Method to evaluate a rule against the provided data
    public boolean evaluateRule(Node root, org.json.JSONObject data) {
        if (root == null) {
            return true;  // Base case
        }

        if (root.getType().equals("operand")) {
            String condition = root.getValue();
            return evaluateCondition(condition, data);
        }

        boolean leftResult = evaluateRule(root.getLeft(), data);
        boolean rightResult = evaluateRule(root.getRight(), data);

        if (root.getValue().equalsIgnoreCase("AND")) {
            return leftResult && rightResult;
        } else if (root.getValue().equalsIgnoreCase("OR")) {
            return leftResult || rightResult;
        }

        return false;
    }

    // Helper to evaluate conditions like "age > 30"
    private boolean evaluateCondition(String condition, org.json.JSONObject data) {
        if (condition.contains(">")) {
            String[] parts = condition.split(">");
            String field = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());
            return data.getInt(field) > value;
        }
        // Handle other conditions as necessary (e.g., <, =, etc.)
        return false;
    }

    // Basic validation for the rule string to catch syntax errors
    private void validateRuleString(String ruleString) {
        if (ruleString == null || ruleString.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid rule: Rule string cannot be empty");
        }
    }
}
