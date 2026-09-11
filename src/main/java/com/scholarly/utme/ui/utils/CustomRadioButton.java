package com.scholarly.utme.ui.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class CustomRadioButton extends StackPane {
    private final HBox content = new HBox();
    private final Label optionLetter = new Label();
    private final Label optionText = new Label();

    public CustomRadioButton(String optionLetter, String optionText) {

        this.optionLetter.setText(optionLetter);
        this.optionText.setText(optionText);

        setPadding(new Insets(10, 20, 10, 20));
        setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 50; ");

        createButton();
    }

    @Override
    protected double computePrefWidth(double height) {
        super.computePrefWidth(height);

        return height / 1.5;
    }

    @Override
    protected double computePrefHeight(double width) {
        super.computePrefHeight(width);

        return width / 2;
    }

    private void createButton() {
        content.setSpacing(50);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(optionLetter, optionText);

        Circle radioCircle = new Circle(10);
        radioCircle.setFill(Paint.valueOf("#FFFFFF"));
        radioCircle.setStroke(Paint.valueOf("#FFFF00"));
        radioCircle.setStrokeWidth(2.0);

        setAlignment(content, Pos.CENTER_LEFT);
        setAlignment(radioCircle, Pos.CENTER_RIGHT);

        getChildren().addAll(content, radioCircle);
    }


}
