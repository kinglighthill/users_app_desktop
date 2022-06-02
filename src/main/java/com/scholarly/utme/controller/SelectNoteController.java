package com.scholarly.utme.controller;


import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.Topic;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.SelectNoteVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/SelectNoteScreen.fxml")
public class SelectNoteController implements FxmlView<SelectNoteVM>, Initializable {

    @InjectViewModel
    private SelectNoteVM viewModel;

//    @FXML
//    private ListView<Subject> subjectList;

    @FXML
    private VBox subjectListVBox, topicListVBox;

    @FXML
    private ScrollPane subjectListScrollPane, topicListScrollPane;

    @FXML
    private ListView<Topic> topicList;

    @FXML
    private Label emptyTopicListLabel, subjectsTitle, topicsTitle, pageTitle;

    @FXML
    private Button commenceButton, backButton;


    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        subjectList.setItems(viewModel.getSubjects());
//        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Subject>) c -> {
//            if (c.getList().size() == 1) {
//                Subject subject = c.getList().get(0);
//                viewModel.setSelectedSubject(subject);
//                emptyTopicListLabel.setVisible(false);
//            } else {
//                viewModel.setSelectedSubject(null);
//                emptyTopicListLabel.setVisible(true);
//            }
//        });

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                Subject selectedSubject = (Subject) newValue.getUserData();
                viewModel.setSelectedSubject(selectedSubject);
                emptyTopicListLabel.setVisible(false);
            } else {
                viewModel.setSelectedSubject(null);
                emptyTopicListLabel.setVisible(true);
            }
        });

        viewModel.getSubjects().forEach(subject -> {
            ToggleButton button = new ToggleButton();
            button.setUserData(subject);
            toggleGroup.getToggles().add(button);


            button.setMinHeight(70);
            button.setMaxHeight(70);
            button.setPadding(new Insets(0, 0, 0, 20));
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setText(subject.getSubjectName());

            button.setStyle(IDLE_BUTTON_STYLE);
            button.setOnMouseEntered(e -> {
                if (!button.isSelected()) {
                    button.setStyle(HOVERED_BUTTON_STYLE);
                }
            });
            button.setOnMouseExited(e -> {
                if (!button.isSelected()) {
                    button.setStyle(IDLE_BUTTON_STYLE);
                }
            });

            button.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    button.setStyle(PRESSED_STYLE);
                    button.setTextFill(Color.WHITE);
                } else {
                    button.setStyle(IDLE_BUTTON_STYLE);
                    button.setTextFill(Color.BLACK);
                }
            });

            button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 13));

            subjectListVBox.getChildren().add(button);
        });


        ToggleGroup topicListToggleGroup = new ToggleGroup();
        topicListToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                Pair<Topic, SubTopic> selectedTopic = (Pair<Topic, SubTopic>) newValue.getUserData();
                viewModel.setSelectedTopic(selectedTopic.getKey());
                viewModel.setSelectedSubTopic(selectedTopic.getValue());

                if (!commenceButton.isVisible()) {
                    showCommenceButton();
                }
            } else {
                hideCommenceButton();
            }
        });

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            topicListToggleGroup.getToggles().clear();
            topicListVBox.getChildren().clear();

            if (newValue != null) {
                viewModel.getSubjectTopics().get(newValue.getSubjectName()).forEach(topic -> {

                    VBox vBox = new VBox();
                    TitledPane titledPane = new TitledPane(topic.getTitle(), vBox);

                    viewModel.getSubjectSubTopics().get(newValue.getSubjectName()).forEach(subTopic -> {
                        if (subTopic.getTopicId() == topic.getId()) {
                            ToggleButton button = new ToggleButton();
                            Pair<Topic, SubTopic> data = new Pair<>(topic, subTopic);
                            button.setUserData(data);
                            topicListToggleGroup.getToggles().add(button);


                            button.setMinHeight(48);
                            button.setMaxHeight(48);
                            button.setPadding(new Insets(0, 0, 0, 20));
                            button.setAlignment(Pos.BASELINE_LEFT);
                            button.setMaxWidth(Double.MAX_VALUE);
                            button.setText(subTopic.getTitle());

                            button.setStyle(IDLE_BUTTON_STYLE);
                            button.setOnMouseEntered(e -> {
                                if (!button.isSelected()) {
                                    button.setStyle(HOVERED_BUTTON_STYLE);
                                }
                            });
                            button.setOnMouseExited(e -> {
                                if (!button.isSelected()) {
                                    button.setStyle(IDLE_BUTTON_STYLE);
                                }
                            });

                            button.selectedProperty().addListener((observe, old, newVal) -> {
                                if (newVal) {
                                    button.setStyle(PRESSED_STYLE);
                                    button.setTextFill(Color.WHITE);
                                } else {
                                    button.setStyle(IDLE_BUTTON_STYLE);
                                    button.setTextFill(Color.BLACK);
                                }
                            });

                            button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 12));

                            vBox.getChildren().add(button);
                        }
                    });

                    topicListVBox.getChildren().add(titledPane);
                });
            }
        });


//        topicList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Topic>) c -> {
//            if (c.getList().size() == 1) {
//                Topic topic = c.getList().get(0);
//                viewModel.setSelectedTopic(topic);
//                if (!commenceButton.isVisible()) {
//                    showCommenceButton();
//                }
//            } else {
//                hideCommenceButton();
//                viewModel.setSelectedTopic(null);
//            }
//        });

        commenceButton.setOnAction(event -> {
            NotesScreenController.InitialData data = new NotesScreenController.InitialData(
                    viewModel.getSelectedSubject(),
                    viewModel.getSelectedTopic(),
                    viewModel.getSubjectSubTopics().get(
                            viewModel.getSelectedSubject().getSubjectName()
                    ).stream().filter(subTopic -> subTopic.getTopicId() == viewModel.getSelectedTopic().getId()).collect(Collectors.toList()),
                    viewModel.getSelectedSubTopic()
            );
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

        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setFromX(1);

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setDuration(Duration.millis(200));
        scaleTransition.setNode(commenceButton);

        scaleTransition.play();
//        fadeTransition.play();
    }

    private void hideCommenceButton() {
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(commenceButton);

        fadeTransition.setOnFinished(event -> {
            commenceButton.setVisible(false);
        });

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);
        scaleTransition.setDuration(Duration.millis(300));
        scaleTransition.setNode(commenceButton);

        scaleTransition.setOnFinished(event -> {
            commenceButton.setVisible(false);
        });

        scaleTransition.play();
//        fadeTransition.play();
    }

    public void backButtonClicked() {
        ViewSwitcher.passData("studyNotesPanel");
        ViewSwitcher.showScreen(View.HOME_SCREEN);
    }
}
