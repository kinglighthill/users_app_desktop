package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.listItems.NewsItem;
import com.scholarly.utme.ui.cellFactories.NewsListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenHomeVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

import static com.scholarly.utme.util.Constants.*;

@FxmlPath("/layouts/landing_screens/landing_screen_home.fxml")
public class LandingScreenHomeController implements FxmlView<LandingScreenHomeVM>, Initializable {

    @InjectViewModel
    private LandingScreenHomeVM viewModel;

    @FXML
    private BorderPane rootPane;

    @FXML
    private ScrollPane mainScrollPane, previousSessionScrollPane, performanceScrollPane;

    @FXML
    private StackPane actionsPane;

    @FXML
    private GridPane previousSessionGridPane;

    @FXML
    private HBox scrollHBox;

    @FXML
    private Button viewDesktopAppButton;

    @FXML
    ListView<NewsItem> newsList;

    @FXML
    private ImageView handImage, notificationIcon, profileImage, biologyIcon, englishIcon, physicsIcon, chemistryIcon, mathematicsIcon, geographyIcon, boyWithLaptop;

    @FXML
    private ImageView cbtPracticeIcon, videosIcon, novelsIcon, studyNotesIcon, cbtCentresIcon, audioIcon, syllabusIcon, firstSessionVideoImage, firstSessionPlayIcon, thirdSessionVideoImage, thirdSessionPlayIcon;

    @FXML
    private ImageView firstSessionOneStar, firstSessionTwoStar, firstSessionThreeStar, firstSessionFourStar, firstSessionFiveStar, thirdSessionOneStar, thirdSessionTwoStar, thirdSessionThreeStar, thirdSessionFourStar, thirdSessionFiveStar;

    @FXML
    private ImageView secondSessionBookImage, secondSessionAuthorIcon, secondSessionChaptersIcon, fourthSessionBookImage, fourthSessionAuthorIcon, fourthSessionChaptersIcon, actionCloseImage, actionCbtPracticeIcon, actionVideosPracticeIcon, actionNovelsPracticeIcon, actionAudioPracticeIcon;

    @FXML
    private Panel biologyPane, englishPane, physicsPane, chemistryPane, mathematicsPane, geographyPane;

    @FXML
    private Panel cbtPracticePanel, pastQuestionsPanel, cbtGamePanel, videosPanel, audioPanel, novelsPanel,  studyNotesPanel, cbtCentresPanel, syllabusPanel;

    @FXML
    private Panel firstSession, secondSession, thirdSession, fourthSession, actionCbtPracticePanel;

    @FXML
    private Label helloText, startLearningText, topSubjectsText, biologyText, englishText, physicsText, chemistryText, mathematicsText, geographyText, activitiesText, continueSessionsText;

    @FXML
    private Label cbtPracticeText, videosText, novelsText, pastQuestionsLabel, audioText, syllabusText, studyNotesText, cbtCentresText, syllabusLabel, firstSessionVideoTitle, firstSessionTimeText, firstSessionRatingNumber, firstSessionDescriptionText;

    @FXML
    private Label secondSessionBookTitle, secondSessionAuthorName, secondSessionChaptersText, thirdSessionVideoTitle, thirdSessionTimeText, thirdSessionRatingNumber, thirdSessionDescriptionText, fourthSessionBookTitle, fourthSessionAuthorName, fourthSessionChaptersText;

    @FXML
    private Label newsFeedText, seeAllText, performanceChartText, firstPerformanceCBTText, firstPerformanceCBTDate, firstPerformancePercentText, secondPerformanceCBTText, secondPerformanceCBTDate, secondPerformancePercentText;

    @FXML
    private Label whichActionText, actionCbtPracticeText, actionVideosPracticeText, actionNovelsPracticeText, actionAudioPracticeText, viewDesktopAppText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();

        initializeFonts();


        NewsItem newsItem1 = new NewsItem("", "", "", "");
        NewsItem newsItem2 = new NewsItem("", "", "", "");
        NewsItem newsItem3 = new NewsItem("", "", "", "");
        NewsItem newsItem4 = new NewsItem("", "", "", "");
        NewsItem newsItem5 = new NewsItem("", "", "", "");
        NewsItem newsItem6 = new NewsItem("", "", "", "");

        ObservableList<NewsItem> newsList = FXCollections.observableArrayList(newsItem1, newsItem2, newsItem3, newsItem4, newsItem5, newsItem6);
        this.newsList.setCellFactory(new NewsListCellFactory());
        this.newsList.setItems(newsList);

        biologyPane.setOnMouseClicked(event -> {
            mainScrollPane.setOpacity(0.3);
            actionsPane.setVisible(true);
        });
        actionCloseImage.setOnMouseClicked(event -> {
            mainScrollPane.setOpacity(1.0);
            actionsPane.setVisible(false);
        });

        cbtPracticePanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(new HomeScreenController.InitialData(PRACTICE_SCREEN));
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        novelsPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(new HomeScreenController.InitialData(NOVELS_SCREEN));
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        actionCbtPracticePanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(new HomeScreenController.InitialData(PRACTICE_SCREEN));
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        studyNotesPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(new HomeScreenController.InitialData(NOTES_SCREEN));
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        syllabusPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(new HomeScreenController.InitialData(SYLLABUS_SCREEN));
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

        /*videosPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("videosPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });*/

        /*audioPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("audiosPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });*/

        /*cbtCentresPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("learningCenterPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });*/

        /*syllabusPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("syllabusPanel");
            ViewSwitcher.showScreen(View.SELECT_SYLLABUS_SCREEN);
        });*/

        notificationIcon.setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.ACCOUNT_NOTIFICATIONS_SCREEN);
        });

        profileImage.setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.ACCOUNT_PROFILE_SCREEN);
        });

    }

    private void initializeViews() {
        rootPane.setPadding(new Insets(0,15, 0, 0));
        notificationIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/bell_without_notification.png").toString()));
        handImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/hand_image2.png").toString()));
        actionCloseImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/action_close_icon.png").toString()));
        boyWithLaptop.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/boy_with_laptop.png").toString()));

        final Circle clip = new Circle(20, 30, 20);
        profileImage.setClip(clip);
        profileImage.setImage(new Image(getClass().getResource("/drawable/profileImage.jpg").toString()));

        biologyIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/biology_icon.png").toString()));
        englishIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/english_icon.png").toString()));
        physicsIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/physics_icon.png").toString()));
        chemistryIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/chemistry_icon.png").toString()));
        mathematicsIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/mathematics_icon.png").toString()));
        geographyIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/geography_icon.png").toString()));

        cbtPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_practice_icon.png").toString()));
//        videosIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/videos_icon.png").toString()));
        novelsIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/novels_icon.png").toString()));
        studyNotesIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/notes_icon.png").toString()));
//        cbtCentresIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_centres_icon.png").toString()));
//        audioIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/audio_icon.png").toString()));
        syllabusIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/syllabus_icon.png").toString()));

        firstSessionVideoImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_lung_image.png").toString()));
//        firstSessionPlayIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_play_icon.png").toString()));
//        firstSessionOneStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionTwoStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionThreeStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionFourStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionFiveStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));

        secondSessionBookImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_book_image.jpg").toString()));
        secondSessionAuthorIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_author_icon.png").toString()));
        secondSessionChaptersIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_chapters_icon.png").toString()));

//        thirdSessionVideoImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_video_background.jpg").toString()));
//        thirdSessionPlayIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_play_icon.png").toString()));
//        thirdSessionOneStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        thirdSessionTwoStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        thirdSessionThreeStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        thirdSessionFourStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        thirdSessionFiveStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));

//        fourthSessionBookImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_book_image.jpg").toString()));
//        fourthSessionAuthorIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_author_icon.png").toString()));
//        fourthSessionChaptersIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_chapters_icon.png").toString()));

        actionCbtPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_practice_icon.png").toString()));
        actionVideosPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/videos_icon.png").toString()));
        actionNovelsPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/novels_icon.png").toString()));
        actionAudioPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/audio_icon.png").toString()));

        mainScrollPane.widthProperty().addListener(((observable, oldValue, newValue) -> {
//            firstSession.setPrefWidth((Double) newValue/2);
//            secondSession.setPrefWidth((Double) newValue/2);
//            thirdSession.setPrefWidth((Double) newValue/2);
//            fourthSession.setPrefWidth((Double) newValue/2);
        }));

//        performanceScrollPane.setBackground(Background.EMPTY);
//        scrollHBox.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {
        helloText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 22));
        startLearningText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        topSubjectsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        biologyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        englishText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        physicsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        chemistryText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        mathematicsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        geographyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

        activitiesText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        cbtPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//        videosText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        novelsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        studyNotesText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        syllabusText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//        audioText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//        cbtCentresText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        continueSessionsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        firstSessionVideoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
//        firstSessionTimeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
//        firstSessionRatingNumber.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        firstSessionDescriptionText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

        secondSessionBookTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        secondSessionAuthorName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        secondSessionChaptersText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

//        thirdSessionVideoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//        thirdSessionTimeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        thirdSessionRatingNumber.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        thirdSessionDescriptionText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

//        fourthSessionBookTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//        fourthSessionAuthorName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
//        fourthSessionChaptersText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

        newsFeedText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        seeAllText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        performanceChartText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));

//        firstPerformanceCBTText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
//        firstPerformanceCBTDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        firstPerformancePercentText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
//
//        secondPerformanceCBTText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
//        secondPerformanceCBTDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        secondPerformancePercentText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));

        whichActionText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        actionCbtPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        actionVideosPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        actionNovelsPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        actionAudioPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

        viewDesktopAppText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
        viewDesktopAppButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

    }
}
