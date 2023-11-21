package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.GlobalExceptionHandler;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.landing_screens.*;
import de.saxsys.mvvmfx.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

import static com.scholarly.utme.util.Constants.*;

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
    private Label scholarlyText, helloText, startLearningText, activateBarLabel;

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
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());

        try {
            viewModel.processInitialData(getInitialData());

            initializeViews();

            initializeFonts();

            long start = System.currentTimeMillis();

            appsButton.setVisible(false);

//            Task<Void> homeViewTask = new Task<>() {
//                @Override
//                protected Void call() {
//                    ViewTuple<LandingScreenHomeController, LandingScreenHomeVM> landingScreenHomeViewTuple = FluentViewLoader.fxmlView(LandingScreenHomeController.class).load();
//                    Platform.runLater(() -> {
//                        homeView = landingScreenHomeViewTuple.getView();
//                    });
//                    return null;
//                }
//            };
//            Thread homeViewThread = new Thread(homeViewTask);
//            homeViewThread.start();
            System.out.println(TAG + "Time taken to load Home Screen -> " + (System.currentTimeMillis() - start)+"ms");
            Parent homeView = FluentViewLoader.fxmlView(LandingScreenHomeController.class).load().getView();

            Task<Void> accountViewTask = new Task<>() {
                @Override
                protected Void call() {
                    ViewTuple<LandingScreenAccountController, LandingScreenAccountVM> landingScreenAccountViewTuple = FluentViewLoader.fxmlView(LandingScreenAccountController.class).load();
                    Platform.runLater(() -> {
                        accountView = landingScreenAccountViewTuple.getView();
                    });
                    return null;
                }
            };
            Thread accountViewThread = new Thread(accountViewTask);
            accountViewThread.start();
//            Parent accountView = FluentViewLoader.fxmlView(LandingScreenAccountController.class).load().getView();
//        accountView = accountView == null ? FluentViewLoader.fxmlView(LandingScreenAccountController.class).load().getView() : accountView;
            System.out.println(TAG + "Time taken to load Account Screen -> " + (System.currentTimeMillis() - start)+"ms");

            Task<Void> activateViewTask = new Task<>() {
                @Override
                protected Void call() {
                    ViewTuple<LandingScreenActivateController, LandingScreenActivateVM> landingScreenActivateViewTuple = FluentViewLoader.fxmlView(LandingScreenActivateController.class).load();
                    Platform.runLater(() -> {
                        activateView = landingScreenActivateViewTuple.getView();
                    });
                    return null;
                }
            };
            Thread activateViewThread = new Thread(activateViewTask);
            activateViewThread.start();
//            Parent activateView = FluentViewLoader.fxmlView(LandingScreenActivateController.class).load().getView();

            System.out.println(TAG + "Time taken to load Activate Screen -> " + (System.currentTimeMillis() - start)+"ms");

            Task<Void> appsViewTask = new Task<>() {
                @Override
                protected Void call() {
                    ViewTuple<LandingScreenAppsController, LandingScreenAppsVM> landingScreenAppsViewTuple = FluentViewLoader.fxmlView(LandingScreenAppsController.class).load();
                    Platform.runLater(() -> {
                        appsView = landingScreenAppsViewTuple.getView();
                    });
                    return null;
                }
            };
            Thread appsViewThread = new Thread(appsViewTask);
            appsViewThread.start();
            //        Parent appsView = FluentViewLoader.fxmlView(LandingScreenAppsController.class).load().getView();
//            appsView = appsView == null ? FluentViewLoader.fxmlView(LandingScreenAppsController.class).load().getView() : appsView;


            Task<Void> settingsViewTask = new Task<>() {
                @Override
                protected Void call() {
                    ViewTuple<LandingScreenSettingsController, LandingScreenSettingsVM> landingScreenSettingsViewTuple = FluentViewLoader.fxmlView(LandingScreenSettingsController.class).load();
                    Platform.runLater(() -> {
                        settingsView = landingScreenSettingsViewTuple.getView();
                    });
                    return null;
                }
            };
            Thread settingsViewThread = new Thread(settingsViewTask);
            settingsViewThread.start();
            //        Parent settingsView = FluentViewLoader.fxmlView(LandingScreenSettingsController.class).load().getView();
//            settingsView = settingsView == null ? FluentViewLoader.fxmlView(LandingScreenSettingsController.class).load().getView() : settingsView;

            Task<Void> allViewsTask = new Task<>() {
                @Override
                protected Void call() {
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
                    return null;
                }
            };
            Thread allViewsThread = new Thread(allViewsTask);
            allViewsThread.start();

            System.out.println(TAG + "Time taken to load Views -> " + (System.currentTimeMillis() - start)+"ms");

        /* else if (viewModel.getScreenToShow().equalsIgnoreCase("triviaScreen")) {
            selectButton(triviaView, triviaButton);
        } else if (viewModel.getScreenToShow().equalsIgnoreCase("performanceScreen")) {
            selectButton(performanceView, performanceButton);
        } else if (viewModel.getScreenToShow().equalsIgnoreCase("updatesScreen")) {
            selectButton(updatesView, updatesButton);
        }*/

//            homeContentPane.getChildren().add(homeView);


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

            appsButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue){
                    selectButton(appsView, appsButton);
                }
            });
            appsButton.setOnMouseEntered(event -> {
                if (appsButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    appsButton.setStyle(HOVER_BUTTON_STYLE);
            });
            appsButton.setOnMouseExited(event -> {
                if (appsButton.getStyle().equals(PRESSED_BUTTON_STYLE))
                    event.consume();
                else
                    appsButton.setStyle(null);
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

        /*logoutButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.PRE_AUTHENTICATION_SCREEN);
        });*/

            topActivateButton.setOnAction(event -> {
                ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
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
            activateCloseIcon.setOnMouseClicked(event -> {
                PreferencesManager.putBoolean(PREF_KEY_HOME_SCREEN_ACTIVATE_PROMPT_REMOVED+viewModel.getUserId(), activateVBox.getChildren().remove(activatePanel));
            });
        } catch (Exception e) {
            e.printStackTrace();
//            MainApplication.log(e);
        }
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

    public record InitialData(Screens screenToShow) {
        public Screens getScreenToShow() {
            return screenToShow;
        }
    }
}
