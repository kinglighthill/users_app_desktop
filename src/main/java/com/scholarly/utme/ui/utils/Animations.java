package com.scholarly.utme.ui.utils;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {

    public static void translateIn(Node node, double duration) {
        node.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setDuration(Duration.millis(duration));
        fadeTransition.setNode(node);

        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setFromX(1);

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setDuration(Duration.millis(duration));
        scaleTransition.setNode(node);

        scaleTransition.play();
    }

    public static void translateOut(Node node, double duration) {
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setDuration(Duration.millis(duration));
        fadeTransition.setNode(node);

        fadeTransition.setOnFinished(event -> node.setVisible(false));

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);
        scaleTransition.setDuration(Duration.millis(duration));
        scaleTransition.setNode(node);

        scaleTransition.setOnFinished(event -> node.setVisible(false));

        scaleTransition.play();
    }

    public static void fadeIn(Node node, double duration) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(duration));
        fadeIn.setNode(node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);

        node.setVisible(true);
        fadeIn.play();

    }

    public static void fadeOut(Node node, double milliseconds) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(milliseconds));
        fadeOut.setNode(node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setCycleCount(1);
        fadeOut.setAutoReverse(false);

        fadeOut.play();

        fadeOut.setOnFinished(event -> node.setVisible(false));

    }

    public static void fadeInAndOut(Node node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(700));
        fadeIn.setNode(node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);

        fadeIn.play();

        node.setVisible(!node.isVisible());

        fadeIn.setOnFinished(event2 -> {
            try {
                Thread.sleep(1200);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(700));
                fadeOut.setNode(node);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setCycleCount(1);
                fadeOut.setAutoReverse(false);

                fadeOut.play();

                fadeOut.setOnFinished(event3 -> node.setVisible(false));
            }

        });
    }

    public static void slideIn(Node node) {
        node.setVisible(true);

        TranslateTransition slideIn = new TranslateTransition();

        slideIn.setFromX(400f);
        slideIn.setToX(0f);
        slideIn.setDuration(Duration.millis(500));
        slideIn.setNode(node);

        slideIn.play();

    }

    public static void slideOut(Node node) {
        TranslateTransition slideOut = new TranslateTransition();

        slideOut.setFromX(0f);
        slideOut.setToX(400f);
        slideOut.setDuration(Duration.millis(500));
        slideOut.setNode(node);

        slideOut.play();

        slideOut.setOnFinished(event -> node.setVisible(false));

    }
}
