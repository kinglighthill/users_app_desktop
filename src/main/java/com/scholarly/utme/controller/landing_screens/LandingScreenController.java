package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenVM;
import de.saxsys.mvvmfx.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_HOME_SCREEN_ACTIVATE_PROMPT_REMOVED;

@FxmlPath("/layouts/landing_screens/landing_screen.fxml")
public class LandingScreenController implements FxmlView<LandingScreenVM>, Initializable {
    private static final String TAG = "LandingScreenController: ";

    @InjectViewModel
    private LandingScreenVM viewModel;

    @FXML
    public ImageView profileImage, handImage, bell, appImage, activateCloseIcon, activateInfoIcon;
    @FXML
    private Panel activatePanel;
    @FXML
    public StackPane homeContentPane;
    @FXML
    private ListView<Course> recentlyViewedListView;
    @FXML
    private ToggleButton homeButton, accountButton, activateButton, appsButton, triviaButton, performanceButton, updatesButton, settingsButton;

    @FXML
    private Button logoutButton, topActivateButton;
    @FXML
    private VBox activateVBox;
    @FXML
    private Label scholarlyText, helloText, startLearningText;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FF9900; -fx-border-width: 0 0 0 5;";

    private Preferences preferences;

    private static Parent homeView;
    private static Parent accountView;
    private static Parent activateView;
    private static Parent appsView;
    private static Parent settingsView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String userId = viewModel.getUserId();
        preferences = AppPreferences.getPreferences();

        long start = System.currentTimeMillis();

        Parent homeView = FluentViewLoader.fxmlView(LandingScreenHomeController.class).load().getView();
//        homeView = homeView == null ? FluentViewLoader.fxmlView(LandingScreenHomeController.class).load().getView() : homeView;

        Parent accountView = FluentViewLoader.fxmlView(LandingScreenAccountController.class).load().getView();
//        accountView = accountView == null ? FluentViewLoader.fxmlView(LandingScreenAccountController.class).load().getView() : accountView;

        Parent activateView = FluentViewLoader.fxmlView(LandingScreenActivateController.class).load().getView();
//        activateView = activateView == null ? FluentViewLoader.fxmlView(LandingScreenActivateController.class).load().getView() : activateView;

//        Parent appsView = FluentViewLoader.fxmlView(LandingScreenAppsController.class).load().getView();
        appsView = appsView == null ? FluentViewLoader.fxmlView(LandingScreenAppsController.class).load().getView() : appsView;

//        Parent settingsView = FluentViewLoader.fxmlView(LandingScreenSettingsController.class).load().getView();
        settingsView = settingsView == null ? FluentViewLoader.fxmlView(LandingScreenSettingsController.class).load().getView() : settingsView;

        long end = System.currentTimeMillis();
        System.out.println(TAG + "Time taken to load Views -> " + (end - start)+"ms");

        homeContentPane.getChildren().add(homeView);

        viewModel.processInitialData(getInitialData());

        initializeViews();

        initializeFonts();

        if (viewModel.getScreenToShow().equals(Screens.ACCOUNT_SCREEN)) {
            selectButton(accountView, accountButton);
        } else if (viewModel.getScreenToShow().equals(Screens.ACTIVATE_SCREEN)) {
            selectButton(activateView, activateButton);
        } else if (viewModel.getScreenToShow().equals(Screens.APPS_SCREEN)) {
            selectButton(appsView, appsButton);
        } else if (viewModel.getScreenToShow().equals(Screens.SETTINGS_SCREEN)) {
            selectButton(settingsView, settingsButton);
        } else {
            selectButton(homeView, homeButton);
        }
        /* else if (viewModel.getScreenToShow().equalsIgnoreCase("triviaScreen")) {
            selectButton(triviaView, triviaButton);
        } else if (viewModel.getScreenToShow().equalsIgnoreCase("performanceScreen")) {
            selectButton(performanceView, performanceButton);
        } else if (viewModel.getScreenToShow().equalsIgnoreCase("updatesScreen")) {
            selectButton(updatesView, updatesButton);
        }*/

        /*if (viewModel.getSelectedScreen().equalsIgnoreCase("homeScreen")) {
            selectButton(homeView, homeButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase("accountScreen")) {
            selectButton(accountView, accountButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase("activateScreen")) {
            selectButton(activateView, activateButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase("appsScreen")) {
            selectButton(appsView, appsButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase("triviaScreen")) {
            selectButton(triviaView, triviaButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase("performanceScreen")) {
            selectButton(performanceView, performanceButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase("updatesScreen")) {
            selectButton(updatesView, updatesButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase("settingsScreen")) {
            selectButton(settingsView, settingsButton);
        }*/


        toggleGroup.getToggles().addAll(homeButton, accountButton, activateButton, appsButton, settingsButton);

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

        /*triviaButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(triviaView, triviaButton);
            }
        });*/

//        performanceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue){
//                selectButton(performanceView, performanceButton);
//            }
//        });

//        updatesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue){
//                selectButton(updatesView, updatesButton);
//            }
//        });

        settingsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                selectButton(settingsView, settingsButton);
            }
        });

        /*logoutButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.PRE_AUTHENTICATION_SCREEN);
        });*/

        topActivateButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.ACTIVATE_PAYMENT_SCREEN);
        });

        if (viewModel.isActivated()) {
            activateVBox.getChildren().remove(activatePanel);
        } else {
            if (!activateVBox.getChildren().contains(activatePanel)) {
                activateVBox.getChildren().add(activatePanel);
            }
        }

        if (preferences.getBoolean(PREF_KEY_HOME_SCREEN_ACTIVATE_PROMPT_REMOVED+userId, false)) {
            activateVBox.getChildren().remove(activatePanel);
        }
        activateCloseIcon.setOnMouseClicked(event -> {
            preferences.putBoolean(PREF_KEY_HOME_SCREEN_ACTIVATE_PROMPT_REMOVED+userId, activateVBox.getChildren().remove(activatePanel));
        });

    }

    private void initializeViews() {
        appImage.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));
        activateCloseIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/activate_close_icon.png").toString()));
        activateInfoIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/activate_info_icon.png").toString()));
        topActivateButton.setBackground(Background.EMPTY);

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

        /*triviaButton.setBackground(Background.EMPTY);
        triviaButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/trivia_icon.png").toString())));
        triviaButton.setGraphicTextGap(20);*/

//        performanceButton.setBackground(Background.EMPTY);
//        performanceButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/performance_icon.png").toString())));
//        performanceButton.setGraphicTextGap(20);

//        updatesButton.setBackground(Background.EMPTY);
//        updatesButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/updates_icon.png").toString())));
//        updatesButton.setGraphicTextGap(20);

        settingsButton.setBackground(Background.EMPTY);
        settingsButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/landing_screen_images/settings_icon.png").toString())));
        settingsButton.setGraphicTextGap(20);
    }

    private void initializeFonts() {
        homeButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        accountButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        activateButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        appsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
//        triviaButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
//        performanceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
//        updatesButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        settingsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));

        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 22));
    }

    private void changeButtonStyle(ToggleButton pressedButton) {
        homeButton.setStyle(null);
        accountButton.setStyle(null);
        activateButton.setStyle(null);
        appsButton.setStyle(null);
//        triviaButton.setStyle(null);
//        performanceButton.setStyle(null);
//        updatesButton.setStyle(null);
        settingsButton.setStyle(null);

        pressedButton.setStyle(PRESSED_BUTTON_STYLE);
    }

    private void selectButton(Parent view, ToggleButton toggleButton) {
        homeContentPane.getChildren().clear();
        homeContentPane.getChildren().add(view);
        changeButtonStyle(toggleButton);
    }

    private InitialData getInitialData() {
        if (ViewSwitcher.retrieveData() != null) {
            return (InitialData) ViewSwitcher.retrieveData();
        } else {
            return null;
        }
    }

     public static class InitialData {
        private Screens screenToShow;

        public InitialData(Screens screenToShow) {
            this.screenToShow = screenToShow;
        }

         public Screens getScreenToShow() {
            return screenToShow;
        }
    }
}
