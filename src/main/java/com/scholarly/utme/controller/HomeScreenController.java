package com.scholarly.utme.controller;


import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.HomeScreenVM;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
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

    private ToggleGroup toggleGroup;

    @FXML
    private ToggleButton practiceButton, lessonNoteButton, cbtGameButton, videosButton, audiosButton, learningCenterButton, studyNotesButton;

    @FXML
    private StackPane contentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewTuple<SubjectListViewController, SubjectListViewVM> viewTuple = FluentViewLoader.fxmlView(SubjectListViewController.class).load();
        SubjectListViewController subjectListController = viewTuple.getCodeBehind();

        contentPane.getChildren().add(viewTuple.getView());

        toggleGroup = new ToggleGroup();


        toggleGroup.getToggles().addAll(practiceButton, cbtGameButton, lessonNoteButton, videosButton, audiosButton, learningCenterButton);

        practiceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                animate(contentPane);
                subjectListController.setOption(SubjectListOption.PRACTICE);
            }
        });
        practiceButton.setFocusTraversable(false);

        lessonNoteButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                animate(contentPane);
                subjectListController.setOption(SubjectListOption.STUDY);
            }
        });
        lessonNoteButton.setFocusTraversable(false);

        cbtGameButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                animate(contentPane);
                subjectListController.setOption(SubjectListOption.CBT_GAME);
            }
        });
        cbtGameButton.setFocusTraversable(false);

        studyNotesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                animate(contentPane);
                ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
            }
        });

        studyNotesButton.setFocusTraversable(false);

        videosButton.setFocusTraversable(false);

        audiosButton.setFocusTraversable(false);

        learningCenterButton.setFocusTraversable(false);

        studyNotesButton.setFocusTraversable(false);

        Panel selectedMenuOption = (Panel) ViewSwitcher.retrieveData();
        String optionId = selectedMenuOption.getId();
        ToggleButton selectedToggle = selectToggle(optionId);

        toggleGroup.selectToggle(selectedToggle);
        toggleGroup.getSelectedToggle().setSelected(true);
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

}
