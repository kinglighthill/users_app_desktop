package com.scholarly.utme.controller.settings_screens;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.Screens;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/settings_screens/SettingsAboutUsScreen.fxml")
public class SettingsAboutUsScreenController implements FxmlView<SettingsAboutUsScreenVM>, Initializable {

    @FXML
    private Panel appDetailsPanel, ourTeamPanel, missionPanel, visionPanel;
    @FXML
    private VBox appDetailsVBox, ourTeamVBox, missionVBox, visionVBox;
    @FXML
    private Button backButton, contactButton;
    @FXML
    private ImageView cbtCentreImage, scholarlyLogo;
    @FXML
    private Label scholarlyLabel, versionLabel, appDetailsLabel, ourTeamLabel, missionLabel, visionLabel;
    @FXML
    private ToggleButton appDetailsDropdown, ourTeamDropdown, missionDropdown, visionDropdown;

    private ImageView appDetailsOpenDropdownIcon, teamOpenDropdownIcon, missionOpenDropdownIcon, visionOpenDropdownIcon;

    private ImageView appDetailsCloseDropdownIcon, teamCloseDropdownIcon, missionCloseDropdownIcon, visionCloseDropdownIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(appDetailsDropdown, ourTeamDropdown, missionDropdown, visionDropdown);

        appDetailsVBox.getChildren().remove(appDetailsLabel);
        ourTeamVBox.getChildren().remove(ourTeamLabel);
        missionVBox.getChildren().remove(missionLabel);
        visionVBox.getChildren().remove(visionLabel);

        appDetailsPanel.setOnMouseClicked(event -> appDetailsDropdown.setSelected(!appDetailsDropdown.isSelected()));
        ourTeamPanel.setOnMouseClicked(event -> ourTeamDropdown.setSelected(!ourTeamDropdown.isSelected()));
        missionPanel.setOnMouseClicked(event -> missionDropdown.setSelected(!missionDropdown.isSelected()));
        visionPanel.setOnMouseClicked(event -> visionDropdown.setSelected(!visionDropdown.isSelected()));

        appDetailsDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                appDetailsVBox.getChildren().add(appDetailsLabel);
                appDetailsDropdown.setGraphic(appDetailsCloseDropdownIcon);
            } else {
                appDetailsVBox.getChildren().remove(appDetailsLabel);
                appDetailsDropdown.setGraphic(appDetailsOpenDropdownIcon);
            }
        }));

        ourTeamDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                ourTeamVBox.getChildren().add(ourTeamLabel);
                ourTeamDropdown.setGraphic(teamCloseDropdownIcon);
            } else {
                ourTeamVBox.getChildren().remove(ourTeamLabel);
                ourTeamDropdown.setGraphic(teamOpenDropdownIcon);
            }
        }));

        missionDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                missionVBox.getChildren().add(missionLabel);
                missionDropdown.setGraphic(missionCloseDropdownIcon);
            } else {
                missionVBox.getChildren().remove(missionLabel);
                missionDropdown.setGraphic(missionOpenDropdownIcon);
            }
        }));

        visionDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                visionVBox.getChildren().add(visionLabel);
                visionDropdown.setGraphic(visionCloseDropdownIcon);
            } else {
                visionVBox.getChildren().remove(visionLabel);
                visionDropdown.setGraphic(visionOpenDropdownIcon);
            }
        }));

        contactButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.SETTINGS_HELP_SCREEN);
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.SETTINGS_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));
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
        ourTeamDropdown.setGraphic(teamOpenDropdownIcon);
        missionDropdown.setGraphic(missionOpenDropdownIcon);
        visionDropdown.setGraphic(visionOpenDropdownIcon);

        appDetailsLabel.setText("Scholarly provides E-learning solutions and services to students, government, organizations and corporate bodies via various platforms such as mobile, web and desktop." + System.lineSeparator() + "These devices have been loaded with highly scalable software in which are stacked quality and feature rich content with near zero inaccuracy");
        ourTeamLabel.setText("We pride ourselves as one of the foremost educational content provider in Nigeria. Our workforce is an epitome of team balance." + System.lineSeparator() + "Our team comprises a mix in the right proportion of individuals in: Content, Engineering, Product, Sales and Marketing, Growth and Quality Assurance and Control. We have a Scholarly Team!!!");
        missionLabel.setText("We have a simplistic mission to make learning simple and exciting because we believe that if one learns simply and enjoyably, they will hardly forget that which they learnt. We want to help students succeed in their academics and organizations run a successful business.");
        visionLabel.setText("Scholarly aims to change the narrative and perception of learning. We want people to see learning as excitement. We aim to achieve this with richly engineered content in various forms.");

        appDetailsDropdown.setBackground(Background.EMPTY);
        ourTeamDropdown.setBackground(Background.EMPTY);
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
