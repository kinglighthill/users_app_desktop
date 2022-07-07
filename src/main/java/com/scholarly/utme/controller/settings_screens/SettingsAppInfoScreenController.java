package com.scholarly.utme.controller.settings_screens;

import com.scholarly.utme.viewmodels.settings_screens.SettingsAppInfoScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/settings_screens/SettingsAppInfoScreen.fxml")
public class SettingsAppInfoScreenController implements FxmlView<SettingsAppInfoScreenVM>, Initializable {

    @FXML
    private VBox privacyVBox, privacyDetails;

    @FXML
    private Button backButton;

    @FXML
    private ToggleButton privacyDropdown, termsDropdown, thirdPartyDropdown;

    @FXML
    private ImageView privacyImage, termsImage, thirdPartyImage;


    private ImageView privacyOpenDropdownIcon, termsOpenDropdownIcon, thirdPartyOpenDropdownIcon;
    private ImageView privacyCloseDropdownIcon, termsCloseDropdownIcon, thirdPartyCloseDropdownIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        privacyVBox.getChildren().remove(privacyDetails);

        privacyDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                privacyVBox.getChildren().add(privacyDetails);
                privacyDropdown.setGraphic(privacyCloseDropdownIcon);
            } else {
                privacyVBox.getChildren().remove(privacyDetails);
                privacyDropdown.setGraphic(privacyOpenDropdownIcon);
            }
        }));


    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));

        privacyImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/info_icon.png").toString()));
        termsImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/info_icon.png").toString()));
        thirdPartyImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/info_icon.png").toString()));

        privacyOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        termsOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        thirdPartyOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));

        privacyCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        termsCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        thirdPartyCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));

        privacyDropdown.setGraphic(privacyOpenDropdownIcon);
        termsDropdown.setGraphic(termsOpenDropdownIcon);
        thirdPartyDropdown.setGraphic(thirdPartyOpenDropdownIcon);


        backButton.setBackground(Background.EMPTY);
        privacyDropdown.setBackground(Background.EMPTY);
        termsDropdown.setBackground(Background.EMPTY);
        thirdPartyDropdown.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }
}
