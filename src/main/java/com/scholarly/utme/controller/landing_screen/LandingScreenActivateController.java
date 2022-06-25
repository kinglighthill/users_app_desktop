package com.scholarly.utme.controller.landing_screen;

import com.scholarly.utme.viewmodels.landing_screen.LandingScreenActivateVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen/landing_screen_activate.fxml")
public class LandingScreenActivateController implements FxmlView<LandingScreenActivateVM>, Initializable {

    @FXML
    private ImageView padlockIcon, atmCardImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();
    }

    private void initializeViews() {
        padlockIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/padlock_icon.png").toString()));
        atmCardImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/credit_card_image.png").toString()));
    }

    private void initializeFonts() {

    }
}
