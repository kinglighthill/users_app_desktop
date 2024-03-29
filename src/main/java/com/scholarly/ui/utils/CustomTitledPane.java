package com.scholarly.ui.utils;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomTitledPane extends TitledPane {

    public CustomTitledPane() {
        setAnimated(true);
        setCollapsible(true);
        ImageView img = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toExternalForm()));
        img.setFitHeight(10d);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        setGraphic(img);
        setContentDisplay(ContentDisplay.RIGHT);
    }
}
