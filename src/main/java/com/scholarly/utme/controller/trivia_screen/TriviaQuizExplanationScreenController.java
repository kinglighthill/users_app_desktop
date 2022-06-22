package com.scholarly.utme.controller.trivia_screen;

import com.scholarly.utme.viewmodels.trivia_screen.TriviaQuizExplanationScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/trivia_screen/TriviaQuizExplanationScreen.fxml")
public class TriviaQuizExplanationScreenController implements FxmlView<TriviaQuizExplanationScreenVM>, Initializable {

    @FXML
    private Button backButton;

    @FXML
    private ToggleButton textExplanation, videoExplanation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

    }

    private void initializeViews() {
        backButton.setBackground(Background.EMPTY);
        textExplanation.setBackground(Background.EMPTY);
        videoExplanation.setBackground(Background.EMPTY);


        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));
    }

    private void initializeFonts() {

    }
}
