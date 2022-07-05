package com.scholarly.utme.controller.landing_screens;

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
    private ImageView notificationIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

    }

    private void initializeViews() {
        notificationIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/notification_bell.png").toString()));
    }

    private void initializeFonts() {

    }
}
