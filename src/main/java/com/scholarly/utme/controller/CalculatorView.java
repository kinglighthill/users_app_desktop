package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.Calculate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalculatorView {


    @FXML
    private Label result, expression;

    private String currentValue = "";

    private float number1 = 0;

    private float number2 = 0;

    private String operator = "";

    private boolean start = true;

    private Calculate calculate = new Calculate();


    @FXML
    public void processNumber(ActionEvent event) {
        if(start){
            expression.setText("");
            result.setText("");
            currentValue = "";
            start=false;
        }
        String value=((Button)event.getSource()).getText();
        expression.setText(expression.getText() + value);

        currentValue += value;
    }


    @FXML
    public void processBinaryOperator(ActionEvent event) {
        String value = ((Button)event.getSource()).getText();

        if(!value.equals("=")) {
            if(!operator.isEmpty())
                return;

            if (currentValue.isEmpty()) {
                return;
            }

            operator = value;
            expression.setText(expression.getText() + operator);
            number1 = Float.parseFloat(currentValue);
            currentValue = "";
        } else {
            if(operator.isEmpty())
                return;

            number2 = Float.parseFloat(currentValue);
            float output = calculate.calculateBinaryNumber(number1, number2, operator);
            result.setText(String.valueOf(output));
            start = true;
            operator = "";
        }
    }

    @FXML
    public void processUnaryOperator(ActionEvent event) {

        String value = ((Button) event.getSource()).getText();
        if(!operator.isEmpty())
            return;

        operator = value;
        number1 = Float.parseFloat(currentValue);
        result.setText("");
        currentValue = "";

        float output = calculate.calculateUnaryNumber(number1,operator);
        result.setText(String.valueOf(output));
        operator = "";

    }

    @FXML
    public void clearFunction(ActionEvent event) {
        operator="";
        start=true;
        result.setText("");
        expression.setText("");
        currentValue = "";
    }
}
