package com.scholarly.utme;

import com.github.sarxos.webcam.Webcam;
import com.google.gson.Gson;
import com.scholarly.utme.controller.AuthenticationController;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.network.model.UserData;
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
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.scholarly.utme.util.Constants.*;

public class MainApplication extends Application /*implements Thread.UncaughtExceptionHandler*/  {
    private static final String TAG = "MainApplication: ";
    private Webcam webcam;

    public static final Logger logger = Logger.getLogger(MainApplication.class.getName());
    public static FileHandler fileHandler = null;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            fileHandler = new FileHandler("logging.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.info("Welcome to Scholarly.");

            InputStream iconStream = MainApplication.class.getResourceAsStream("/drawable/app_logo.png");
            assert iconStream != null;
            Image icon = new Image(iconStream);

            stage.getIcons().add(icon);
            stage.setTitle("Scholarly JAMB CBT");

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            ViewSwitcher.setStage(stage);
            stage.setOnCloseRequest(event -> {
                Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Exit", null, "Are you sure you want to exit the application?");

                dialog.showAndWait().filter(buttonType -> buttonType != ButtonType.YES).ifPresentOrElse(
                        buttonType -> event.consume(), () -> {
                            System.out.println(TAG + "Screen showing before exit -> " + ViewSwitcher.getCurrentView());
                            PreferencesManager.put(PREF_KEY_LAST_SELECTED_PRACTICE, Screens.PRACTICE_SCREEN.getName());
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
                logger.info(TAG + "Logged Out User -> " + userLoggedOut);

                String userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
                String userDataString = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
                UserData userData = new Gson().fromJson(userDataString, UserData.class);

                if (userLoggedOut || userData == null) {;
                    logger.info("Move to Auth Screen");
                    ViewSwitcher.passData(new AuthenticationController.InitialData(false));
                    ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
                } else {;
                    logger.info("Move to Landing Screen");
                    ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
                    ViewSwitcher.showScreen(View.LANDING_SCREEN);
                }
            }
        } catch (Exception e) {
            log(e);
        }
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

    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        launch();
    }

    public static void log(Exception exception) {
        if (fileHandler != null) {
            for (StackTraceElement element : exception.getStackTrace()) {
                logger.severe(element.toString());
            }
        }
    }

    public static void logInfo(String info) {
        if (fileHandler != null) {
            logger.info(info);
        }
    }
}