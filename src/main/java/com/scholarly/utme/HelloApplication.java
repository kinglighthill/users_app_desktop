package com.scholarly.utme;

import com.scholarly.utme.data.dao.NovelObjectiveBookmarkDao;
import com.scholarly.utme.data.dao.ObjectiveBookmarkDao;
import com.scholarly.utme.data.dao.TheoryBookmarkDao;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.NovelsDatabase;
import com.scholarly.utme.data.util.UserDataDatabase;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_FIRST_TIME_USER;

public class HelloApplication extends Application {
    private final Preferences preferences = AppPreferences.getPreferences();

    private static HelloApplication application;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            InputStream iconStream = HelloApplication.class.getResourceAsStream("/drawable/app_logo.png");
            assert iconStream != null;
            Image icon = new Image(iconStream);

            stage.getIcons().add(icon);
            stage.setTitle("Scholarly JAMB CBT");

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ViewSwitcher.setStage(stage);
        boolean firstTimeUser = preferences.getBoolean(PREF_KEY_FIRST_TIME_USER, true);
        if (firstTimeUser) {
            ViewSwitcher.showScreen(View.WELCOME_SCREEN);
        } else {
            ViewSwitcher.showScreen(View.PRE_AUTHENTICATION_SCREEN);
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        }
    }

    public void openBrowser(String uri) {
        System.out.println("Browser");
        getHostServices().showDocument(uri);
    }

    public static void main(String[] args) {
        launch();
    }
}