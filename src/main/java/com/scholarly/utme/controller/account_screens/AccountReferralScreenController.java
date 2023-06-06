package com.scholarly.utme.controller.account_screens;

import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.account_screens.AccountReferralScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/account_screens/AccountReferralScreen.fxml")
public class AccountReferralScreenController implements FxmlView<AccountReferralScreenVM>, Initializable {

    @FXML
    private Button backButton, copyButton;

    @FXML
    private ImageView accountImage, centerImage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();


        backButton.setOnAction(event -> {
            ViewSwitcher.passData("accountScreen");
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        centerImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/referral_screen_center_image.png").toString()));
        accountImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/account_icon.png").toString()));
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));
        copyButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/account_screen_images/copy_icon.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }
}
