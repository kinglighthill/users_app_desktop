package com.scholarly.utme.controller.trivia_screen;

import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.viewmodels.trivia_screen.TriviaChallengeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/trivia_screen/TriviaChallengeQuizScreen.fxml")
public class TriviaChallengeQuizScreenController implements FxmlView<TriviaChallengeScreenVM>, Initializable {

    @FXML
    private Pane dialogDimmer;

    @FXML
    private VBox challengeEndedPane;

    @FXML
    private ImageView clockImage, challengeImage, speakerIcon, buttonAIcon, buttonBIcon, buttonCIcon, buttonDIcon;

    @FXML
    private ImageView player1, player2, player3, player4, challengeEndedCloseIcon;

    @FXML
    private Label questionLabel, challengeEndedText, yourScoreText, scoreText;

    @FXML
    private Button exitButton, buttonA, buttonB, buttonC, buttonD, homePageButton, rankingsButton, resultButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        challengeEndedCloseIcon.setOnMouseClicked(event -> {
            Animations.hideDialog(challengeEndedPane, dialogDimmer);
        });

        exitButton.setOnAction(event -> {
            Animations.fadeIn(dialogDimmer, 200);
            Dialog<ButtonType> dialog = Alerts.dialog(
                    this.getClass(),
                    "Confirm Exit",
                    null,
                    "Are you sure you want to exit?"
            );
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    Animations.showDialog(challengeEndedPane, dialogDimmer);
                }else {
                    Animations.fadeOut(dialogDimmer, 100);
                }
                return buttonType;
            });

            dialog.show();

        });

        resultButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.TRIVIA_QUIZ_RESULT_SCREEN);
        });

    }

    private void initializeViews() {
        clockImage.setImage(new Image(getClass().getResource("/drawable/practice_screen_images/time_image.jpg").toString()));
        speakerIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/question_volume_icon.png").toString()));
        challengeImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/challenge_image.png").toString()));
        challengeEndedCloseIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/new_challenge_close_icon.png").toString()));

        buttonAIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/option_a_btn.png").toString()));
        buttonBIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/option_b_btn.png").toString()));
        buttonCIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/option_c_btn.png").toString()));
        buttonDIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/option_d_btn.png").toString()));

        player1.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/player1_image.png").toString()));
        Circle clip1 = new Circle(18, 18, 18);
        player1.setClip(clip1);
        player2.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/player2_image.png").toString()));
        Circle clip2 = new Circle(18, 18, 18);
        player2.setClip(clip2);
        player3.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/player3_image.png").toString()));
        Circle clip3 = new Circle(18, 18, 18);
        player3.setClip(clip3);
        player4.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/player4_image.png").toString()));
        Circle clip4 = new Circle(18, 18, 18);
        player4.setClip(clip4);

        exitButton.setBackground(Background.EMPTY);
        homePageButton.setBackground(Background.EMPTY);
        rankingsButton.setBackground(Background.EMPTY);
        resultButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        challengeEndedText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        questionLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 18));
        buttonA.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        buttonB.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        buttonC.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        buttonD.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

        yourScoreText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        scoreText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));

    }
}
