package com.scholarly.utme.controller.landing_screen;

import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.viewmodels.landing_screen.LandingScreenTriviaVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.BatchUpdateException;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen/landing_screen_trivia.fxml")
public class LandingScreenTriviaController implements FxmlView<LandingScreenTriviaVM>, Initializable {

    @FXML
    private ImageView centerImage;

    @FXML
    private HBox centerPane;

    @FXML
    private Label challengeHeader, challengeDescription, usernameError, prevHeader;

    @FXML
    private TextField usernameField;

    @FXML
    private Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();

        initializeFonts();

        /*challengeHeader.widthProperty().addListener((observable, oldValue, newValue) -> {
            usernameField.setMaxWidth(newValue.doubleValue()/2);
        });*/

    }

    private void initializeViews() {
        centerImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/young_african_students.jpg").toString()));

    }

    private void initializeFonts() {
        challengeHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 42));
        challengeDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        usernameError.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM_ITALIC, 12));
        startButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        prevHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
    }
}
