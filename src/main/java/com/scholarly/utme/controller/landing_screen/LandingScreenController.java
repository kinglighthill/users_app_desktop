package com.scholarly.utme.controller.landing_screen;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screen.LandingScreenVM;
import com.scholarly.utme.viewmodels.landing_screen.*;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen/landing_screen.fxml")
public class LandingScreenController implements FxmlView<LandingScreenVM>, Initializable {

    @FXML
    public ImageView profileImage, handImage, bell, appImage;

    @FXML
    public StackPane homeContentPane;

    @FXML
    private ListView<Course> recentlyViewedListView;

    @FXML
    private ToggleButton homeButton, accountButton, activateButton, appsButton, triviaButton, performanceButton, updatesButton, settingsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label scholarlyText, helloText, startLearningText;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private static final String PRESSED_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1);";
    private static final String IDLE_STYLE = "-fx-background-color: #006B17;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ViewTuple<LandingScreenHomeController, LandingScreenHomeVM> homeViewTuple = FluentViewLoader.fxmlView(LandingScreenHomeController.class).load();

        ViewTuple<LandingScreenAccountController, LandingScreenAccountVM> accountViewTuple = FluentViewLoader.fxmlView(LandingScreenAccountController.class).load();

        ViewTuple<LandingScreenActivateController, LandingScreenActivateVM> activateViewTuple = FluentViewLoader.fxmlView(LandingScreenActivateController.class).load();

        ViewTuple<LandingScreenAppsController, LandingScreenAppsVM> appsViewTuple = FluentViewLoader.fxmlView(LandingScreenAppsController.class).load();

        ViewTuple<LandingScreenUpdatesController, LandingScreenUpdatesVM> updatesViewTuple = FluentViewLoader.fxmlView(LandingScreenUpdatesController.class).load();

        homeContentPane.getChildren().add(homeViewTuple.getView());

        initializeViews();

        initializeFonts();


        /*final Circle clip = new Circle(15, 15, 15);
        profileImage.setClip(clip);

         try {
            profileImage.setImage(new Image(getClass().getResource("/drawable/profileImage.jpg").toString()));
            appIcon.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        toggleGroup.getToggles().addAll(homeButton, accountButton, activateButton, appsButton, triviaButton, performanceButton, updatesButton, settingsButton);

        toggleGroup.selectToggle(homeButton);

        homeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(homeViewTuple.getView());
                homeButton.setStyle(PRESSED_STYLE);

                accountButton.setStyle(null);
                activateButton.setStyle(null);
                appsButton.setStyle(null);
                triviaButton.setStyle(null);
                performanceButton.setStyle(null);
                updatesButton.setStyle(null);
                settingsButton.setStyle(null);
            }
         });

        accountButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue){
                 homeContentPane.getChildren().clear();
                 homeContentPane.getChildren().add(accountViewTuple.getView());
                 accountButton.setStyle(PRESSED_STYLE);

                 homeButton.setStyle(null);
                 activateButton.setStyle(null);
                 appsButton.setStyle(null);
                 triviaButton.setStyle(null);
                 performanceButton.setStyle(null);
                 updatesButton.setStyle(null);
                 settingsButton.setStyle(null);
             }
         });

        activateButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(activateViewTuple.getView());
                activateButton.setStyle(PRESSED_STYLE);

                homeButton.setStyle(null);
                accountButton.setStyle(null);
                appsButton.setStyle(null);
                triviaButton.setStyle(null);
                performanceButton.setStyle(null);
                updatesButton.setStyle(null);
                settingsButton.setStyle(null);
            }
        });

        appsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(appsViewTuple.getView());
                appsButton.setStyle(PRESSED_STYLE);

                homeButton.setStyle(null);
                accountButton.setStyle(null);
                activateButton.setStyle(null);
                triviaButton.setStyle(null);
                performanceButton.setStyle(null);
                updatesButton.setStyle(null);
                settingsButton.setStyle(null);
            }
        });

        triviaButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(appsViewTuple.getView());
                triviaButton.setStyle(PRESSED_STYLE);

                homeButton.setStyle(null);
                accountButton.setStyle(null);
                activateButton.setStyle(null);
                appsButton.setStyle(null);
                performanceButton.setStyle(null);
                updatesButton.setStyle(null);
                settingsButton.setStyle(null);
            }
        });

        performanceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(appsViewTuple.getView());
                performanceButton.setStyle(PRESSED_STYLE);

                homeButton.setStyle(null);
                accountButton.setStyle(null);
                activateButton.setStyle(null);
                appsButton.setStyle(null);
                triviaButton.setStyle(null);
                updatesButton.setStyle(null);
                settingsButton.setStyle(null);
            }
        });

        updatesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(updatesViewTuple.getView());
                updatesButton.setStyle(PRESSED_STYLE);

                homeButton.setStyle(null);
                accountButton.setStyle(null);
                activateButton.setStyle(null);
                appsButton.setStyle(null);
                triviaButton.setStyle(null);
                performanceButton.setStyle(null);
                settingsButton.setStyle(null);
            }
        });

        settingsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                homeContentPane.getChildren().clear();
                homeContentPane.getChildren().add(appsViewTuple.getView());
                settingsButton.setStyle(PRESSED_STYLE);

                homeButton.setStyle(null);
                accountButton.setStyle(null);
                activateButton.setStyle(null);
                appsButton.setStyle(null);
                triviaButton.setStyle(null);
                performanceButton.setStyle(null);
                updatesButton.setStyle(null);
            }
        });

        /*logoutButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.PRE_AUTHENTICATION_SCREEN);
        });*/

    }

    private void initializeViews() {
        appImage.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));

        homeButton.setBackground(Background.EMPTY);
        homeButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/home_icon.png").toString())));
        homeButton.setGraphicTextGap(20);

        accountButton.setBackground(Background.EMPTY);
        accountButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/account_icon.png").toString())));
        accountButton.setGraphicTextGap(20);

        activateButton.setBackground(Background.EMPTY);
        activateButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/activate_icon.png").toString())));
        activateButton.setGraphicTextGap(20);

        appsButton.setBackground(Background.EMPTY);
        appsButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/apps_icon.png").toString())));
        appsButton.setGraphicTextGap(20);

        triviaButton.setBackground(Background.EMPTY);
        triviaButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/trivia_icon.png").toString())));
        triviaButton.setGraphicTextGap(20);

        performanceButton.setBackground(Background.EMPTY);
        performanceButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/performance_icon.png").toString())));
        performanceButton.setGraphicTextGap(20);

        updatesButton.setBackground(Background.EMPTY);
        updatesButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/updates_icon.png").toString())));
        updatesButton.setGraphicTextGap(20);

        settingsButton.setBackground(Background.EMPTY);
        settingsButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/settings_icon.png").toString())));
        settingsButton.setGraphicTextGap(20);
    }

    private void initializeFonts() {
        homeButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        accountButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        activateButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        appsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        triviaButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        performanceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        updatesButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        settingsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.TWENTY.size));

    }
}
