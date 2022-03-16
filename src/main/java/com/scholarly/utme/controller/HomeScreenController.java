package com.scholarly.utme.controller;


import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.HomeScreenVM;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

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
                subjectListController.setOption(SubjectListOption.PRACTICE);
            }
        });

        lessonNoteButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                subjectListController.setOption(SubjectListOption.STUDY);
            }
        });

        cbtGameButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                subjectListController.setOption(SubjectListOption.CBT_GAME);
            }
        });

        studyNotesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
            }
        });

        Button selectedButton = (Button) ViewSwitcher.retrieveData();
        String buttonId = selectedButton.getId();

        ToggleButton selectedToggle = selectToggle(buttonId);

        toggleGroup.selectToggle(selectedToggle);
    }

    /**
     * Accepts a Button ID and retrieves the corresponding ToggleButton
     * @param buttonId the id of the Button
     * @return a ToggleButton corresponding to the Button ID
     */
    private ToggleButton selectToggle(String buttonId){
        return switch (buttonId) {
            case "practiceButton" -> practiceButton;
            case "pastQuestionsButton" -> lessonNoteButton;
            case "cbtGameButton" -> cbtGameButton;
            case "videosButton" -> videosButton;
            case "audioButton" -> audiosButton;
            case "learningCenterButton" -> learningCenterButton;
            case "studyNotesButton" -> studyNotesButton;
            default -> null;
        };
    }

}
