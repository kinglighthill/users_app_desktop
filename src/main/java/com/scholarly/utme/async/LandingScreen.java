package com.scholarly.utme.async;

import com.scholarly.utme.controller.landing_screens.LandingScreenAccountController;
import com.scholarly.utme.controller.landing_screens.LandingScreenActivateController;
import com.scholarly.utme.controller.landing_screens.LandingScreenHomeController;
import com.scholarly.utme.controller.landing_screens.LandingScreenSettingsController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LandingScreen {
    private static LandingScreen instance = null;

    private final SimpleObjectProperty<Parent> homeView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Parent> accountView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Parent> activateView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Parent> settingsView = new SimpleObjectProperty<>();

    private LandingScreen() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        LandingScreenTask homeTask = new LandingScreenTask(LandingScreenHomeController.class);
        LandingScreenTask accountTask = new LandingScreenTask(LandingScreenAccountController.class);
        LandingScreenTask activateTask = new LandingScreenTask(LandingScreenActivateController.class);
        LandingScreenTask settingsTask = new LandingScreenTask(LandingScreenSettingsController.class);

        homeView.bind(homeTask.valueProperty());
        accountView.bind(accountTask.valueProperty());
        activateView.bind(activateTask.valueProperty());
        settingsView.bind(settingsTask.valueProperty());

        executorService.execute(homeTask);
        executorService.execute(accountTask);
        executorService.execute(activateTask);
        executorService.execute(settingsTask);

        executorService.shutdown();
    }

    public static synchronized LandingScreen getInstance() {
        if (instance == null)
            instance = new LandingScreen();

        return instance;
    }

    public static void logOut() {
        instance = null;
    }

    public SimpleObjectProperty<Parent> getHomeView() {
        return homeView;
    }

    public SimpleObjectProperty<Parent> getAccountView() {
        return accountView;
    }

    public SimpleObjectProperty<Parent> getActivateView() {
        return activateView;
    }

    public SimpleObjectProperty<Parent> getSettingsView() {
        return settingsView;
    }
}
