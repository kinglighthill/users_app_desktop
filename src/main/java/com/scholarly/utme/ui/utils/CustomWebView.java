package com.scholarly.utme.ui.utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;

import java.util.Set;

public class CustomWebView extends StackPane {
    final WebView webview = new WebView();
    final WebEngine webEngine = webview.getEngine();

    public CustomWebView() {

    }

    public void loadContent(String content) {

        webview.setPrefHeight(5);

        this.setPadding(new Insets( 10));

        widthProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            Double width = (Double) newValue;
            System.out.println("Region width changed: " + width);
            webview.setPrefWidth(width);
            adjustHeight();
        });

        webview.getEngine().getLoadWorker().stateProperty().addListener((arg0, oldState, newState) -> {
            if (newState == State.SUCCEEDED) {
                adjustHeight();
            }
        });

        // http://stackoverflow.com/questions/11206942/how-to-hide-scrollbars-in-the-javafx-webview
        webview.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
            Set<Node> scrolls = webview.lookupAll(".scroll-bar");
            for (Node scroll : scrolls) {
                scroll.setVisible(false);
            }
        });

        setContent(content);

        getChildren().addAll(webview);

    }

    private void setContent(String content) {
        Platform.runLater(() -> {
            webEngine.loadContent(getHtml(content));
            Platform.runLater(this::adjustHeight);
        });
    }

    private void adjustHeight() {
        Platform.runLater(() -> {
            try {
                //"document.getElementById('mydiv').offsetHeight"
                Object result = webEngine.executeScript(
                        "document.getElementById('mydiv').offsetHeight");
                if (result instanceof Integer) {
                    Integer i = (Integer) result;
                    double height = Double.valueOf(i);
                    webview.setPrefHeight(height + 10);
                    System.out.println("height on state: " + height + " prefh: " + webview.getPrefHeight());

                }
            } catch (JSException e) {
                // not important
            }
        });
    }

    private String getHtml(String content) {
        return "<html><body>" +
                "<div id=\"mydiv\">" + content + "</div>" +
                "</body></html>";
    }



    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(webview,0,15, w, h,0, HPos.CENTER, VPos.CENTER);
        //layoutInArea(label,0,0, label.getWidth(), label.getHeight(),0, HPos.RIGHT, VPos.BOTTOM);
    }
}
