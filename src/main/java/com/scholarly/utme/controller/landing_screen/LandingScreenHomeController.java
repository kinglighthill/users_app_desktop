package com.scholarly.utme.controller.landing_screen;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screen.LandingScreenHomeVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen/landing_screen_home.fxml")
public class LandingScreenHomeController implements FxmlView<LandingScreenHomeVM>, Initializable {

    @InjectViewModel
    private LandingScreenHomeVM viewModel;

    @FXML
    private BorderPane rootPane;

    @FXML
    private ScrollPane previousSessionScrollPane, performanceScrollPane;

    @FXML
    private GridPane previousSessionGridPane;

    @FXML
    private HBox scrollHBox;

    @FXML
    ListView<Course> recentlyViewedListView;

    @FXML
    private ImageView handImage, notificationIcon, profileImage, biologyIcon, englishIcon, physicsIcon, chemistryIcon, mathematicsIcon, geographyIcon;

    @FXML
    private ImageView cbtPracticeIcon, videosIcon, novelsIcon, studyNotesIcon, cbtCentresIcon, audioIcon, firstSessionVideoImage, firstSessionPlayIcon, thirdSessionVideoImage, thirdSessionPlayIcon;

    @FXML
    private ImageView firstSessionOneStar, firstSessionTwoStar, firstSessionThreeStar, firstSessionFourStar, firstSessionFiveStar, thirdSessionOneStar, thirdSessionTwoStar, thirdSessionThreeStar, thirdSessionFourStar, thirdSessionFiveStar;

    @FXML
    private ImageView secondSessionBookImage, secondSessionAuthorIcon, secondSessionChaptersIcon, fourthSessionBookImage, fourthSessionAuthorIcon, fourthSessionChaptersIcon;

    @FXML
    private Panel cbtPracticePanel, pastQuestionsPanel, cbtGamePanel, videosPanel, audioPanel, studyNotesPanel, cbtCentresPanel, syllabusPanel;

    @FXML
    private Panel firstSession, secondSession, thirdSession, fourthSession;

    @FXML
    private Label helloText, startLearningText, topSubjectsText, biologyText, englishText, physicsText, chemistryText, mathematicsText, geographyText, activitiesText, continueSessionsText;

    @FXML
    private Label cbtPracticeText, videosText, novelsText, pastQuestionsLabel, audioText, studyNotesText, cbtCentresText, syllabusLabel, firstSessionVideoTitle, firstSessionTimeText, firstSessionRatingNumber, firstSessionDescriptionText;

    @FXML
    private Label secondSessionBookTitle, secondSessionAuthorName, secondSessionChaptersText, thirdSessionVideoTitle, thirdSessionTimeText, thirdSessionRatingNumber, thirdSessionDescriptionText, fourthSessionBookTitle, fourthSessionAuthorName, fourthSessionChaptersText;

    @FXML
    private Label performanceText, previousScoresText, performanceChartText, firstPerformanceCBTText, firstPerformanceCBTDate, firstPerformancePercentText, secondPerformanceCBTText, secondPerformanceCBTDate, secondPerformancePercentText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();

        initializeFonts();

        String defaultImageURL = getClass().getResource("/drawable/app_logo.png").toString();
        ObservableList<Course> items = FXCollections.observableArrayList(
                new Course("Mathematics", defaultImageURL),
                new Course("Physics", defaultImageURL),
                new Course("Economics", defaultImageURL),
                new Course("English Language", defaultImageURL)
        );

//
//        pastQuestionsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//
//        syllabusLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        cbtPracticePanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("practicePanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });


       /* pastQuestionsPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("pastQuestionsPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });*/

        /*cbtGamePanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("cbtGamePanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });*/

        videosPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("videosPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        audioPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("audiosPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        cbtCentresPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("learningCenterPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        studyNotesPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("studyNotesPanel");
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });

        /*syllabusPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("syllabusPanel");
            ViewSwitcher.showScreen(View.SELECT_SYLLABUS_SCREEN);
        });*/
    }

    private void initializeViews() {
        rootPane.setPadding(new Insets(0,15, 0, 0));
        notificationIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/bell_without_notification.png").toString()));
        handImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/hand_image2.png").toString()));

        profileImage.setImage(new Image(getClass().getResource("/drawable/profileImage.jpg").toString()));
        final Circle clip = new Circle(20, 30, 20);
        clip.setStyle("-fx-border-color: #000000; -fx-border-width: 5");
        profileImage.setClip(clip);

        biologyIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/biology_icon.png").toString()));
        englishIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/english_icon.png").toString()));
        physicsIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/physics_icon.png").toString()));
        chemistryIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/chemistry_icon.png").toString()));
        mathematicsIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/mathematics_icon.png").toString()));
        geographyIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/geography_icon.png").toString()));

        cbtPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_practice_icon.png").toString()));
        videosIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/videos_icon.png").toString()));
        novelsIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/novels_icon.png").toString()));
        studyNotesIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/novels_icon.png").toString()));
        cbtCentresIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_centres_icon.png").toString()));
        audioIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/audio_icon.png").toString()));

        firstSessionVideoImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_video_background.jpg").toString()));
        firstSessionPlayIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_play_icon.png").toString()));
        firstSessionOneStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        firstSessionTwoStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        firstSessionThreeStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        firstSessionFourStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        firstSessionFiveStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));

        secondSessionBookImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_book_image.jpg").toString()));
        secondSessionAuthorIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_author_icon.png").toString()));
        secondSessionChaptersIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_chapters_icon.png").toString()));

        thirdSessionVideoImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_video_background.jpg").toString()));
        thirdSessionPlayIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_play_icon.png").toString()));
        thirdSessionOneStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        thirdSessionTwoStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        thirdSessionThreeStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        thirdSessionFourStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
        thirdSessionFiveStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));

        fourthSessionBookImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_book_image.jpg").toString()));
        fourthSessionAuthorIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_author_icon.png").toString()));
        fourthSessionChaptersIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_chapters_icon.png").toString()));

        previousSessionScrollPane.widthProperty().addListener(((observable, oldValue, newValue) -> {
            firstSession.setPrefWidth((Double) newValue/2);
            secondSession.setPrefWidth((Double) newValue/2);
            thirdSession.setPrefWidth((Double) newValue/2);
            fourthSession.setPrefWidth((Double) newValue/2);
        }));

        previousSessionScrollPane.setBackground(Background.EMPTY);
        performanceScrollPane.setBackground(Background.EMPTY);
        scrollHBox.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {
        helloText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.TWENTY.size));
        startLearningText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        topSubjectsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));

        biologyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        englishText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        physicsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        chemistryText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        mathematicsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        geographyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

        activitiesText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));

        cbtPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        videosText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        novelsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        studyNotesText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        audioText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        cbtCentresText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        continueSessionsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        firstSessionVideoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        firstSessionTimeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        firstSessionRatingNumber.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        firstSessionDescriptionText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

        secondSessionBookTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        secondSessionAuthorName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        secondSessionChaptersText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

        thirdSessionVideoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        thirdSessionTimeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        thirdSessionRatingNumber.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        thirdSessionDescriptionText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

        fourthSessionBookTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        fourthSessionAuthorName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        fourthSessionChaptersText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

        performanceText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        previousScoresText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        performanceChartText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));

        firstPerformanceCBTText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
        firstPerformanceCBTDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        firstPerformancePercentText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));

        secondPerformanceCBTText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
        secondPerformanceCBTDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        secondPerformancePercentText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));

    }
}
