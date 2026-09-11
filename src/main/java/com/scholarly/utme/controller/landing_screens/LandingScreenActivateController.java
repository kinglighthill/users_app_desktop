package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenActivateVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen_activate.fxml")
public class LandingScreenActivateController implements FxmlView<LandingScreenActivateVM>, Initializable {

    @FXML
    private Pane dialogDimmer;

    @FXML
    private ImageView padlockIcon, atmCardImage, paymentSuccessfulCloseIcon, paymentSuccessfulImage;

    @FXML
    private VBox paymentSuccessfulPane;

    @FXML
    private Button activateButton, buyPinButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        activateButton.setOnAction(event -> {
            Animations.showDialog(paymentSuccessfulPane, dialogDimmer);
        });

        buyPinButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.ACTIVATE_PAYMENT_SCREEN);
        });

        paymentSuccessfulCloseIcon.setOnMouseClicked(mouseEvent -> {
            Animations.hideDialog(paymentSuccessfulPane, dialogDimmer);
        });
    }

    private void initializeViews() {
        padlockIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/padlock_icon.png").toString()));
        atmCardImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/credit_card_image.png").toString()));
        paymentSuccessfulCloseIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/new_challenge_close_icon.png").toString()));
        paymentSuccessfulImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/successful_image.png").toString()));

    }

    private void initializeFonts() {

    }
}
