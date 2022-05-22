package com.scholarly.utme.controller;


import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.SelectSubjectScreenVM;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static com.scholarly.utme.controller.SubjectListViewController.*;

@FxmlPath("/layouts/SelectSubjectScreen.fxml")
public class SelectSubjectScreenController implements FxmlView<SelectSubjectScreenVM>, Initializable {

    @FXML
    private ToggleButton practiceButton, lessonNoteButton, cbtGameButton, videosButton, audiosButton, learningCenterButton, studyNotesButton;

    @FXML
    private Button backButton;

    @FXML
    private Label cbtPracticeText;

    @FXML
    private StackPane contentPane;

    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FF9900; -fx-border-width: 0 0 0 5;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewTuple<SubjectListViewController, SubjectListViewVM> viewTuple = FluentViewLoader.fxmlView(SubjectListViewController.class).load();
        SubjectListViewController subjectListController = viewTuple.getCodeBehind();
        contentPane.getChildren().add(viewTuple.getView());

        initializeViews();

        initializeFonts();

        ToggleGroup toggleGroup = new ToggleGroup();

        toggleGroup.getToggles().addAll(practiceButton, cbtGameButton, lessonNoteButton, videosButton, audiosButton, learningCenterButton, studyNotesButton);

        practiceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            changeButtonStyle(practiceButton);
            if (newValue) {
                subjectListController.setOption(SubjectListOption.PRACTICE);
                animate(contentPane);

                if (!subjectListController.tabMenu.getTabs().contains(subjectListController.theoryTab)){
                    subjectListController.tabMenu.getTabs().add(subjectListController.theoryTab);
                }
            }
            if (oldValue) {
                subjectListController.setOption(SubjectListOption.PRACTICE);
                animate(contentPane);
            }
        });

        lessonNoteButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            changeButtonStyle(lessonNoteButton);
            if (newValue) {
                subjectListController.setOption(SubjectListOption.STUDY);
                animate(contentPane);

                if (!subjectListController.tabMenu.getTabs().contains(subjectListController.theoryTab)){
                    subjectListController.tabMenu.getTabs().add(subjectListController.theoryTab);
                }
            }
            if (oldValue) {
                subjectListController.setOption(SubjectListOption.STUDY);
                animate(contentPane);
            }
        });

        cbtGameButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            changeButtonStyle(cbtGameButton);
            if (newValue) {
                subjectListController.setOption(SubjectListOption.CBT_GAME);
                animate(contentPane);
                subjectListController.tabMenu.getTabs().remove(subjectListController.theoryTab);
            }
            if (oldValue) {
                subjectListController.setOption(SubjectListOption.CBT_GAME);
                animate(contentPane);
            }
        });

        studyNotesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            changeButtonStyle(studyNotesButton);
            if (newValue) {
                ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
                animate(contentPane);
            }
        });

        videosButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            changeButtonStyle(videosButton);
        }));

        audiosButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            changeButtonStyle(audiosButton);
        }));

        learningCenterButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            changeButtonStyle(learningCenterButton);
        }));

        studyNotesButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            changeButtonStyle(studyNotesButton);
        }));

        if (ViewSwitcher.retrieveData() instanceof String){
            String selectedMenuOption = (String) ViewSwitcher.retrieveData();
            ToggleButton selectedToggle = selectToggle(selectedMenuOption);

            toggleGroup.selectToggle(selectedToggle);
            // It is 'false' because the studyNotesButton selectedProperty's was initially toggled to 'true' when user navigated from the HOME_SCREEN to SELECT_NOTE_SCREEN
            // This will make the button toggle when clicked thus switching selectedProperty to true
            toggleGroup.getSelectedToggle().setSelected(false);
        }

    }

    private void initializeViews() {
        practiceButton.setBackground(Background.EMPTY);
        lessonNoteButton.setBackground(Background.EMPTY);
        cbtGameButton.setBackground(Background.EMPTY);
        videosButton.setBackground(Background.EMPTY);
        audiosButton.setBackground(Background.EMPTY);
        learningCenterButton.setBackground(Background.EMPTY);
        studyNotesButton.setBackground(Background.EMPTY);
        //backButton.setBackground(Background.EMPTY);

        final Circle clip = new Circle(15, 15,20);
        //backButton.setClip(clip);
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);
    }

    private void initializeFonts() {
        practiceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        lessonNoteButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        cbtGameButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        videosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        audiosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        learningCenterButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        studyNotesButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

        cbtPracticeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));

    }

    /**
     * Accepts a Button ID and retrieves the corresponding ToggleButton
     * @param buttonId the id of the Button
     * @return a ToggleButton corresponding to the Button ID
     */
    private ToggleButton selectToggle(String buttonId){
        return switch (buttonId) {
            case "practicePanel" -> practiceButton;
            case "pastQuestionsPanel" -> lessonNoteButton;
            case "cbtGamePanel" -> cbtGameButton;
            case "videosPanel" -> videosButton;
            case "audioPanel" -> audiosButton;
            case "learningCenterPanel" -> learningCenterButton;
            case "studyNotesPanel" -> studyNotesButton;
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
        lessonNoteButton.setStyle(null);
        cbtGameButton.setStyle(null);
        videosButton.setStyle(null);
        audiosButton.setStyle(null);
        learningCenterButton.setStyle(null);
        studyNotesButton.setStyle(null);

        pressedButton.setStyle(PRESSED_BUTTON_STYLE);
    }

    public void homeTextClicked() {
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }
}
