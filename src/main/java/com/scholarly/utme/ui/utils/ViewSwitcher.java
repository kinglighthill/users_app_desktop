package com.scholarly.utme.ui.utils;

import com.scholarly.utme.MainApplication;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class ViewSwitcher {
    private static Stage stage;
    private static final Scene rootScene = new Scene(new Pane());

    private static View currentView;

    public static void showScreen(View view) {
        try {
            MainApplication.timeTakenTo("start showing screen (" + view.getControllerClass().getSimpleName() + ")");
//            MainApplication.logInfo("Loading " + view.getControllerClass().getName());
            Class<? extends FxmlView<? extends ViewModel>> myClass = view.getControllerClass();
            MainApplication.timeTakenTo("get class");
            FluentViewLoader.FxmlViewStep<? extends FxmlView<? extends ViewModel>, ? extends ViewModel> step = FluentViewLoader.fxmlView(myClass);
            MainApplication.timeTakenTo("get step");
            ViewTuple<? extends FxmlView<? extends ViewModel>, ? extends ViewModel> tuple = step.load();
            MainApplication.timeTakenTo("get tuple");
            Parent root = tuple.getView();
//            Parent root = FluentViewLoader.fxmlView(view.getControllerClass()).load().getView();
            MainApplication.timeTakenTo("get view");

//            MainApplication.logInfo("Loading css");
            String cssResource = MainApplication.class.getResource("/styles/main.css").toExternalForm();
            MainApplication.timeTakenTo("get css");
//            MainApplication.logInfo("Adding css");
            root.getStylesheets().add(cssResource);
            MainApplication.timeTakenTo("load css");
//            root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

//            MainApplication.logInfo("Setting view");
            currentView = view;
            MainApplication.timeTakenTo("set view");
//            MainApplication.logInfo("Setting root");
            rootScene.setRoot(root);
            MainApplication.timeTakenTo("set root");
//            MainApplication.logInfo("Done");
            MainApplication.timeTakenTo("finish showing screen (" + view.getControllerClass().getSimpleName() + ")");
        } catch (Exception e) {
            MainApplication.log(e);
            e.printStackTrace();
        }
    }

    /*public static void showScreen(View view) {
        try {
            Parent root = FluentViewLoader.fxmlView(view.getControllerClass()).load().getView();
            String cssResource = Objects.requireNonNull(MainApplication.class.getResource("/styles/main.css")).toExternalForm();
            root.getStylesheets().add(cssResource);
            currentView = view;
            rootScene.setRoot(root);
        } catch (Exception e) {
            MainApplication.log(e);
            e.printStackTrace();
        }
    }*/

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
