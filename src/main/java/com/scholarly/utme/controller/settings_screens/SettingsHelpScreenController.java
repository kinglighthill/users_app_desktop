package com.scholarly.utme.controller.settings_screens;

import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.settings_screens.SettingsHelpScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/settings_screens/SettingsHelpScreen.fxml")
public class SettingsHelpScreenController implements FxmlView<SettingsHelpScreenVM>, Initializable {

    @FXML
    private VBox contactVBox, contactDetails;

    @FXML
    private Button backButton;

    @FXML
    private ToggleButton faqDropdown, contactDropdown;

    @FXML
    private ImageView faqImage, contactImage, phoneImage, whatsAppImage, mailImage, websiteImage;


    private ImageView faqOpenDropdownIcon, contactOpenDropdownIcon;

    private ImageView faqCloseDropdownIcon, contactCloseDropdownIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        contactVBox.getChildren().remove(contactDetails);

        contactDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                contactVBox.getChildren().add(contactDetails);
                contactDropdown.setGraphic(contactCloseDropdownIcon);
            } else {
                contactVBox.getChildren().remove(contactDetails);
                contactDropdown.setGraphic(contactOpenDropdownIcon);
            }
        }));

        backButton.setOnAction(event -> {
            ViewSwitcher.passData("settingsButton");
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));

        faqImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/faq_icon.png").toString()));
        contactImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/contact_icon.png").toString()));
        phoneImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/phone_icon.png").toString()));
        whatsAppImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/whatsapp_icon.png").toString()));
        mailImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/mail_icon.png").toString()));
        websiteImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/contact_icon.png").toString()));

        faqOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        contactOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));

        faqCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        contactCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));

        faqDropdown.setGraphic(faqOpenDropdownIcon);
        contactDropdown.setGraphic(contactOpenDropdownIcon);

        backButton.setBackground(Background.EMPTY);
        faqDropdown.setBackground(Background.EMPTY);
        contactDropdown.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }
}
