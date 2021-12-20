package com.scholarly.utme;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.controller.PracticeScreenController;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.UserDataDatabase;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.HomeScreenVM;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/layouts/authentication_screen.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Scholarly UTME");
//        stage.setScene(scene);

        System.out.println("Databases are ok..." + (Database.isOK() && UserDataDatabase.isOK()));

        try {
            InputStream iconStream = HelloApplication.class.getResourceAsStream("/drawable/app_logo.png");
            assert iconStream != null;
            Image icon = new Image(iconStream);

            stage.getIcons().add(icon);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ViewSwitcher.setStage(stage);
        ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);

//        ViewTuple viewTuple = FluentViewLoader.fxmlView(PracticeScreenController.class).load();
//
//        Parent root = viewTuple.getView();
//        stage.setMaximized(true);
//        stage.setScene(new Scene(root));
//        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}