package com.scholarly.utme.controller.settings_screens;

import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.settings_screens.SettingsAboutUsScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/settings_screens/SettingsAboutUsScreen.fxml")
public class SettingsAboutUsScreenController implements FxmlView<SettingsAboutUsScreenVM>, Initializable {

    @FXML
    private VBox appDetailsVBox;

    @FXML
    private Button backButton, contactButton;

    @FXML
    private ImageView cbtCentreImage, scholarlyLogo;

    @FXML
    private Label scholarlyLabel, versionLabel, appDetails;

    @FXML
    private ToggleButton appDetailsDropdown, teamDropdown, missionDropdown, visionDropdown;

    private ImageView appDetailsOpenDropdownIcon, teamOpenDropdownIcon, missionOpenDropdownIcon, visionOpenDropdownIcon;

    private ImageView appDetailsCloseDropdownIcon, teamCloseDropdownIcon, missionCloseDropdownIcon, visionCloseDropdownIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        appDetailsVBox.getChildren().remove(appDetails);

        appDetailsDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                appDetailsVBox.getChildren().add(appDetails);
                appDetailsDropdown.setGraphic(appDetailsOpenDropdownIcon);
            } else {
                appDetailsVBox.getChildren().remove(appDetails);
                appDetailsDropdown.setGraphic(appDetailsCloseDropdownIcon);
            }
        }));

        backButton.setOnAction(event -> {
            ViewSwitcher.passData("settingsButton");
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));
        cbtCentreImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/cbt_center_image.png").toString()));
        scholarlyLogo.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/scholarly_logo.png").toString()));

        appDetailsOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        teamOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        missionOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        visionOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));

        appDetailsCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        teamCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        missionCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        visionCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));

        appDetailsDropdown.setGraphic(appDetailsOpenDropdownIcon);
        teamDropdown.setGraphic(teamOpenDropdownIcon);
        missionDropdown.setGraphic(missionOpenDropdownIcon);
        visionDropdown.setGraphic(visionOpenDropdownIcon);

        appDetailsDropdown.setBackground(Background.EMPTY);
        teamDropdown.setBackground(Background.EMPTY);
        missionDropdown.setBackground(Background.EMPTY);
        visionDropdown.setBackground(Background.EMPTY);
        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        scholarlyLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 46));
        contactButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        versionLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
    }
}
