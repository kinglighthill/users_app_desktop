package com.scholarly.utme;

import com.github.sarxos.webcam.Webcam;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.ui.utils.Alerts;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.PreferencesManager;
import javafx.application.Application;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.scholarly.utme.util.Constants.*;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication: ";

    private Webcam webcam;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            InputStream iconStream = MainApplication.class.getResourceAsStream("/drawable/app_logo.png");
            assert iconStream != null;
            Image icon = new Image(iconStream);

            stage.getIcons().add(icon);
            stage.setTitle("Scholarly JAMB CBT");

//            stage.setWidth(1200);

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ViewSwitcher.setStage(stage);


        stage.setOnCloseRequest(event -> {
            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Exit", null, "Are you sure you want to exit the application?");

            dialog.showAndWait().filter(buttonType -> buttonType != ButtonType.YES).ifPresentOrElse(
                    buttonType -> event.consume(), () -> {
                        System.out.println(TAG + "Screen showing before exit -> " + ViewSwitcher.getCurrentView());
                        PreferencesManager.putBoolean(PREF_KEY_LOGGED_USER_OUT, ViewSwitcher.getCurrentView() == View.AUTHENTICATION_SCREEN || ViewSwitcher.getCurrentView() == View.PRE_AUTHENTICATION_SCREEN || ViewSwitcher.getCurrentView() == View.WELCOME_SCREEN);
                    }
            );
        });

        boolean firstTimeUser = PreferencesManager.getBoolean(PREF_KEY_FIRST_TIME_USER, true);
        if (firstTimeUser) {
            ViewSwitcher.showScreen(View.WELCOME_SCREEN);
        } else {
            boolean userLoggedOut = PreferencesManager.getBoolean(PREF_KEY_LOGGED_USER_OUT, false);
            System.out.println(TAG + "Logged Out User -> " + userLoggedOut);
            if (userLoggedOut) {
//                ViewSwitcher.passData(new AuthenticationController.InitialData(false));
                ViewSwitcher.showScreen(View.PRE_AUTHENTICATION_SCREEN);
            } else {
                ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
                ViewSwitcher.showScreen(View.LANDING_SCREEN);
            }
        }

//        try {
//            webcam = Webcam.getDefault();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        ViewTuple viewTuple = FluentViewLoader.fxmlView(PracticeScreenController.class).load();
//
//        Parent root = viewTuple.getView();
//        stage.setMaximized(true);
//        stage.setScene(new Scene(root));
//        stage.show();
    }

    public File openFileChooser(Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        return fileChooser.showOpenDialog(stage);
    }

    public void openWebcam() {
        System.out.println("Open Webcam called!");
        if (webcam != null) {
            webcam.open(true);
        } else {
            System.out.println("Cannot open Webcam!");
        }
    }

    public void openBrowser(String url) {
        System.out.println(TAG + "Browser");
        getHostServices().showDocument(url);
    }

    public static void main(String[] args) {
        launch();
    }
}