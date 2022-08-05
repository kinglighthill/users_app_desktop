package com.scholarly.utme.controller;


import com.scholarly.utme.controller.audio_video_screens.AudioVideoSubjectListViewController;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.controller.novel_screens.NovelScreenController;
import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.*;
import com.scholarly.utme.viewmodels.novel_screens.NovelScreenVM;
import com.scholarly.utme.viewmodels.audio_video_screens.AudioVideoSubjectListViewVM;
import de.saxsys.mvvmfx.*;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import static com.scholarly.utme.controller.SubjectListViewController.*;
import static com.scholarly.utme.util.Constants.*;

@FxmlPath("/layouts/HomeScreen.fxml")
public class HomeScreenController implements FxmlView<HomeScreenVM>, Initializable {

    @InjectViewModel
    private HomeScreenVM viewModel;

    @FXML
    private ToggleButton practiceButton, pastQuestionButton, cbtGameButton, novelsButton, videosButton, audiosButton, learningCenterButton, studyNotesButton, syllabusButton;

    @FXML
    private ImageView appImage;

    @FXML
    private Button backButton;

    @FXML
    private Label pageTitle, scholarlyText;

    @FXML
    private StackPane contentPane;

    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FF9900; -fx-border-width: 0 0 0 5;";

    private SubjectListViewController subjectListController;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private static final String TAG = "HomeScreenController::  ";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewTuple<SubjectListViewController, SubjectListViewVM> subjectListViewTuple = FluentViewLoader.fxmlView(SubjectListViewController.class).load();
        subjectListController = subjectListViewTuple.getCodeBehind();
        Parent subjectListView = subjectListViewTuple.getView();

        ViewTuple<NovelScreenController, NovelScreenVM> novelListViewTuple = FluentViewLoader.fxmlView(NovelScreenController.class).load();
        Parent novelListView = novelListViewTuple.getView();

        ViewTuple<AudioVideoSubjectListViewController, AudioVideoSubjectListViewVM> audioVideoSubjectListViewTuple = FluentViewLoader.fxmlView(AudioVideoSubjectListViewController.class).load();
        AudioVideoSubjectListViewController audioVideoSubjectListViewController = audioVideoSubjectListViewTuple.getCodeBehind();
        Parent audioVideoView = audioVideoSubjectListViewTuple.getView();

        ViewTuple<SelectNoteController, SelectNoteVM> selectNoteViewTuple = FluentViewLoader.fxmlView(SelectNoteController.class).load();
        Parent studyNotesView = selectNoteViewTuple.getView();

        ViewTuple<SelectSyllabusController, SelectSyllabusVM> syllabusViewTuple = FluentViewLoader.fxmlView(SelectSyllabusController.class).load();
        Parent syllabusView = syllabusViewTuple.getView();

        initializeViews();
        initializeFonts();

        viewModel.processInitialData(getInitialData());

        if (viewModel.getSelectedScreen().equalsIgnoreCase(PRACTICE_SCREEN)) {
            pageTitle.setText("CBT Practice");
            subjectListController.setOption(SubjectListOption.PRACTICE);
            selectButton(subjectListView, practiceButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(PAST_QUESTION_SCREEN)) {
            pageTitle.setText("Study Past Questions");
            selectButton(subjectListView, pastQuestionButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(CBT_GAME_SCREEN)) {
            pageTitle.setText("CBT Game");
            selectButton(subjectListView, cbtGameButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(NOVELS_SCREEN)) {
            pageTitle.setText("Novels");
            selectButton(novelListView, novelsButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(VIDEOS_SCREEN)) {
//            selectButton(audioVideoView, videosButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(AUDIOS_SCREEN)) {
//            selectButton(audioVideoView, audiosButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(LEARNING_CENTER_SCREEN)) {
//            selectButton(audioVideoView, learningCenterButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(NOTES_SCREEN)) {
            pageTitle.setText("Select Note");
            selectButton(studyNotesView, studyNotesButton);
        } else if (viewModel.getSelectedScreen().equalsIgnoreCase(SYLLABUS_SCREEN)) {
            pageTitle.setText("Select Syllabus");
            selectButton(syllabusView, syllabusButton);
        }


        toggleGroup.getToggles().addAll(practiceButton, pastQuestionButton, cbtGameButton, novelsButton, studyNotesButton);

        practiceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pageTitle.setText("CBT Practice");
                subjectListController.setOption(SubjectListOption.PRACTICE);
                if (!subjectListController.tabMenu.getTabs().contains(subjectListController.theoryTab)){
                    subjectListController.tabMenu.getTabs().add(subjectListController.theoryTab);
                    subjectListController.tabMenu.setTabMinWidth(subjectListController.tabMenu.getTabMinWidth() / 2);
                }
                selectButton(subjectListView, practiceButton);
            }
        });

        pastQuestionButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pageTitle.setText("Study Past Questions");
                subjectListController.setOption(SubjectListOption.STUDY);
                if (!subjectListController.tabMenu.getTabs().contains(subjectListController.theoryTab)){
                    subjectListController.tabMenu.getTabs().add(subjectListController.theoryTab);
                    subjectListController.tabMenu.setTabMinWidth(subjectListController.tabMenu.getTabMinWidth() / 2);
                }
                selectButton(subjectListView, pastQuestionButton);
            }
        });

        cbtGameButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pageTitle.setText("CBT Game");
                subjectListController.setOption(SubjectListOption.CBT_GAME);
                subjectListController.tabMenu.getTabs().remove(subjectListController.theoryTab);
                subjectListController.tabMenu.setTabMinWidth(subjectListController.tabMenu.getTabMinWidth() * 2);
                selectButton(subjectListView, cbtGameButton);
            }
        });

        novelsButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            pageTitle.setText("Novels");
            selectButton(novelListView, novelsButton);
        }));

        studyNotesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            pageTitle.setText("Select Note");
            selectButton(studyNotesView, studyNotesButton);
        });

        syllabusButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            pageTitle.setText("Select Syllabus");
            selectButton(syllabusView, syllabusButton);
        });

        /*videosButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                pageTitle.setText("Videos");
                audioVideoSubjectListViewController.setType(AudioVideoSubjectListViewVM.Type.VIDEO);
                selectButton(audioVideoView, videosButton);
            }
        }));*/

        /*audiosButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                pageTitle.setText("Audios");
                audioVideoSubjectListViewController.setType(AudioVideoSubjectListViewVM.Type.AUDIO);
                selectButton(audioVideoView, audiosButton);
            }
        }));*/

        /*learningCenterButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            changeButtonStyle(learningCenterButton);
        }));*/

    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        appImage.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));

        practiceButton.setBackground(Background.EMPTY);
        practiceButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/cbt_practice_icon.png").toString())));
        practiceButton.setGraphicTextGap(20);

        pastQuestionButton.setBackground(Background.EMPTY);
        pastQuestionButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/past_quest_icon.png").toString())));
        pastQuestionButton.setGraphicTextGap(20);

        cbtGameButton.setBackground(Background.EMPTY);
        cbtGameButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/cbt_game_icon.png").toString())));
        cbtGameButton.setGraphicTextGap(20);

        novelsButton.setBackground(Background.EMPTY);
        novelsButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/novels_icon.png").toString())));
        novelsButton.setGraphicTextGap(20);

        /*videosButton.setBackground(Background.EMPTY);
        videosButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/videos_icon.png").toString())));
        videosButton.setGraphicTextGap(20);*/

        /*audiosButton.setBackground(Background.EMPTY);
        audiosButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/audios_icon.png").toString())));
        audiosButton.setGraphicTextGap(20);*/

        /*learningCenterButton.setBackground(Background.EMPTY);
        learningCenterButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/syllabus_icon.png").toString())));
        learningCenterButton.setGraphicTextGap(20);*/

        studyNotesButton.setBackground(Background.EMPTY);
        studyNotesButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/notes_icon.png").toString())));
        studyNotesButton.setGraphicTextGap(20);

        syllabusButton.setBackground(Background.EMPTY);
        syllabusButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/syllabus_icon.png").toString())));
        syllabusButton.setGraphicTextGap(20);

    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 20));
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));

        practiceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        pastQuestionButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        cbtGameButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        novelsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        videosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        audiosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        learningCenterButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        studyNotesButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        syllabusButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

    }


    private void changeButtonStyle(ToggleButton pressedButton) {
        practiceButton.setStyle(null);
        pastQuestionButton.setStyle(null);
        cbtGameButton.setStyle(null);
        novelsButton.setStyle(null);
//        videosButton.setStyle(null);
//        audiosButton.setStyle(null);
//        learningCenterButton.setStyle(null);
        studyNotesButton.setStyle(null);
        syllabusButton.setStyle(null);

        pressedButton.setStyle(PRESSED_BUTTON_STYLE);
    }

    private void selectButton(Parent view, ToggleButton toggleButton) {
        Animations.animate(contentPane);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);
        changeButtonStyle(toggleButton);
    }

    public void homeTextClicked() {
        subjectListController.dispose();
        ViewSwitcher.passData(new LandingScreenController.InitialData("homeScreen"));
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }

    private InitialData getInitialData() {
        return (InitialData) ViewSwitcher.retrieveData();
    }

    public static class InitialData {
        private String screen;

        public InitialData(String screen) {
            this.screen = screen;
        }

        public String getScreen() {
            return screen;
        }
    }
}
