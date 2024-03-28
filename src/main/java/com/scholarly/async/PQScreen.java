package com.scholarly.async;

import com.scholarly.controller.SubjectListViewController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.util.Pair;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PQScreen {
    private static PQScreen instance = null;

    private final SimpleObjectProperty<Pair<SubjectListViewController, Parent>> pqView = new SimpleObjectProperty<>();

    private PQScreen() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        PQScreenTask<SubjectListViewController> pqScreenTask = new PQScreenTask<>(SubjectListViewController.class);

        pqView.bind(pqScreenTask.valueProperty());

        executorService.execute(pqScreenTask);

        executorService.shutdown();
    }

    public static synchronized PQScreen getInstance() {
        if (instance == null)
            instance = new PQScreen();

        return instance;
    }

    public static void logOut() {
        instance = null;
    }

    public SimpleObjectProperty<Pair<SubjectListViewController, Parent>> getPQView() {
        return pqView;
    }
}