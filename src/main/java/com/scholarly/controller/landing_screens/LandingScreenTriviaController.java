package com.scholarly.controller.landing_screens;

import com.scholarly.ui.utils.FontUtil;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import com.scholarly.viewmodels.landing_screens.LandingScreenTriviaVM;
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
import javafx.scene.shape.Circle;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen_trivia.fxml")
public class LandingScreenTriviaController implements FxmlView<LandingScreenTriviaVM>, Initializable {

    @FXML
    private ImageView centerImage, prevChallengeIcon1, prevChallengeIcon2, prevChallengeIcon3, playerImage1, playerImage2, playerImage3;

    @FXML
    private HBox centerPane;

    @FXML
    private Panel prevChallenge1, prevChallenge2, prevChallenge3;

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

        startButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.TRIVIA_CHALLENGE_SCREEN);
        });

    }

    private void initializeViews() {
        centerImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/young_african_students.jpg").toString()));
        prevChallengeIcon1.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/challenge_image.png").toString()));
        prevChallengeIcon2.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/challenge_image.png").toString()));
        prevChallengeIcon3.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/challenge_image.png").toString()));

        playerImage1.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/prev_challenge_player_image.png").toString()));
        playerImage2.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/prev_challenge_player_image.png").toString()));
        playerImage3.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/prev_challenge_player_image.png").toString()));
        Circle clip1 = new Circle(18, 18, 18);
        playerImage1.setClip(clip1);
        Circle clip2 = new Circle(18, 18, 18);
        playerImage2.setClip(clip2);
        Circle clip3 = new Circle(18, 18, 18);
        playerImage3.setClip(clip3);

    }

    private void initializeFonts() {
        challengeHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 42));
        challengeDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        usernameError.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM_ITALIC, 12));
        startButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        prevHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
    }
}
