package com.scholarly.utme.controller;


import com.scholarly.utme.controller.audio_video_screens.AudioVideoSubjectListViewController;
import com.scholarly.utme.controller.novel_screens.NovelScreenController;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.HomeScreenVM;
import com.scholarly.utme.viewmodels.novel_screens.NovelScreenVM;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
import com.scholarly.utme.viewmodels.audio_video_screens.AudioVideoSubjectListViewVM;
import de.saxsys.mvvmfx.*;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static com.scholarly.utme.controller.SubjectListViewController.*;

@FxmlPath("/layouts/HomeScreen.fxml")
public class HomeScreenController implements FxmlView<HomeScreenVM>, Initializable {

    @FXML
    private ToggleButton practiceButton, pastQuestionButton, cbtGameButton, novelsButton, videosButton, audiosButton, learningCenterButton, studyNotesButton;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewTuple<SubjectListViewController, SubjectListViewVM> subjectListViewTuple = FluentViewLoader.fxmlView(SubjectListViewController.class).load();
        subjectListController = subjectListViewTuple.getCodeBehind();

        ViewTuple<NovelScreenController, NovelScreenVM> novelListViewTuple = FluentViewLoader.fxmlView(NovelScreenController.class).load();

        ViewTuple<AudioVideoSubjectListViewController, AudioVideoSubjectListViewVM> videoAudioSubjectListViewTuple = FluentViewLoader.fxmlView(AudioVideoSubjectListViewController.class).load();
        AudioVideoSubjectListViewController audioVideoSubjectListViewController = videoAudioSubjectListViewTuple.getCodeBehind();

        initializeViews();

        initializeFonts();

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(practiceButton, pastQuestionButton, cbtGameButton, novelsButton, videosButton, audiosButton, learningCenterButton, studyNotesButton);

        practiceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected Practice button");
            changeButtonStyle(practiceButton);
            if (newValue) {
                pageTitle.setText("CBT Practice");
                subjectListController.setOption(SubjectListOption.PRACTICE);
                animate(contentPane);
                if (!subjectListController.tabMenu.getTabs().contains(subjectListController.theoryTab)){
                    subjectListController.tabMenu.getTabs().add(subjectListController.theoryTab);
                }
                contentPane.getChildren().clear();
                contentPane.getChildren().add(subjectListViewTuple.getView());
            }
            if (oldValue) {
                subjectListController.setOption(SubjectListOption.PRACTICE);
            }
        });

        pastQuestionButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected Past Question");
            changeButtonStyle(pastQuestionButton);
            if (newValue) {
                pageTitle.setText("Study Past Questions");
                subjectListController.setOption(SubjectListOption.STUDY);
                animate(contentPane);
                if (!subjectListController.tabMenu.getTabs().contains(subjectListController.theoryTab)){
                    subjectListController.tabMenu.getTabs().add(subjectListController.theoryTab);
                }
                contentPane.getChildren().clear();
                contentPane.getChildren().add(subjectListViewTuple.getView());
            }
            if (oldValue) {
                subjectListController.setOption(SubjectListOption.STUDY);

            }
        });

        cbtGameButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected CBT Game");
            changeButtonStyle(cbtGameButton);
            if (newValue) {
                pageTitle.setText("CBT Game");
                subjectListController.setOption(SubjectListOption.CBT_GAME);
                animate(contentPane);
                subjectListController.tabMenu.getTabs().remove(subjectListController.theoryTab);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(subjectListViewTuple.getView());
            }
            if (oldValue) {
                subjectListController.setOption(SubjectListOption.CBT_GAME);
            }
        });

        novelsButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("Selected novels button");
            changeButtonStyle(novelsButton);
            animate(contentPane);
            if (newValue) {
                pageTitle.setText("Novels");
                contentPane.getChildren().clear();
                contentPane.getChildren().add(novelListViewTuple.getView());
            }
        }));

        videosButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("Selected video button");
            changeButtonStyle(videosButton);
            animate(contentPane);
            if (newValue) {
                pageTitle.setText("Videos");
                contentPane.getChildren().clear();
                contentPane.getChildren().add(videoAudioSubjectListViewTuple.getView());
                audioVideoSubjectListViewController.setType(AudioVideoSubjectListViewVM.Type.VIDEO);
            }
        }));

        audiosButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("Selected audio button");
            changeButtonStyle(audiosButton);
            animate(contentPane);
            if (newValue) {
                pageTitle.setText("Audios");
                contentPane.getChildren().clear();
                contentPane.getChildren().add(videoAudioSubjectListViewTuple.getView());
                audioVideoSubjectListViewController.setType(AudioVideoSubjectListViewVM.Type.AUDIO);

            }
        }));

        learningCenterButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("Selected learning center");
            changeButtonStyle(learningCenterButton);
        }));

        studyNotesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            changeButtonStyle(studyNotesButton);
            if (newValue) {
                ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
                animate(contentPane);
            }
        });

        if (ViewSwitcher.retrieveData() instanceof String){
            String selectedMenuOption = (String) ViewSwitcher.retrieveData();
            System.out.println("Selected menu option -> " + selectedMenuOption);
            ToggleButton selectedToggle = getToggle(selectedMenuOption);

            toggleGroup.selectToggle(selectedToggle);
            // It is 'false' because the studyNotesButton selectedProperty's was initially toggled to 'true' when user navigated from the HOME_SCREEN to SELECT_NOTE_SCREEN
            // This will make the button toggle when clicked thus switching selectedProperty to true
            toggleGroup.getSelectedToggle().setSelected(false);
        }

    }

    private void initializeViews() {
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

        videosButton.setBackground(Background.EMPTY);
        videosButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/videos_icon.png").toString())));
        videosButton.setGraphicTextGap(20);

        audiosButton.setBackground(Background.EMPTY);
        audiosButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/audios_icon.png").toString())));
        audiosButton.setGraphicTextGap(20);

        learningCenterButton.setBackground(Background.EMPTY);
        learningCenterButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/syllabus_icon.png").toString())));
        learningCenterButton.setGraphicTextGap(20);

        studyNotesButton.setBackground(Background.EMPTY);
        studyNotesButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/notes_icon.png").toString())));
        studyNotesButton.setGraphicTextGap(20);


        appImage.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));

        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);
    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.TWENTY.size));

        practiceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        pastQuestionButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        cbtGameButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        novelsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        videosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        audiosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        learningCenterButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        studyNotesButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));

    }

    /**
     * Accepts a Button ID and retrieves the corresponding ToggleButton
     * @param buttonId the id of the Button
     * @return a ToggleButton corresponding to the Button ID
     */
    private ToggleButton getToggle(String buttonId){
        return switch (buttonId) {
            case "practicePanel", "practiceButton" -> practiceButton;
            case "pastQuestionsPanel", "pastQuestionsButton" -> pastQuestionButton;
            case "cbtGamePanel", "cbtGameButton" -> cbtGameButton;
            case "novelsPanel", "novelsButton" -> novelsButton;
            case "videosPanel", "videosButton" -> videosButton;
            case "audiosPanel", "audiosButton" -> audiosButton;
            case "learningCenterPanel", "learningCenterButton" -> learningCenterButton;
            case "studyNotesPanel", "studyNotesButton" -> studyNotesButton;
            default -> null;
        };
    }

    /**
     * Shows a screen change effect when selected menu option changes
     * @param node on which the effect is displayed
     */
    public void animate(Node node){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);

        fadeTransition.setFromValue(0.1);
        fadeTransition.setToValue(1.0);

        fadeTransition.play();
    }

    private void changeButtonStyle(ToggleButton pressedButton) {
        practiceButton.setStyle(null);
        pastQuestionButton.setStyle(null);
        cbtGameButton.setStyle(null);
        novelsButton.setStyle(null);
        videosButton.setStyle(null);
        audiosButton.setStyle(null);
        learningCenterButton.setStyle(null);
        studyNotesButton.setStyle(null);

        pressedButton.setStyle(PRESSED_BUTTON_STYLE);
    }

    public void homeTextClicked() {
        subjectListController.dispose();
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }
}
