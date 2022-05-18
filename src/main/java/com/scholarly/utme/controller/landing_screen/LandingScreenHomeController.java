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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
    ListView<Course> recentlyViewedListView;

    @FXML
    private ImageView handImage, notificationIcon, profileImage, biologyIcon, englishIcon, physicsIcon, chemistryIcon, mathematicsIcon, geographyIcon;

    @FXML
    private ImageView cbtPracticeIcon, videosIcon, novelsIcon;

    @FXML
    private Panel cbtPracticePanel, pastQuestionsPanel, cbtGamePanel, videosPanel, audioPanel, studyNotesPanel, learningCenterPanel, syllabusPanel;

    @FXML
    private Label helloText, startLearningText, topSubjectsText, biologyText, englishText, physicsText, chemistryText, mathematicsText, geographyText, activitiesText;

    @FXML
    private Label cbtPracticeText, videosText, novelsText, pastQuestionsLabel, audiosLabel, studyNotesLabel, learningCenterLabel, syllabusLabel;

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

        /*recentlyViewedListView.setItems(items);

        recentlyViewedListView.setCellFactory(new RecentlyViewedCellFactory());
*/

        // Set fontStyles for the Label texts

        pastQuestionsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        audiosLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        learningCenterLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        studyNotesLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        syllabusLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));


        cbtPracticePanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("practicePanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });


        pastQuestionsPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("pastQuestionsPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

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

        learningCenterPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("learningCenterPanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        studyNotesPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("studyNotesPanel");
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });

        syllabusPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData("syllabusPanel");
            ViewSwitcher.showScreen(View.SELECT_SYLLABUS_SCREEN);
        });
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
    }

    private void initializeFonts() {
        helloText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.TWENTY.size));
        startLearningText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        topSubjectsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));

        biologyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        englishText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        physicsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        chemistryText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        mathematicsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        geographyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

        activitiesText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));

        cbtPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        videosText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        novelsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
    }
}
