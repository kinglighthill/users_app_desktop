package com.scholarly.async;

import com.scholarly.controller.landing_screens.LandingScreenAccountController;
import com.scholarly.controller.landing_screens.LandingScreenActivateController;
import com.scholarly.controller.landing_screens.LandingScreenHomeController;
import com.scholarly.controller.landing_screens.LandingScreenSettingsController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.util.Pair;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LandingScreen {
    private static LandingScreen instance = null;

    private final SimpleObjectProperty<Pair<LandingScreenHomeController, Parent>> homeView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Pair<LandingScreenAccountController, Parent>> accountView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Pair<LandingScreenActivateController, Parent>> activateView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Pair<LandingScreenSettingsController, Parent>> settingsView = new SimpleObjectProperty<>();

    private boolean refreshHomeLastSession = false;

    private LandingScreen() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        LandingScreenTask<LandingScreenHomeController> homeTask = new LandingScreenTask<>(LandingScreenHomeController.class);
        LandingScreenTask<LandingScreenAccountController> accountTask = new LandingScreenTask<>(LandingScreenAccountController.class);
        LandingScreenTask<LandingScreenActivateController> activateTask = new LandingScreenTask<>(LandingScreenActivateController.class);
        LandingScreenTask<LandingScreenSettingsController> settingsTask = new LandingScreenTask<>(LandingScreenSettingsController.class);

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

    public SimpleObjectProperty<Pair<LandingScreenHomeController, Parent>> getHomeView() {
        return homeView;
    }

    public SimpleObjectProperty<Pair<LandingScreenAccountController, Parent>> getAccountView() {
        return accountView;
    }

    public SimpleObjectProperty<Pair<LandingScreenActivateController, Parent>> getActivateView() {
        return activateView;
    }

    public SimpleObjectProperty<Pair<LandingScreenSettingsController, Parent>> getSettingsView() {
        return settingsView;
    }

    public boolean isRefreshHomeLastSession() {
        return refreshHomeLastSession;
    }

    public void refreshHomeLastSession(boolean value) {
        refreshHomeLastSession = value;
    }
}