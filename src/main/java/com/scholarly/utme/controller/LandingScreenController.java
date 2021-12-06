package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.cellFactories.RecentlyViewedCellFactory;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.LandingScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen.fxml")
public class LandingScreenController implements FxmlView<LandingScreenVM>, Initializable {

    public ImageView profileImage, appIcon, bell;

    @FXML
    private ListView<Course> recentlyViewedListView;

    @FXML private FlowPane flowPane;

    @FXML private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Circle clip = new Circle(15, 15, 15);
        profileImage.setClip(clip);

        String defaultImageURL = getClass().getResource("/drawable/app_logo.png").toString();
        ObservableList<Course> items = FXCollections.observableArrayList(
                new Course("Math 101", defaultImageURL),
                new Course("Chem 202", defaultImageURL),
                new Course("Eng 103", defaultImageURL),
                new Course("Phy 212", defaultImageURL)
        );

        recentlyViewedListView.setItems(items);

        recentlyViewedListView.setCellFactory(new RecentlyViewedCellFactory());

        try {
            profileImage.setImage(new Image(getClass().getResource("/drawable/profileImage.jpg").toString()));
            appIcon.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));
            bell.setImage(new Image(getClass().getResource("/drawable/bell_icon.png").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Flow pane setup

        Button button1 = new Button("Practice");
        Button button2 = new Button("Lesson");
        Button button3 = new Button("Videos");
        Button button4 = new Button("Audio");
        Button button5 = new Button("Novels");
        Button button6 = new Button("Learning Center");

        flowPane.getChildren().addAll(button1, button2, button3, button4, button5, button6);

        logoutButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
        });
    }
}
