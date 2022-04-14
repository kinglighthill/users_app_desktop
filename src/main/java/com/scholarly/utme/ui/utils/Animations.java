package com.scholarly.utme.ui.utils;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {

    public static void translateIn(Node node) {
        node.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(node);

        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setFromX(1);

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setDuration(Duration.millis(200));
        scaleTransition.setNode(node);

        scaleTransition.play();
    }

    public static void translateOut(Node node) {
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(node);

        fadeTransition.setOnFinished(event -> {
            node.setVisible(false);
        });

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);
        scaleTransition.setDuration(Duration.millis(300));
        scaleTransition.setNode(node);

        scaleTransition.setOnFinished(event -> {
            node.setVisible(false);
        });

        scaleTransition.play();
    }
}
