package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenVM;
import com.scholarly.utme.viewmodels.landing_screens.*;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen.fxml")
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

    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FF9900; -fx-border-width: 0 0 0 5;";
    private static final String IDLE_STYLE = "-fx-background-color: #006B17;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ViewTuple<LandingScreenHomeController, LandingScreenHomeVM> homeViewTuple = FluentViewLoader.fxmlView(LandingScreenHomeController.class).load();
        Parent homeView = homeViewTuple.getView();

        ViewTuple<LandingScreenAccountController, LandingScreenAccountVM> accountViewTuple = FluentViewLoader.fxmlView(LandingScreenAccountController.class).load();
        Parent accountView = accountViewTuple.getView();

        ViewTuple<LandingScreenActivateController, LandingScreenActivateVM> activateViewTuple = FluentViewLoader.fxmlView(LandingScreenActivateController.class).load();
        Parent activateView = activateViewTuple.getView();

        ViewTuple<LandingScreenAppsController, LandingScreenAppsVM> appsViewTuple = FluentViewLoader.fxmlView(LandingScreenAppsController.class).load();
        Parent appsView = appsViewTuple.getView();

        ViewTuple<LandingScreenTriviaController, LandingScreenTriviaVM> triviaViewTuple = FluentViewLoader.fxmlView(LandingScreenTriviaController.class).load();
        Parent triviaView = triviaViewTuple.getView();

        ViewTuple<LandingScreenPerformanceController, LandingScreenPerformanceVM> performanceViewTuple = FluentViewLoader.fxmlView(LandingScreenPerformanceController.class).load();
        Parent performanceView = performanceViewTuple.getView();

        ViewTuple<LandingScreenUpdatesController, LandingScreenUpdatesVM> updatesViewTuple = FluentViewLoader.fxmlView(LandingScreenUpdatesController.class).load();
        Parent updatesView = updatesViewTuple.getView();

        ViewTuple<LandingScreenSettingsController, LandingScreenSettingsVM> settingsViewTuple = FluentViewLoader.fxmlView(LandingScreenSettingsController.class).load();
        Parent settingsView = settingsViewTuple.getView();

        homeContentPane.getChildren().add(homeView);

        initializeViews();

        initializeFonts();


        toggleGroup.getToggles().addAll(homeButton, accountButton, activateButton, appsButton, triviaButton, performanceButton, updatesButton, settingsButton);

        if (ViewSwitcher.retrieveData() == null) {
            selectButton(homeView, homeButton);
        }else {
//            System.out.println("Retrieved data -> " + ViewSwitcher.retrieveData());
            String retrievedId = (String) ViewSwitcher.retrieveData();

            if (retrievedId.equalsIgnoreCase("homeButton")) {
                selectButton(homeView, homeButton);

            } else if (retrievedId.equalsIgnoreCase("accountScreen")) {
                selectButton(accountView, accountButton);

            } else if (retrievedId.equalsIgnoreCase("activateButton")) {
                selectButton(activateView, activateButton);

            } else if (retrievedId.equalsIgnoreCase("appsButton")) {
                selectButton(appsView, appsButton);

            } else if (retrievedId.equalsIgnoreCase("triviaButton")) {
                selectButton(triviaView, triviaButton);

            } else if (retrievedId.equalsIgnoreCase("performanceButton")) {
                selectButton(performanceView, performanceButton);

            } else if (retrievedId.equalsIgnoreCase("updatesButton")) {
                selectButton(updatesView, updatesButton);

            } else if (retrievedId.equalsIgnoreCase("settingsButton")) {
                selectButton(settingsView, settingsButton);
            }
        }


        homeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectButton(homeView, homeButton);
            }
         });

        accountButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue){
                 selectButton(accountView, accountButton);
             }
         });

        activateButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(activateView, activateButton);
            }
        });

        appsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(appsView, appsButton);
            }
        });

        triviaButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(triviaView, triviaButton);
            }
        });

        performanceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(performanceView, performanceButton);
            }
        });

        updatesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(updatesView, updatesButton);
            }
        });

        settingsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(settingsView, settingsButton);
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

    private void changeButtonStyle(ToggleButton pressedButton) {
        homeButton.setStyle(null);
        accountButton.setStyle(null);
        activateButton.setStyle(null);
        appsButton.setStyle(null);
        triviaButton.setStyle(null);
        performanceButton.setStyle(null);
        updatesButton.setStyle(null);
        settingsButton.setStyle(null);

        pressedButton.setStyle(PRESSED_BUTTON_STYLE);
    }

    private void selectButton(Parent view, ToggleButton toggleButton) {
        homeContentPane.getChildren().clear();
        homeContentPane.getChildren().add(view);
        changeButtonStyle(toggleButton);
    }

}
