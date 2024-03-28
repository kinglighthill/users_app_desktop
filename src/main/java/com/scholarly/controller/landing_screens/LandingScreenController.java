package com.scholarly.controller.landing_screens;

import com.scholarly.MainApplication;
import com.scholarly.data.model.Course;
import com.scholarly.ui.utils.FontUtil;
import com.scholarly.ui.utils.Screens;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import com.scholarly.async.LandingScreen;
import com.scholarly.util.Constants;
import com.scholarly.util.PreferencesManager;
import com.scholarly.viewmodels.landing_screens.*;
import de.saxsys.mvvmfx.*;
import io.reactivex.rxjava3.annotations.NonNull;
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
import java.util.*;

import static com.scholarly.util.Constants.*;

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
    private Label scholarlyText, helloText, startLearningText, activateBarLabel, versionText;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FF9900; -fx-border-width: 0 0 0 5;";
    private static final String HOVER_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FFFFFF; -fx-border-width: 0 0 0 0; -fx-cursor: hand;";


    private static Parent homeView;
    private static Parent accountView;
    private static Parent activateView;
    private static Parent appsView;
    private static Parent settingsView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            viewModel.processInitialData(getInitialData());

            initializeViews();

            initializeFonts();

            appsButton.setVisible(false);

            Screens screenToShow = Objects.requireNonNull(getInitialData()).screenToShow();

            LandingScreen landingScreen = LandingScreen.getInstance();

            homeView = landingScreen.getHomeView().getValue();
            accountView = landingScreen.getAccountView().getValue();
            activateView = landingScreen.getActivateView().getValue();
            settingsView = landingScreen.getSettingsView().getValue();

            if (homeView == null) homeButton.setDisable(true);
            if (accountView == null) accountButton.setDisable(true);
            if (activateView == null) activateButton.setDisable(true);
            if (settingsView == null) settingsButton.setDisable(true);

            if (screenToShow == Screens.ACCOUNT_SCREEN) {
                selectButton(accountView, accountButton);
            } else if (screenToShow == Screens.ACTIVATE_SCREEN) {
                selectButton(activateView, activateButton);
            } else if (screenToShow == Screens.APPS_SCREEN) {
                changeButtonStyle(appsButton);
            } else if (screenToShow == Screens.SETTINGS_SCREEN) {
                selectButton(settingsView, settingsButton);
            } else {
                selectButton(homeView, homeButton);
            }

            landingScreen.getHomeView().addListener((observable, oldValue, newValue) -> {
                homeView = newValue;

                if (newValue != null) homeButton.setDisable(false);

                if (screenToShow == Screens.HOME_SCREEN) {
                    homeContentPane.getChildren().clear();
                    homeContentPane.getChildren().add(homeView);
                    changeButtonStyle(homeButton);
                    MainApplication.timeTakenTo("load home");
                }
            });
            landingScreen.getAccountView().addListener((observable, oldValue, newValue) -> {
                accountView = newValue;

                if (newValue != null) accountButton.setDisable(false);

                if (screenToShow == Screens.ACCOUNT_SCREEN) {
                    homeContentPane.getChildren().clear();
                    homeContentPane.getChildren().add(newValue);
                    changeButtonStyle(accountButton);
                }
            });
            landingScreen.getActivateView().addListener((observable, oldValue, newValue) -> {
                activateView = newValue;

                if (newValue != null) activateButton.setDisable(false);

                if (screenToShow == Screens.ACTIVATE_SCREEN) {
                    homeContentPane.getChildren().clear();
                    homeContentPane.getChildren().add(newValue);
                    changeButtonStyle(activateButton);
                }
            });
            landingScreen.getSettingsView().addListener((observable, oldValue, newValue) -> {
                settingsView = newValue;

                if (newValue != null) settingsButton.setDisable(false);

                if (screenToShow == Screens.SETTINGS_SCREEN) {
                    homeContentPane.getChildren().clear();
                    homeContentPane.getChildren().add(newValue);
                    changeButtonStyle(settingsButton);
                }
            });

            toggleGroup.getToggles().addAll(homeButton, accountButton, activateButton, appsButton, settingsButton);

            homeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    selectButton(homeView, homeButton);
                }
            });
            homeButton.setOnMouseEntered(event -> {
                if (homeButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    homeButton.setStyle(HOVER_BUTTON_STYLE);
            });
            homeButton.setOnMouseExited(event -> {
                if (homeButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    homeButton.setStyle(null);
            });

            accountButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue){
                    selectButton(accountView, accountButton);
                }
            });
            accountButton.setOnMouseEntered(event -> {
                if (accountButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    accountButton.setStyle(HOVER_BUTTON_STYLE);
            });
            accountButton.setOnMouseExited(event -> {
                if (accountButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    accountButton.setStyle(null);
            });

            activateButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue){
                    selectButton(activateView, activateButton);
                }
            });
            activateButton.setOnMouseEntered(event -> {
                if (activateButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    activateButton.setStyle(HOVER_BUTTON_STYLE);
            });
            activateButton.setOnMouseExited(event -> {
                if (activateButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    activateButton.setStyle(null);
            });

            settingsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue){
                    selectButton(settingsView, settingsButton);
                }
            });
            settingsButton.setOnMouseEntered(event -> {
                if (settingsButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    settingsButton.setStyle(HOVER_BUTTON_STYLE);
            });
            settingsButton.setOnMouseExited(event -> {
                if (settingsButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    settingsButton.setStyle(null);
            });

            topActivateButton.setOnAction(event -> {
                ViewSwitcher.passData(new InitialData(Screens.ACTIVATE_SCREEN));
                ViewSwitcher.showScreen(View.LANDING_SCREEN);
            });

            if (viewModel.isActivated()) {
                activateVBox.getChildren().remove(activatePanel);
            } else {
                if (!activateVBox.getChildren().contains(activatePanel)) {
                    activateVBox.getChildren().add(activatePanel);
                }
            }

            String message = PreferencesManager.get(PREF_KEY_ACTIVATE_MESSAGE, "Your device is not activated.");
            activateBarLabel.setText(message);

            if (PreferencesManager.getBoolean(PREF_KEY_HOME_SCREEN_ACTIVATE_PROMPT_REMOVED+viewModel.getUserId(), false)) {
                activateVBox.getChildren().remove(activatePanel);
            }
            activateCloseIcon.setOnMouseClicked(event -> PreferencesManager.putBoolean(PREF_KEY_HOME_SCREEN_ACTIVATE_PROMPT_REMOVED+viewModel.getUserId(), activateVBox.getChildren().remove(activatePanel)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        appImage.setImage(new Image(getClass().getResource("/drawable/app_icon.png").toString()));
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

        versionText.setText(Constants.VERSION_TXT);
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
        if (view != null) {
            homeContentPane.getChildren().clear();
            homeContentPane.getChildren().add(view);
        }
        changeButtonStyle(toggleButton);
    }

    public InitialData getInitialData() {
        if (ViewSwitcher.retrieveData() != null) {
            return (InitialData) ViewSwitcher.retrieveData();
        } else {
            return null;
        }
    }

    public record InitialData(@NonNull Screens screenToShow) {
    }
}
