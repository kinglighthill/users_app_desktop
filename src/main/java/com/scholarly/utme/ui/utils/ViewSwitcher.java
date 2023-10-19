package com.scholarly.utme.ui.utils;

import com.scholarly.utme.MainApplication;
import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewSwitcher {
    private static Stage stage;
    private static final Scene rootScene = new Scene(new Pane());

    private static View currentView;

    public static void showScreen(View view) {
        try {
            MainApplication.logInfo("Loading " + view.getControllerClass().getName());
            Parent root = FluentViewLoader.fxmlView(view.getControllerClass()).load().getView();

            MainApplication.logInfo("Loading css");
            String cssResource = MainApplication.class.getResource("/styles/main.css").toExternalForm();
            MainApplication.logInfo("Adding css");
            root.getStylesheets().add(cssResource);
//            root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            MainApplication.logInfo("Setting view");
            currentView = view;
            MainApplication.logInfo("Setting root");
            rootScene.setRoot(root);
            MainApplication.logInfo("Done");
        } catch (Exception e) {
            MainApplication.log(e);
            e.printStackTrace();
        }
    }

    public static void setStage(Stage stage) {
        ViewSwitcher.stage = stage;
        stage.setMaximized(true);
        stage.setScene(rootScene);
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void passData(Object data) {
        try {
            MainApplication.logInfo("Passing data: " + data.toString());
            stage.setUserData(data);
            MainApplication.logInfo("Done");
        } catch (Exception e) {
            MainApplication.log(e);
        }
    }

    public static Object retrieveData() {
        return stage.getUserData();
    }

    public static Scene getRootScene() {
        return rootScene;
    }

    public static View getCurrentView() {
        return currentView;
    }
}
