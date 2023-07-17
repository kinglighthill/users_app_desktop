package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenSettingsVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.ToggleSwitch;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

import static com.scholarly.utme.util.Constants.PREF_KEY_SOUND;
import static com.scholarly.utme.util.Constants.PREF_KEY_VIBRATION;

@FxmlPath("/layouts/landing_screens/landing_screen_settings.fxml")
public class LandingScreenSettingsController implements FxmlView<LandingScreenSettingsVM>, Initializable {
    private static final String TAG = "LandingScreenSettingsController: ";

    @InjectViewModel
    private LandingScreenSettingsVM viewModel;

    @FXML
    private Panel aboutPanel, helpPanel, appInfoPanel;
    @FXML
    private ToggleSwitch vibrationSwitch, soundSwitch;
    @FXML
    private ImageView notificationIcon, vibrationIcon, soundIcon, shareIcon, ratingIcon, aboutIcon, helpIcon, appInfoIcon;
    @FXML
    private ImageView shareExpandIcon, ratingExpandIcon, aboutExpandIcon, helpExpandIcon, appInfoExpandIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        vibrationSwitch.selectedProperty().bindBidirectional(viewModel.vibrationProperty());
        soundSwitch.selectedProperty().bindBidirectional(viewModel.soundProperty());


        vibrationSwitch.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.getPreferences().putBoolean(PREF_KEY_VIBRATION+viewModel.getUser().getId(), newValue);
        }));

        soundSwitch.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.getPreferences().putBoolean(PREF_KEY_SOUND+viewModel.getUser().getId(), newValue);
        }));

        aboutPanel.setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.SETTINGS_ABOUT_US_SCREEN);
        });

        helpPanel.setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.SETTINGS_HELP_SCREEN);
        });

        appInfoPanel.setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.SETTINGS_APP_INFO_SCREEN);
        });
    }

    private void initializeViews() {
//        notificationIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/notification_bell.png").toString()));
        vibrationIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/vibration_icon.png").toString()));
        soundIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/sound_icon.png").toString()));
//        shareIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/share_icon.png").toString()));
//        ratingIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/star_icon.png").toString()));
        aboutIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/about_icon.png").toString()));
        helpIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/settings_icon.png").toString()));
        appInfoIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/app_icon.png").toString()));

//        shareExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        ratingExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        aboutExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        helpExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        appInfoExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
    }

    private void initializeFonts() {

    }
}
