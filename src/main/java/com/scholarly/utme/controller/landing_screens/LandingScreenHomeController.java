package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.PQScreenController;
import com.scholarly.utme.controller.note_screens.NotesScreenController;
import com.scholarly.utme.controller.novel_screens.NovelContentScreenController;
import com.scholarly.utme.data.model.listItems.NewsItem;
import com.scholarly.utme.data.model.newDb.FavoriteSubject;
import com.scholarly.utme.data.model.newDb.NoteLastSession;
import com.scholarly.utme.data.model.newDb.NovelLastSession;
import com.scholarly.utme.ui.cellFactories.SubjectGridCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenHomeVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.commons.lang3.RandomStringUtils;
import org.controlsfx.control.GridView;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@FxmlPath("/layouts/landing_screens/landing_screen_home.fxml")
public class LandingScreenHomeController implements FxmlView<LandingScreenHomeVM>, Initializable {
    private static final String TAG = "LandingScreenHomeController: ";

    @InjectViewModel
    private LandingScreenHomeVM viewModel;

    @FXML
    private BorderPane rootPane;
    @FXML
    private ScrollPane mainScrollPane, previousSessionScrollPane, performanceScrollPane;
    @FXML
    private StackPane selectSubjectPane;
    @FXML
    private TilePane favoriteSubjectsTile;
    @FXML
    private HBox previousSessionHBox;
    @FXML
    private GridView<FavoriteSubject> selectSubjectsGrid;
    @FXML
    private HBox scrollHBox;
    @FXML
    private VBox continuePreviousSessionVBox, dimmer, fontVBox;
    @FXML
    private Button viewDesktopAppButton, completeEditSubjectsButton;
    @FXML
    private ListView<NewsItem> newsList;
    @FXML
    private ImageView handImage, notificationIcon, profileImage, biologyIcon, englishIcon, physicsIcon, chemistryIcon, mathematicsIcon, geographyIcon, boyWithLaptop;
    @FXML
    private ImageView cbtPracticeIcon, videosIcon, novelsIcon, studyNotesIcon, cbtCentresIcon, audioIcon, syllabusIcon, noteSessionSubjectImage, firstSessionPlayIcon, thirdSessionVideoImage, thirdSessionPlayIcon;
    @FXML
    private ImageView firstSessionOneStar, firstSessionTwoStar, firstSessionThreeStar, firstSessionFourStar, firstSessionFiveStar, thirdSessionOneStar, thirdSessionTwoStar, thirdSessionThreeStar, thirdSessionFourStar, thirdSessionFiveStar;
    @FXML
    private ImageView novelLastSessionImage, novelLastSessionAuthorIcon, novelLastSessionChaptersIcon, fourthSessionBookImage, fourthSessionAuthorIcon, fourthSessionChaptersIcon, selectSubjectCloseIcon, actionCbtPracticeIcon, actionVideosPracticeIcon, actionNovelsPracticeIcon, actionAudioPracticeIcon;
    @FXML
    private Panel biologyPane, englishPane, physicsPane, chemistryPane, mathematicsPane, geographyPane;
    @FXML
    private Panel cbtPracticePanel, pastQuestionsPanel, cbtGamePanel, videosPanel, audioPanel, novelsPanel,  studyNotesPanel, cbtCentresPanel, syllabusPanel;
    @FXML
    private Panel noteLastSessionPanel, novelLastSessionPanel, thirdSession, fourthSession, actionCbtPracticePanel;
    @FXML
    private Label helloText, startLearningText, editSubjectsText, topSubjectsText, biologyText, englishText, physicsText, chemistryText, mathematicsText, geographyText, libraryText, continueSessionsText;
    @FXML
    private Label cbtPracticeText, videosText, novelsText, pastQuestionsLabel, audioText, syllabusText, studyNotesText, cbtCentresText, syllabusLabel, noteSessionSubjectName, firstSessionTimeText, firstSessionRatingNumber, noteLastSessionText;
    @FXML
    private Label novelLastSessionTitle, novelLastSessionAuthorName, novelLastSessionChapterText;
    @FXML
    private Label newsFeedText, seeAllText, performanceChartText, firstPerformanceCBTText, firstPerformanceCBTDate, firstPerformancePercentText, secondPerformanceCBTText, secondPerformanceCBTDate, secondPerformancePercentText;
    @FXML
    private Label selectFavoriteText, moreThanOneText, actionCbtPracticeText, actionVideosPracticeText, actionNovelsPracticeText, actionAudioPracticeText, otherAppsTitleDesc, otherAppsShortDesc;

    private final SimpleIntegerProperty count = new SimpleIntegerProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        continuePreviousSessionVBox.getChildren().removeAll(continueSessionsText, previousSessionHBox);

        Task<Void> userNameTask = new Task<>() {
            @Override
            protected Void call() {
                String userName = viewModel.getUser().getFullName().trim();
                String[] userNameSplit = userName.split(" ");

                String firstName = "";
                if (userNameSplit.length > 0) {
                    firstName = userNameSplit[0];
                } else {
                    firstName = userName;
                }

                String finalFirstName = firstName;
                Platform.runLater(() -> helloText.setText(helloText.getText() + finalFirstName));

                count.set(count.add(1).getValue());
                return null;
            }
        };

        Task<Void> imageTask = new Task<>() {
            @Override
            protected Void call() {
                boolean internetEnabled = Helper.checkNetworkConnectivity();
                String imageUrl = viewModel.getUser().getProfilePicUrl();
                String imageUrlWithQueryString = imageUrl + "?" + RandomStringUtils.random(6, true, true);

                if (imageUrl != null && !imageUrl.contains("empty")) {
                    Image image = new Image(imageUrlWithQueryString, true);
                    if (image.isError() || !internetEnabled) {
                        try {
                            InputStream inputStream = new FileInputStream("scholarly_profile_image.jpg");
                            displayProfileImage(new Image(inputStream));
                            System.out.println(TAG + "Loaded Image from File");
                        } catch (Exception e) {
                            System.out.println(TAG + "Error loading image from File system");
                        }
                    } else {
                        displayProfileImage(image);
                        System.out.println(TAG + "Loaded Image from url -> " + imageUrlWithQueryString);
                    }
                } else {
                    displayProfileImage(new Image(getClass().getResource("/drawable/account_screen_images/default_profile_image.png").toString()));
                }

                count.set(count.add(1).getValue());
                return null;
            }
        };

        Task<Void> subjectComboTask = new Task<>() {
            @Override
            protected Void call() {
                ObservableList<FavoriteSubject> subjects = viewModel.getSubjects();
                selectSubjectsGrid.setCellFactory(new SubjectGridCellFactory());
                Platform.runLater(() -> selectSubjectsGrid.setItems(subjects));

                count.set(count.add(1).getValue());
                return null;
            }
        };

        Task<Void> favouriteSubjectsTask = new Task<>() {
            @Override
            protected Void call() {
                boolean showFavSubjectDialog = PreferencesManager.getBoolean(Constants.PREF_KEY_SHOW_FAVORITE_SUBJECT_DIALOG, true);
                ObservableList<FavoriteSubject> favouriteSubjects = viewModel.getFavoriteSubjects();

                if (favouriteSubjects.isEmpty() && showFavSubjectDialog) {
                    Platform.runLater(() -> {
                        Animations.fadeIn(selectSubjectPane, 300);
                        Animations.fadeIn(dimmer, 250);
                    });
                } else {
                    Platform.runLater(() -> displayFavoriteSubjects(favouriteSubjects));
                }

                count.set(count.add(1).getValue());
                return null;
            }
        };

        favouriteSubjectsTask.setOnSucceeded(event -> editSubjectsText.setDisable(false));

        Task<Void> lastSessionTask = new Task<>() {
            @Override
            protected Void call() {
                boolean populate = populateLastSession();
                if (populate) {
                    Platform.runLater(() -> continuePreviousSessionVBox.getChildren().addAll(continueSessionsText, previousSessionHBox));
                } else {
                    Platform.runLater(() -> continuePreviousSessionVBox.getChildren().removeAll(continueSessionsText, previousSessionHBox));
                }

                count.set(count.add(1).getValue());
                return null;
            }
        };

        viewModel.getUidLoaded().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                executorService.execute(userNameTask);
                executorService.execute(imageTask);
            }
        });

        viewModel.getSubjectLoaded().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                executorService.execute(subjectComboTask);
                executorService.execute(favouriteSubjectsTask);
            }
        });

        viewModel.getLastSessionLoaded().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                executorService.execute(lastSessionTask);
            }
        });

        count.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 4) {
                executorService.shutdown();
            }
        });

        editSubjectsText.setDisable(true);
        initializeViews();
        initializeFonts();
        initializeGestures();

        editSubjectsText.setOnMouseClicked(e -> {
            Animations.fadeIn(selectSubjectPane, 300);
            Animations.fadeIn(dimmer, 250);
        });

        selectSubjectCloseIcon.setOnMouseClicked(event -> {
            Animations.fadeOut(selectSubjectPane, 300);
            Animations.fadeOut(dimmer, 250);
            PreferencesManager.putBoolean(Constants.PREF_KEY_SHOW_FAVORITE_SUBJECT_DIALOG, false);
        });

        completeEditSubjectsButton.setOnAction(event -> {
            ObservableList<FavoriteSubject> updatedFavoriteSubjects = selectSubjectsGrid.getItems().stream().filter(FavoriteSubject::isSelected).collect(Collectors.toCollection(FXCollections::observableArrayList));
            viewModel.putSubjectCombination(updatedFavoriteSubjects);

            displayFavoriteSubjects(updatedFavoriteSubjects);
            Animations.fadeOut(selectSubjectPane, 300);
            Animations.fadeOut(dimmer, 250);
            PreferencesManager.putBoolean(Constants.PREF_KEY_SHOW_FAVORITE_SUBJECT_DIALOG, false);
        });

        profileImage.setOnMouseClicked(event -> ViewSwitcher.showScreen(View.ACCOUNT_PROFILE_SCREEN));

        cbtPracticePanel.setOnMouseClicked(e -> {
            MainApplication.resetTime();
            ViewSwitcher.passData(new PQScreenController.InitialData(null, null));
            MainApplication.timeTakenTo("pass practice data");
            ViewSwitcher.showScreen(View.PQ_SCREEN);
            MainApplication.timeTakenTo("show pq screen");
        });

        novelsPanel.setOnMouseClicked(e -> {
            MainApplication.resetTime();
            ViewSwitcher.showScreen(View.NOVEL_SCREEN);
            MainApplication.timeTakenTo("show novel screen");
        });

        studyNotesPanel.setOnMouseClicked(e -> {
            MainApplication.resetTime();
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
            MainApplication.timeTakenTo("show note screen");
        });

        syllabusPanel.setOnMouseClicked(e -> {
            MainApplication.resetTime();
            ViewSwitcher.showScreen(View.SELECT_SYLLABUS_SCREEN);
            MainApplication.timeTakenTo("show syllabus screen");
        });

        noteLastSessionPanel.setOnMouseClicked(e -> {
            MainApplication.resetTime();
            NotesScreenController.InitialData data = new NotesScreenController.InitialData(
                    viewModel.getLastSessionSubject(),
                    viewModel.getNoteSubjectTopics().get(
                            viewModel.getLastSessionSubject().getId()
                    ),
                    viewModel.getSelectedNoteTopic(),
                    viewModel.getNoteSubTopics().get(
                            viewModel.getSelectedNoteTopic().getId()
                    ),
                    null,
                    viewModel.getNoteLastSection()
            );
            MainApplication.timeTakenTo("fetch note data");
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.NOTES_SCREEN);
        });

        novelLastSessionPanel.setOnMouseClicked(e -> {
            NovelContentScreenController.InitialData data = new NovelContentScreenController.InitialData(
                    viewModel.getLastSessionNovel(),
                    viewModel.getLastSessionChapters(),
                    viewModel.getLastSessionChapter()
            );

            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.NOVEL_CONTENT_SCREEN);
        });

        /*viewDesktopAppButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.APPS_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
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

        /*notificationIcon.setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.ACCOUNT_NOTIFICATIONS_SCREEN);
        });*/

        /*List<String> fontFamilies = Font.getFamilies();
        List<String> fontNames    = Font.getFontNames();*/
    }

    private void initializeViews() {
        rootPane.setPadding(new Insets(0,15, 0, 0));
//        notificationIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/bell_without_notification.png").toString()));
        handImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/hand_image.png").toString()));
        selectSubjectCloseIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/action_close_icon.png").toString()));
//        boyWithLaptop.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/boy_with_laptop.png").toString()));


        cbtPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_practice_icon.png").toString()));
//        videosIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/videos_icon.png").toString()));
        novelsIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/novels_icon.png").toString()));
        studyNotesIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/notes_icon.png").toString()));
//        cbtCentresIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_centres_icon.png").toString()));
//        audioIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/audio_icon.png").toString()));
        syllabusIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/syllabus_icon.png").toString()));

//        lastSessionSubjectImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_lung_image.png").toString()));
//        firstSessionPlayIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_play_icon.png").toString()));
//        firstSessionOneStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionTwoStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionThreeStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionFourStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
//        firstSessionFiveStar.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));


//        novelLastSessionAuthorIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_author_icon.png").toString()));
//        novelLastSessionChaptersIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/session_chapters_icon.png").toString()));

//        actionCbtPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/cbt_practice_icon.png").toString()));
//        actionVideosPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/videos_icon.png").toString()));
//        actionNovelsPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/novels_icon.png").toString()));
//        actionAudioPracticeIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/audio_icon.png").toString()));

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
//        helloText.setFont(Font.font("SansSerif", FontWeight.SEMI_BOLD, 22));
        startLearningText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        topSubjectsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        editSubjectsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));

        libraryText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        cbtPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//        videosText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        novelsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        studyNotesText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        syllabusText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//        audioText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//        cbtCentresText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

        continueSessionsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 17));
        noteSessionSubjectName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        noteLastSessionText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

        novelLastSessionTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        novelLastSessionChapterText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
//        novelLastSessionAuthorName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

//        newsFeedText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//        seeAllText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        performanceChartText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));

//        firstPerformanceCBTText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
//        firstPerformanceCBTDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        firstPerformancePercentText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
//
//        secondPerformanceCBTText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
//        secondPerformanceCBTDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        secondPerformancePercentText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));

        selectFavoriteText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        moreThanOneText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
//        actionCbtPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        actionVideosPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        actionNovelsPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        actionAudioPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

//        otherAppsTitleDesc.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
//        otherAppsShortDesc.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));
//        viewDesktopAppButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        completeEditSubjectsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));

    }

    private void initializeGestures() {
        editSubjectsText.setOnMouseEntered(e -> editSubjectsText.setUnderline(true));
        editSubjectsText.setOnMouseExited(e -> editSubjectsText.setUnderline(false));

        cbtPracticePanel.setOnMouseEntered(e -> cbtPracticePanel.setStyle("-fx-background-color: #3289C6; -fx-background-radius: 10; -fx-cursor: hand;"));
        cbtPracticePanel.setOnMouseExited(e -> cbtPracticePanel.setStyle("-fx-background-color: #1B68AF; -fx-background-radius: 10;"));

        novelsPanel.setOnMouseEntered(e -> novelsPanel.setStyle("-fx-background-color: #F1723B; -fx-background-radius: 10; -fx-cursor: hand;"));
        novelsPanel.setOnMouseExited(e -> novelsPanel.setStyle("-fx-background-color: #AD4518; -fx-background-radius: 10;"));

        studyNotesPanel.setOnMouseEntered(e -> studyNotesPanel.setStyle("-fx-background-color: #F5A100; -fx-background-radius: 10; -fx-cursor: hand;"));
        studyNotesPanel.setOnMouseExited(e -> studyNotesPanel.setStyle("-fx-background-color: #E18400; -fx-background-radius: 10;"));

        syllabusPanel.setOnMouseEntered(e -> syllabusPanel.setStyle("-fx-background-color: #FF29A3; -fx-background-radius: 10; -fx-cursor: hand;"));
        syllabusPanel.setOnMouseExited(e -> syllabusPanel.setStyle("-fx-background-color: #D4107A; -fx-background-radius: 10;"));

        noteLastSessionPanel.setOnMouseEntered(e -> noteLastSessionPanel.setStyle("-fx-background-color: rgba(18, 175, 32, 0.05); -fx-background-radius: 10; -fx-border-color: #A2CAA6; -fx-border-radius: 10; -fx-cursor: hand;"));
        noteLastSessionPanel.setOnMouseExited(e -> noteLastSessionPanel.setStyle("-fx-background-color: rgba(18, 175, 32, 0.05); -fx-background-radius: 10;"));

        novelLastSessionPanel.setOnMouseEntered(e -> novelLastSessionPanel.setStyle("-fx-background-color: rgba(18, 175, 32, 0.05); -fx-background-radius: 10; -fx-border-color: #A2CAA6; -fx-border-radius: 10; -fx-cursor: hand;"));
        novelLastSessionPanel.setOnMouseExited(e -> novelLastSessionPanel.setStyle("-fx-background-color: rgba(18, 175, 32, 0.05); -fx-background-radius: 10;"));
    }

    private void displayFavoriteSubjects(ObservableList<FavoriteSubject> selectedSubjects) {
        favoriteSubjectsTile.getChildren().clear();
        selectedSubjects.forEach(subject -> {
            Panel panel = new Panel();
            panel.setPrefSize(110, 90);
            ImageView subjectImage = new ImageView(new Image(getClass().getResource("/drawable/subject_images/" + subject.getShortTitle() + "_image.png").toString()));
            panel.setTop(subjectImage);

            Label subjectLabel = new Label(subject.getTitle());
            subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            subjectLabel.setTextFill(Paint.valueOf(subject.getColorCode()));
            subjectLabel.setPadding(new Insets(5, 0, 0, 0));
            panel.setBottom(subjectLabel);

            panel.setStyle("-fx-background-color: rgba(143, 152, 255, 0.10); -fx-background-radius: 7");
            panel.setPadding(new Insets(10, 0, 10, 15));

            panel.setOnMouseEntered(event -> {
                ViewSwitcher.getRootScene().setCursor(Cursor.HAND);
            });
            panel.setOnMouseExited(event -> {
                ViewSwitcher.getRootScene().setCursor(Cursor.DEFAULT);
            });

            panel.setOnMouseClicked(event -> {
                MainApplication.resetTime();
                ViewSwitcher.passData(new PQScreenController.InitialData(Screens.PRACTICE_SCREEN, subject));
                ViewSwitcher.showScreen(View.PQ_SCREEN);
                MainApplication.timeTakenTo("show pq screen");
            });

            favoriteSubjectsTile.getChildren().add(panel);
        });
    }

    private boolean populateLastSession() {
        NoteLastSession noteLastSession = viewModel.getNoteLastSession();
        NovelLastSession novelLastSession = viewModel.getNovelLastSession();
        previousSessionHBox.getChildren().removeAll(noteLastSessionPanel, novelLastSessionPanel);

        if (noteLastSession != null) {
            previousSessionHBox.getChildren().add(noteLastSessionPanel);

            noteLastSessionText.setText(noteLastSession.getSectionTitle());
            noteSessionSubjectName.setText(viewModel.getLastSessionSubject().getTitle());
            noteSessionSubjectImage.setImage(new Image(getClass().getResource("/drawable/subject_images/" + viewModel.getLastSessionSubject().getShortTitle() + "_image.png").toString()));

        }
        if (novelLastSession != null) {
            previousSessionHBox.getChildren().add(previousSessionHBox.getChildren().size(), novelLastSessionPanel);

            novelLastSessionTitle.setText(viewModel.getLastSessionNovel().getNovel().getName());
            novelLastSessionChapterText.setText(novelLastSession.getChapterTitle());
            novelLastSessionImage.setImage(new Image(getClass().getResource("/assets/images/novels/" + viewModel.getLastSessionNovel().getNovel().getImagePath()).toString()));
        }

        return noteLastSession != null || novelLastSession != null;
    }

    private void displayProfileImage(Image image) {
        Platform.runLater(() -> {
            Circle clip = new Circle(25, 25, 25);
            profileImage.setClip(clip);
            Rectangle2D imageBounds = new Rectangle2D(0, 0, image.getWidth(), image.getHeight());
            profileImage.setFitWidth(50);
            profileImage.setFitHeight(50);
            profileImage.setViewport(imageBounds);
            profileImage.setSmooth(true);
            profileImage.setCache(true);
            profileImage.setImage(image);
        });
    }
}
