package com.scholarly.utme.controller;


import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.HomeScreenVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.*;
import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

import static com.scholarly.utme.controller.SubjectListViewController.*;

@FxmlPath("/layouts/HomeScreen.fxml")
public class HomeScreenController implements FxmlView<HomeScreenVM>, Initializable {

    @FXML
    public ToggleButton practiceButton, lessonNoteButton, cbtGameButton, videosButton, audiosButton, learningCenterButton, studyNotesButton;

    @FXML
    private StackPane contentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewTuple<SubjectListViewController, SubjectListViewVM> viewTuple = FluentViewLoader.fxmlView(SubjectListViewController.class).load();
        SubjectListViewController subjectListController = viewTuple.getCodeBehind();

        contentPane.getChildren().add(viewTuple.getView());

        ToggleGroup toggleGroup = new ToggleGroup();


        toggleGroup.getToggles().addAll(practiceButton, cbtGameButton, lessonNoteButton, videosButton, audiosButton, learningCenterButton, studyNotesButton);

        practiceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
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
        practiceButton.setFocusTraversable(false);

        lessonNoteButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
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
        lessonNoteButton.setFocusTraversable(false);

        cbtGameButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
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
        cbtGameButton.setFocusTraversable(false);

        studyNotesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
                animate(contentPane);
            }
        });

        studyNotesButton.setFocusTraversable(false);

        videosButton.setFocusTraversable(false);

        audiosButton.setFocusTraversable(false);

        learningCenterButton.setFocusTraversable(false);

        studyNotesButton.setFocusTraversable(false);

        if (ViewSwitcher.retrieveData() instanceof String){
            String selectedMenuOption = (String) ViewSwitcher.retrieveData();
            ToggleButton selectedToggle = selectToggle(selectedMenuOption);

            toggleGroup.selectToggle(selectedToggle);
            // It is 'false' because the studyNotesButton selectedProperty's was initially toggled to 'true' when user navigated from the HOME_SCREEN to SELECT_NOTE_SCREEN
            // This will make the button toggle when clicked thus switching selectedProperty to true
            toggleGroup.getSelectedToggle().setSelected(false);
        }

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

    public void homeTextClicked() {
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }
}
