package com.scholarly.utme.controller;

import com.scholarly.utme.controller.landing_screen.*;
import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.LandingScreenVM;
import com.scholarly.utme.viewmodels.landing_screen.*;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen.fxml")
public class LandingScreenController implements FxmlView<LandingScreenVM>, Initializable {

    @FXML
    public ImageView profileImage, appIcon, bell;

    @FXML
    public StackPane homeContentPane;

    @FXML
    private ListView<Course> recentlyViewedListView;

    @FXML
    private ToggleButton homeButton, accountButton, activateButton, appsButton, updatesButton;

    @FXML
    private Button logoutButton;

    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ViewTuple<LandingScreenHomeController, LandingScreenHomeVM> homeViewTuple = FluentViewLoader.fxmlView(LandingScreenHomeController.class).load();

        ViewTuple<LandingScreenAccountController, LandingScreenAccountVM> accountViewTuple = FluentViewLoader.fxmlView(LandingScreenAccountController.class).load();

        ViewTuple<LandingScreenActivateController, LandingScreenActivateVM> activateViewTuple = FluentViewLoader.fxmlView(LandingScreenActivateController.class).load();

        ViewTuple<LandingScreenAppsController, LandingScreenAppsVM> appsViewTuple = FluentViewLoader.fxmlView(LandingScreenAppsController.class).load();

        ViewTuple<LandingScreenUpdatesController, LandingScreenUpdatesVM> updatesViewTuple = FluentViewLoader.fxmlView(LandingScreenUpdatesController.class).load();

        homeContentPane.getChildren().add(homeViewTuple.getView());

        toggleGroup = new ToggleGroup();

        final Circle clip = new Circle(15, 15, 15);
        profileImage.setClip(clip);

         try {
            profileImage.setImage(new Image(getClass().getResource("/drawable/profileImage.jpg").toString()));
            appIcon.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));
            bell.setImage(new Image(getClass().getResource("/drawable/bell_icon.png").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

         toggleGroup.getToggles().addAll(homeButton, accountButton, activateButton, appsButton, updatesButton);

         toggleGroup.selectToggle(homeButton);

         homeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue){
                 homeContentPane.getChildren().clear();
                 homeContentPane.getChildren().add(homeViewTuple.getView());
             }
         });

         accountButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue){
                 homeContentPane.getChildren().clear();
                 homeContentPane.getChildren().add(accountViewTuple.getView());
             }
         });

        activateButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(activateViewTuple.getView());
            }
        });

        appsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(appsViewTuple.getView());
            }
        });

        updatesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(updatesViewTuple.getView());
            }
        });


        logoutButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
        });

    }
}
