package com.scholarly.utme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/layouts/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Scholarly UTME");
        stage.setMaximized(true);
        stage.setScene(scene);

        try {
            InputStream iconStream = HelloApplication.class.getResourceAsStream("/drawable/app_logo.png");
            assert iconStream != null;
            Image icon = new Image(iconStream);

            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}