package com.scholarly.utme.controller;


import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.Topic;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.SelectNoteVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/SelectNoteScreen.fxml")
public class SelectNoteController implements FxmlView<SelectNoteVM>, Initializable {

    @InjectViewModel
    private SelectNoteVM viewModel;

    @FXML
    private ListView<Subject> subjectList;

    @FXML
    private ListView<Topic> topicList;

    @FXML
    private Label emptyTopicListLabel, subjectsTitle, topicsTitle, pageTitle;

    @FXML
    private Button commenceButton, backButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        subjectList.setItems(viewModel.getSubjects());
        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Subject>) c -> {
            if (c.getList().size() == 1) {
                Subject subject = c.getList().get(0);
                viewModel.setSelectedSubject(subject);
                emptyTopicListLabel.setVisible(false);
            } else {
                viewModel.setSelectedSubject(null);
                emptyTopicListLabel.setVisible(true);
            }
        });


        topicList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Topic>) c -> {
            if (c.getList().size() == 1) {
                Topic topic = c.getList().get(0);
                viewModel.setSelectedTopic(topic);
                if (!commenceButton.isVisible()) {
                    showCommenceButton();
                }
            } else {
                hideCommenceButton();
                viewModel.setSelectedTopic(null);
            }
        });
        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            topicList.setItems(viewModel.getSubjectTopics().get(newValue.getSubjectName()));
        });

        commenceButton.setOnAction(event -> {
            NotesScreenController.InitialData data = new NotesScreenController.InitialData(viewModel.getSelectedSubject(), viewModel.getSubjectTopics().get(viewModel.getSelectedSubject().getSubjectName()), viewModel.getSelectedTopic());
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.NOTES_SCREEN);
        });

        ImageView view = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        view.setFitHeight(25);
        view.setPreserveRatio(true);

        backButton.setGraphic(view);
        backButton.setBackground(Background.EMPTY);

        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        topicsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        emptyTopicListLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        commenceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
    }

    private void showCommenceButton() {
        commenceButton.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(commenceButton);

        fadeTransition.play();
    }

    private void hideCommenceButton() {
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(commenceButton);

        fadeTransition.setOnFinished(event -> {
            commenceButton.setVisible(true);
        });

        fadeTransition.play();
    }
}
