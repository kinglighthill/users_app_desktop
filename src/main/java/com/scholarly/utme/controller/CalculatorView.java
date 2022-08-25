package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.UnaryCalculator;
import com.scholarly.utme.ui.utils.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;

/**
 * Calculator view controller to handle calculator logic and operations
 */
public class CalculatorView {
    private static final String TAG = "CalculatorView: ";

    @FXML
    private Label result, expression;

    private float unaryNumber = 0;

    Calculator binaryCalculator = new Calculator(false);


    @FXML
    public void processNumber(ActionEvent event) {
        String input = ((Button)event.getSource()).getText();

        if (expression.getText().contains(".") && input.contains(".")) {
            return;
        }

        expression.setText(expression.getText() + input);

        System.out.println(TAG + "Expression -> " + expression.getText());

    }

    @FXML
    public void processOperator(ActionEvent event) {
        String input = ((Button) event.getSource()).getText();
        String lastChar = "";
        if (expression.getText().length() > 0) {
            lastChar = expression.getText().substring(expression.getText().length() - 1);
        }

        if (lastCharacterContainsOperator(input, lastChar)) {
            return;
        }

        expression.setText(expression.getText() + input);

    }

    @FXML
    public void processEquals(ActionEvent event) {
        if (expression.getText().isEmpty()) {
            result.setText("");
            return;
        }

        try {
            binaryCalculator.parse(expression.getText());
            result.setText(binaryCalculator.evaluate());

        } catch (Exception e) {
            System.out.println("Could not calculate expression because " + e.getMessage());
        }

    }

    @FXML
    public void processUnaryOperator(ActionEvent event) {
        String operator = ((Button) event.getSource()).getText();

        String lastChar = expression.getText().substring(expression.getText().length() - 1);

        if (lastCharacterContainsOperator(operator, lastChar)) {
            return;
        }

        try {
            unaryNumber = Float.parseFloat(expression.getText());
        } catch (Exception e) {
            unaryNumber = Float.parseFloat(result.getText());
            expression.setText(result.getText());
            System.out.println(TAG + "Cannot convert number because " + e.getMessage());
        }

        result.setText("");

        UnaryCalculator unaryCalculator = new UnaryCalculator();

        float output = unaryCalculator.calculateUnaryNumber(unaryNumber, operator);
        result.setText(String.valueOf(output));
    }

    @FXML
    private void delete(ActionEvent event) {
        String expressionText = expression.getText();
        System.out.println(TAG + "Current text -> " + expressionText);
        if (expressionText.length() > 0) {
            String newText = expressionText.replace(expressionText.substring(expressionText.length() - 1), "");
            System.out.println(TAG + "Expression text length -> " + expressionText.length());
            System.out.println(TAG + "Expression text substring -> " + expressionText.substring(expressionText.length() - 1));
            expression.setText(newText);
            System.out.println(TAG + "New expression -> " + expression.getText());
        }
    }

    @FXML
    public void clearFunction(ActionEvent event) {
        result.setText("");
        expression.setText("");
    }

    private boolean lastCharacterContainsOperator(String value, String lastChar) {
        String[] operators = {"+", "-", "/", "*"};

        boolean valueContainsOperator = Arrays.stream(operators).anyMatch(it -> it.equalsIgnoreCase(value));
        boolean lastCharContainsOperator = Arrays.stream(operators).anyMatch(it -> it.equalsIgnoreCase(lastChar));

        if (valueContainsOperator && lastCharContainsOperator) {
            return true;
        }
        return false;
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println(TAG + "Key pressed -> " + keyEvent.getCode());

    }
}
