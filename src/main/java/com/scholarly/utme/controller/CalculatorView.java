package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Calculator view controller to handle calculator logic and operations
 */
public class CalculatorView {
    private static final String TAG = "CalculatorView: ";

    @FXML
    private Label result, expression;


    @FXML
    private void processNumber(ActionEvent event) {
        String input = ((Button)event.getSource()).getText();

        if (expression.getText().contains(".") && input.contains(".")) {
            return;
        }

        expression.setText(expression.getText() + input);

        System.out.println(TAG + "Expression -> " + expression.getText());

    }

    private void processNumber(String input) {

        if (expression.getText().contains(".") && input.contains(".")) {
            return;
        }

        expression.setText(expression.getText() + input);

        System.out.println(TAG + "Expression -> " + expression.getText());

    }

    @FXML
    private void processOperator(ActionEvent event) {
        String input = ((Button) event.getSource()).getText();
        String lastChar = "";
        if (expression.getText().length() > 0) {
            lastChar = expression.getText().substring(expression.getText().length() - 1);
        }

        if (lastCharacterContainsOperator(input, lastChar)) {
            return;
        }

        if (expressionContainsOperator(expression.getText())) {
            expression.setText("(" + expression.getText() + ")" + input);
        } else {
            expression.setText(expression.getText() + input);
        }

    }

    @FXML
    private void processEquals(ActionEvent event) {
        String lastChar = expression.getText().substring(expression.getText().length() - 1);

        if (expression.getText().isEmpty() || lastCharacterContainsOperator(lastChar, lastChar)) {
            result.setText("");
            return;
        }

        Calculator calculator = new Calculator(false);

        try {
            calculator.parse(expression.getText());
            System.out.println(TAG + "Expression parsed -> " + expression.getText());
            System.out.println(TAG + "Output gotten -> " + calculator.evaluate());
            result.setText(calculator.evaluate());

        } catch (Exception e) {
            System.out.println("Could not calculate expression because " + e.getMessage());
        }

    }

    @FXML
    private void processUnaryOperator(ActionEvent event) {
        String input = (String) ((Button) event.getSource()).getUserData();

        String lastChar = expression.getText().substring(expression.getText().length() - 1);

        if (expression.getText().isEmpty() || lastCharacterContainsOperator(input, lastChar)) {
            return;
        }

        switch (input) {
            case "sqrt" -> expression.setText("sqrt(" + expression.getText() + ")");
            case "x^2" -> expression.setText("(" + expression.getText() + ")^2");
        }

        Calculator calculator = new Calculator(false);

        try {
            calculator.parse(expression.getText());
            result.setText(calculator.evaluate());
        } catch (Exception e) {
            System.out.println(TAG + "Cannot convert number because " + e.getMessage());
        }

    }

    @FXML
    private void delete(ActionEvent event) {
        String expressionText = expression.getText();

        if (expressionText.length() > 0) {
            String newExpression;

            if (expressionText.startsWith("(") && expressionText.substring(expressionText.length() - 1).equalsIgnoreCase(")")) {
                newExpression = expressionText.substring(1, expressionText.length() - 1);
            } else {
                newExpression = expressionText.substring(0, expressionText.length() - 1);
            }
            expression.setText(newExpression);
        }

    }

    @FXML
    private void clearFunction(ActionEvent event) {
        result.setText("");
        expression.setText("");
    }

    @FXML
    private void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().isDigitKey()) {
            String keyPressed = keyEvent.getCode().getName();
            processNumber(keyPressed);
        }

    }

    private boolean lastCharacterContainsOperator(String value, String lastChar) {
        String[] operators = {"+", "-", "/", "*", "^"};

        boolean valueContainsOperator = Arrays.stream(operators).anyMatch(it -> it.equalsIgnoreCase(value));
        boolean lastCharContainsOperator = Arrays.stream(operators).anyMatch(it -> it.equalsIgnoreCase(lastChar));

        return valueContainsOperator && lastCharContainsOperator;
    }

    private boolean expressionContainsOperator(String expression) {
        String[] operators = {"+", "-", "/", "*", "^"};

        System.out.println(TAG + "Expression -> " + expression);

        System.out.println(TAG + "Expression contains operator -> " + Arrays.stream(operators).anyMatch(it -> expression.contains(it)));

        return Arrays.stream(operators).anyMatch(it -> expression.contains(it));

    }

}
