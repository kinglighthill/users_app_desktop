package com.scholarly.utme.ui.utils;

import com.scholarly.utme.controller.PracticeScreenController;
import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewSwitcher {

    private static Stage stage;
    private static Scene rootScene = new Scene(new Pane());

    public static void showScreen(View view) {
        try {
            Parent root = FluentViewLoader.fxmlView(view.getControllerClass()).load().getView();

            rootScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStage(Stage stage) {
        ViewSwitcher.stage = stage;
        stage.setMaximized(true);
        stage.setScene(rootScene);
        stage.show();
    }

    public static void passData(Object data) {
        stage.setUserData(data);
    }

    public static Object retrieveData() {
        return stage.getUserData();
    }

    public static Scene getRootScene() {
        return rootScene;
    }
}
