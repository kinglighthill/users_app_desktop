package com.scholarly.utme.controller.trivia_screen;

import com.scholarly.utme.data.model.listItems.TriviaChallengeItem;
import com.scholarly.utme.data.model.listItems.TriviaParticipantItem;
import com.scholarly.utme.ui.cellFactories.TriviaChallengeListCellFactory;
import com.scholarly.utme.ui.cellFactories.TriviaParticipantListCellFactory;
import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.trivia_screen.TriviaChallengeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/trivia_screen/TriviaChallengeScreen.fxml")
public class TriviaChallengeScreenController implements FxmlView<TriviaChallengeScreenVM>, Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField searchChallenge;

    @FXML
    private VBox newChallengeFirstPane, newChallengeSecondPane, challengeCreatedPane;

    @FXML
    private Pane dialogDimmer;

    @FXML
    private Button backButton, exitButton, newChallengeButton, joinChallengeBtn, nextButton, createChallengeBtn;

    @FXML
    private ImageView playerImage, challengeImage, timeIcon, firstStepCloseIcon, secondStepCloseIcon, challengeCreatedCloseIcon, contactIcon, shareIcon, contactImage, contact2Image, contactRemoveIcon, contact2RemoveIcon, challengeCreatedImage;

    @FXML
    private Label playerName, playerPoints, challengeName, timeLabel, challengeDescription, participantsLabel, challengeCreatedText, successfulText;

    @FXML
    private ListView<TriviaChallengeItem> challengeList;

    @FXML
    private ListView<TriviaParticipantItem> participantsList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        TriviaChallengeItem challenge1 = new TriviaChallengeItem("", "", "", "", "", "", "", 1);
        TriviaChallengeItem challenge2 = new TriviaChallengeItem("", "", "", "", "", "", "", 0);
        TriviaChallengeItem challenge3 = new TriviaChallengeItem("", "", "", "", "", "", "", 2);
        TriviaChallengeItem challenge4 = new TriviaChallengeItem("", "", "", "", "", "", "", 1);

        ObservableList<TriviaChallengeItem> items = FXCollections.observableArrayList(challenge1, challenge2, challenge3, challenge4);
        challengeList.setCellFactory(new TriviaChallengeListCellFactory());
        challengeList.setItems(items);


        TriviaParticipantItem participant1 = new TriviaParticipantItem("", "", 300);
        TriviaParticipantItem participant2 = new TriviaParticipantItem("", "", 300);
        TriviaParticipantItem participant3 = new TriviaParticipantItem("", "", 300);
        TriviaParticipantItem participant4 = new TriviaParticipantItem("", "", 300);
        TriviaParticipantItem participant5 = new TriviaParticipantItem("", "", 300);

        ObservableList<TriviaParticipantItem> participants = FXCollections.observableArrayList(participant1, participant2, participant3, participant4, participant5);
        participantsList.setCellFactory(new TriviaParticipantListCellFactory());
        participantsList.setItems(participants);


        joinChallengeBtn.setOnAction(event -> {
            ViewSwitcher.showScreen(View.TRIVIA_CHALLENGE_QUIZ_SCREEN);
        });

        newChallengeButton.setOnAction(event -> {
            Animations.showDialog(newChallengeFirstPane, dialogDimmer);
        });

        firstStepCloseIcon.setOnMouseClicked(event -> {
            Animations.hideDialog(newChallengeFirstPane, dialogDimmer);
        });

        nextButton.setOnAction(event -> {
//            newChallengeFirstPane.setVisible(false);
//            newChallengeSecondPane.setVisible(true);
            Animations.fadeOut(newChallengeFirstPane, 100);
            Animations.fadeIn(newChallengeSecondPane, 200);
        });

        secondStepCloseIcon.setOnMouseClicked(event -> {
            Animations.hideDialog(newChallengeSecondPane, dialogDimmer);
        });

        createChallengeBtn.setOnAction(event -> {
            Animations.fadeOut(newChallengeSecondPane, 100);
            Animations.fadeIn(challengeCreatedPane, 200);
        });

        challengeCreatedCloseIcon.setOnMouseClicked(event -> {
            Animations.hideDialog(challengeCreatedPane, dialogDimmer);
        });

    }

    private void initializeViews() {
        timeIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/time_image.png").toString()));
        challengeImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/mathematics_challenge_image.png").toString()));
        firstStepCloseIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/new_challenge_close_icon.png").toString()));
        secondStepCloseIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/new_challenge_close_icon.png").toString()));
        challengeCreatedCloseIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/new_challenge_close_icon.png").toString()));
        contactIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/contact_icon.png").toString()));
        shareIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/share_link_icon.png").toString()));
        contactRemoveIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/remove_contact_icon.png").toString()));
        contact2RemoveIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/remove_contact_icon.png").toString()));
        challengeCreatedImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/challenge_created_image.png").toString()));

        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_icon.png").toString()));
        backButton.setGraphic(backIcon);
        Circle clip = new Circle(18, 18, 18);
        backButton.setClip(clip);

        playerImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/prev_challenge_player_image.png").toString()));
        Circle profileClip = new Circle(20, 20, 20);
        playerImage.setClip(profileClip);

        Circle contactClip = new Circle(15, 15, 15);
        contactImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/player_icon.png").toString()));
        contactImage.setClip(contactClip);

        Circle contact2Clip = new Circle(15, 15, 15);
        contact2Image.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/player_icon.png").toString()));
        contact2Image.setClip(contact2Clip);

        challengeList.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
        participantsList.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {
        challengeName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
        participantsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
        playerName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        playerPoints.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        exitButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        timeLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        newChallengeButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));
        challengeDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        joinChallengeBtn.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        nextButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        createChallengeBtn.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));
        challengeCreatedText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        successfulText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

    }
}
