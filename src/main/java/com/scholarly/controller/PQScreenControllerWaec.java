package com.scholarly.controller;


import com.scholarly.async.PQScreen;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.ui.utils.*;
import com.scholarly.viewmodels.PQScreenWaecVM;
import com.scholarly.viewmodels.SubjectListViewWaecVM;
import de.saxsys.mvvmfx.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/PQScreenWaec.fxml")
public class PQScreenControllerWaec implements FxmlView<PQScreenWaecVM>, Initializable{
    private static final String TAG = "HomeScreenController: ";

    @InjectViewModel
    private PQScreenWaecVM viewModel;

    @FXML
    private ToggleButton practiceButton, pastQuestionButton, cbtGameButton, videosButton, audiosButton, learningCenterButton;

    @FXML
    private ImageView appImage;

    @FXML
    private Button backButton;

    @FXML
    private Label pageTitle, scholarlyText;

    @FXML
    private StackPane contentPane;

    @FXML
    private ProgressIndicator progressBar;

    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FF9900; -fx-border-width: 0 0 0 5;";
    private static final String IDLE_BUTTON_STYLE = "-fx-cursor: hand;";

    private static SubjectListViewControllerWaec subjectListController;

    private static Parent subjectListView;
    private final ToggleGroup toggleGroup = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressBar.setVisible(true);

        ViewTuple<SubjectListViewControllerWaec, SubjectListViewWaecVM> subjectListViewWaecTuple = FluentViewLoader.fxmlView(SubjectListViewControllerWaec.class).load();
        subjectListController = subjectListViewWaecTuple.getCodeBehind();
        subjectListView = subjectListViewWaecTuple.getView();

        practiceButton.setDisable(true);
        pastQuestionButton.setDisable(true);
        cbtGameButton.setDisable(true);

        subjectListController.getIsLoadingDone().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                practiceButton.setDisable(false);
                pastQuestionButton.setDisable(false);
                cbtGameButton.setDisable(false);
            }
        });

        initializeViews();
        initializeFonts();

        viewModel.processInitialData(getInitialData());

        Screens selectedScreen = getInitialData().previousScreen;
        PQSubject selectedSubject = getInitialData().selectedSubject;

        if (selectedScreen == Screens.PAST_QUESTION_SCREEN) {
            pageTitle.setText("Study Past Questions");
            toggleGroup.selectToggle(pastQuestionButton);
            subjectListController.setOption(SubjectListOption.STUDY);
            subjectListController.setSelectedSubject(selectedSubject);
            selectButton(subjectListView, pastQuestionButton);
        } else if (selectedScreen == Screens.CBT_GAME_SCREEN) {
            pageTitle.setText("CBT Game");
            toggleGroup.selectToggle(cbtGameButton);
            subjectListController.setOption(SubjectListOption.CBT_GAME);
            subjectListController.setSelectedSubject(selectedSubject);
            selectButton(subjectListView, cbtGameButton);
        } else {
            pageTitle.setText("CBT Practice");
            toggleGroup.selectToggle(practiceButton);
            subjectListController.setOption(SubjectListOption.PRACTICE);
            subjectListController.setSelectedSubject(selectedSubject);
            selectButton(subjectListView, practiceButton);
        }

        toggleGroup.getToggles().addAll(practiceButton, pastQuestionButton, cbtGameButton);

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
                boolean removed = subjectListController.tabMenu.getTabs().remove(subjectListController.theoryTab);
                if (removed)
                    subjectListController.tabMenu.setTabMinWidth(subjectListController.tabMenu.getTabMinWidth() * 2);
                selectButton(subjectListView, cbtGameButton);
            }
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

        appImage.setImage(new Image(getClass().getResource("/drawable/app_icon.png").toString()));

        practiceButton.setBackground(Background.EMPTY);
        practiceButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/cbt_practice_icon.png").toString())));
        practiceButton.setGraphicTextGap(20);

        pastQuestionButton.setBackground(Background.EMPTY);
        pastQuestionButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/past_quest_icon.png").toString())));
        pastQuestionButton.setGraphicTextGap(20);

        cbtGameButton.setBackground(Background.EMPTY);
        cbtGameButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/cbt_game_icon.png").toString())));
        cbtGameButton.setGraphicTextGap(20);

        /*videosButton.setBackground(Background.EMPTY);
        videosButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/videos_icon.png").toString())));
        videosButton.setGraphicTextGap(20);*/

        /*audiosButton.setBackground(Background.EMPTY);
        audiosButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/audios_icon.png").toString())));
        audiosButton.setGraphicTextGap(20);*/

        /*learningCenterButton.setBackground(Background.EMPTY);
        learningCenterButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/syllabus_icon.png").toString())));
        learningCenterButton.setGraphicTextGap(20);*/

    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 20));
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));

        practiceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        pastQuestionButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        cbtGameButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        videosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        audiosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        learningCenterButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

    }

    private void showActivateDialog() {

    }

    private void changeButtonStyle(ToggleButton pressedButton) {
        practiceButton.setStyle(IDLE_BUTTON_STYLE);
        pastQuestionButton.setStyle(IDLE_BUTTON_STYLE);
        cbtGameButton.setStyle(IDLE_BUTTON_STYLE);
//        videosButton.setStyle(null);
//        audiosButton.setStyle(null);
//        learningCenterButton.setStyle(null);

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
        PQScreen.logOut();
        ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }

    private InitialData getInitialData() {
        return (InitialData) ViewSwitcher.retrieveData();
    }

    public static class InitialData {
        private Screens previousScreen;
        private PQSubject selectedSubject;

        public InitialData(Screens previousScreen, PQSubject selectedSubject) {
            this.previousScreen = previousScreen;
            this.selectedSubject = selectedSubject;
        }

        public Screens getPreviousScreen() {
            return previousScreen;
        }

        public PQSubject getSelectedSubject() {
            return selectedSubject;
        }
    }
}
