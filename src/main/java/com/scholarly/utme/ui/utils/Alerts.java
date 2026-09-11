package com.scholarly.utme.ui.utils;


import com.scholarly.utme.controller.SubjectListViewController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Modality;
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

    public static Dialog<ButtonType> dialog(Class<?> thisClass, String windowTitle, String header, String description) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(windowTitle);
        dialog.setHeaderText(header);

        DialogPane dialogPane = new DialogPane();
        dialogPane.setContentText(description);
        dialogPane.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        dialogPane.setMinSize(350, 120);
        dialogPane.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        dialog.setDialogPane(dialogPane);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(thisClass.getResource("/drawable/app_logo.png").toString()));
        return dialog;
    }

}
