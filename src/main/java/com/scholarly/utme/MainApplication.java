package com.scholarly.utme;

import com.github.sarxos.webcam.Webcam;
import com.google.gson.Gson;
import com.scholarly.utme.controller.AuthenticationController;
import com.scholarly.utme.controller.landing_screens.*;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.ui.utils.Alerts;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.async.LandingScreen;
import com.scholarly.utme.util.Constants;
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
    private static final String TAG_DEV = "TAG: ";
    private Webcam webcam;

    public static final Logger logger = Logger.getLogger(MainApplication.class.getName());
    public static FileHandler fileHandler = null;

    static long startDisplay = System.currentTimeMillis();

    @Override
    public void start(Stage stage) throws IOException {
        try {
            startDisplay = System.currentTimeMillis();
            fileHandler = new FileHandler("logging.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.info("Welcome to Scholarly.");

            LandingScreen.getInstance();

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
            System.out.println(TAG_DEV + "Time taken to set stage -> " + (System.currentTimeMillis() - startDisplay) + "ms");
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

            PreferencesManager.putBoolean(Constants.PREF_KEY_SHOW_FAVORITE_SUBJECT_DIALOG, true);
            boolean firstTimeUser = PreferencesManager.getBoolean(PREF_KEY_FIRST_TIME_USER, true);
            if (firstTimeUser) {
                ViewSwitcher.showScreen(View.WELCOME_SCREEN);
            } else {
                boolean userLoggedOut = PreferencesManager.getBoolean(PREF_KEY_LOGGED_USER_OUT, false);
                System.out.println(TAG + "Logged Out User -> " + userLoggedOut);
                logger.info(TAG + "Logged Out User -> " + userLoggedOut);

                String userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
                String userDataString = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
                System.out.println(TAG + "User id -> " + userId);
                System.out.println(TAG + "User Data String -> " + userDataString);
                UserData userData = new Gson().fromJson(userDataString, UserData.class);

                if (!userLoggedOut) {
                    logger.info("Move to Landing Screen");
                    ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
                    MainApplication.timeTakenTo("pass data");

                    ViewSwitcher.showScreen(View.LANDING_SCREEN);
                    MainApplication.timeTakenTo("load landing screen");
                } else if (userData == null) {
                    logger.info("Move to Signup Screen");
                    ViewSwitcher.passData(new AuthenticationController.InitialData(true));
                    MainApplication.timeTakenTo("pass data");
                    ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
                } else {
                    logger.info("Move to Login Screen");
                    ViewSwitcher.passData(new AuthenticationController.InitialData(false));
                    MainApplication.timeTakenTo("pass data");
                    ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
                }
                MainApplication.timeTakenTo("load app");
            }
        } catch (Exception e) {
            log(e);
            ViewSwitcher.showScreen(View.WELCOME_SCREEN);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DbConnection.closeConnection();
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
            logger.severe(exception.getMessage());
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

    public static void resetTime() {
        startDisplay = System.currentTimeMillis();
    }

    public static void timeTakenTo(String message) {
        System.out.println(TAG_DEV + "Time taken to " + message + " -> " + (System.currentTimeMillis() - startDisplay) + "ms");
    }
}