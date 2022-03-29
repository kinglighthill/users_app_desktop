package com.scholarly.utme.ui.utils;


import com.scholarly.utme.controller.SubjectListViewController;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Helper class to show alerts
 */
public class Alerts {

    public static Alert info(Class<?> thisClass, String windowTitle, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(description);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(thisClass.getResource("/drawable/app_logo.png").toString()));

        return alert;
    }


    public static Alert error(Class<?> thisClass, String windowTitle, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(description);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(thisClass.getResource("/drawable/app_logo.png").toString()));
        return alert;
    }
}
