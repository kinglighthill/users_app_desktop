package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenSettingsVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen_settings.fxml")
public class LandingScreenSettingsController implements FxmlView<LandingScreenSettingsVM>, Initializable {

    @FXML
    private ImageView notificationIcon, vibrationIcon, soundIcon, shareIcon, ratingIcon, aboutIcon, helpIcon, appInfoIcon;

    @FXML
    private ImageView expandIcon, expandIcon2, expandIconAbout, expandIcon4, expandIcon5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        expandIconAbout.setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.SETTINGS_ABOUT_US_SCREEN);
        });
    }

    private void initializeViews() {
        notificationIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/notification_bell.png").toString()));
        vibrationIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/vibration_icon.png").toString()));
        soundIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/sound_icon.png").toString()));
        shareIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/share_icon.png").toString()));
        ratingIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/star_icon.png").toString()));
        aboutIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/about_icon.png").toString()));
        helpIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/settings_icon.png").toString()));
        appInfoIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/app_icon.png").toString()));

        expandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        expandIcon2.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        expandIconAbout.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        expandIcon4.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        expandIcon5.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
    }

    private void initializeFonts() {

    }
}
