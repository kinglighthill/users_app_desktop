package com.scholarly.utme.ui.utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import netscape.javascript.JSException;

public class CustomListView<T> extends Region{
    final ListView<T> listView = new ListView<>();

    public CustomListView() {

    }

    public void setItems(ObservableList<T> value) {
        listView.setPrefHeight(5);

        widthProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            Double width = (Double) newValue;
            System.out.println("Region width changed: " + width);
            listView.setPrefWidth(width);
            adjustHeight();
        });

        listView.setItems(value);

        getChildren().add(listView);
    }

    public void setCellFactory(Callback<ListView<T>, ListCell<T>> callback) {
        listView.setCellFactory(callback);
    }

    private void adjustHeight() {
        Platform.runLater(() -> {
            try {
                double height = listView.getPrefHeight();
                listView.setPrefHeight(height + 10);
                System.out.println("height on state: " + height + " prefh: " + listView.getPrefHeight());
            } catch (JSException e) {
                // not important
            }
        });
    }

}
