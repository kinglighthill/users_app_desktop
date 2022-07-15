package com.scholarly.utme.controller.account_screens;

import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.account_screens.AccountProfileScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/account_screens/AccountProfileScreen.fxml")
public class AccountProfileScreenController implements FxmlView<AccountProfileScreenVM>, Initializable {

    @FXML
    private ImageView profileImage, cameraImage, keyImage, infoImage;

    @FXML
    private Button backButton, saveButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private TextField profileNameTextField, phoneTextField;

    @FXML
    private Label changeProfileName, changePhoneNum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();


        changeProfileName.setOnMouseClicked(event -> {
            profileNameTextField.setEditable(true);
        });

        changePhoneNum.setOnMouseClicked(event -> {
            phoneTextField.setEditable(true);
        });

        saveButton.setOnAction(event -> {
            profileNameTextField.setEditable(false);
            phoneTextField.setEditable(false);
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData("accountButton");
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        Circle clip = new Circle(60, 60, 60);
        profileImage.setClip(clip);
        profileImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/profile_image.png").toString()));
        cameraImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/camera_icon.png").toString()));
        keyImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/key_icon.png").toString()));
        infoImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/info_icon.png").toString()));
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }
}
