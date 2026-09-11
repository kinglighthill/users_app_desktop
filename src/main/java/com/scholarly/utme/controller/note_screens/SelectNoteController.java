package com.scholarly.utme.controller.note_screens;


import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.viewmodels.note_screens.SelectNoteVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
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
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/note_screens/SelectNoteScreen.fxml")
public class SelectNoteController implements FxmlView<SelectNoteVM>, Initializable {
    private static final String TAG = "SelectNoteController: ";

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

        initializeViews();
        initializeFonts();

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
                NoteSubject selectedSubject = (NoteSubject) newValue.getUserData();
                viewModel.setSelectedNoteSubject(selectedSubject);
                emptyTopicListLabel.setVisible(false);
            } else {
                viewModel.setSelectedNoteSubject(null);
                emptyTopicListLabel.setVisible(true);
            }
        });

        viewModel.getNoteSubjects().forEach(subject -> {
            ToggleButton button = new ToggleButton();
            button.setUserData(subject);
            toggleGroup.getToggles().add(button);


            button.setMinHeight(70);
            button.setMaxHeight(70);
            button.setPadding(new Insets(0, 0, 0, 20));
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setText(subject.getTitle());

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
                Pair<NoteTopic, NoteSubTopic> selectedTopic = (Pair<NoteTopic, NoteSubTopic>) newValue.getUserData();
                viewModel.setSelectedNoteTopic(selectedTopic.getKey());
                viewModel.setSelectedNoteSubTopic(selectedTopic.getValue());

                if (!commenceButton.isVisible()) {
                    Animations.translateIn(commenceButton, 300);
                }
            } else {
                Animations.translateOut(commenceButton, 300);
            }
        });

        viewModel.selectedNoteSubjectProperty().addListener((observable, oldValue, newValue) -> {
            topicListToggleGroup.getToggles().clear();
            topicListVBox.getChildren().clear();

            if (newValue != null) {
                viewModel.getNoteSubjectTopics().get(newValue.getId()).forEach(topic -> {

                    VBox vBox = new VBox();
                    TitledPane titledPane = new TitledPane(topic.getTitle(), vBox);

//                    System.out.println("Subtopics for note with subject id -> " + newValue.getId() + " and topic id -> " + topic.getTopicId() + " is " + Helper.toString(viewModel.getNoteSubTopics().get(newValue.getId())));
//                    System.out.println(TAG + "Subtopics for note with subject id -> " + newValue.getId() + " and topic id -> " + topic.getTopicId() + " is " + Helper.toString(viewModel.getNoteSubTopics().get(topic.getTopicId())));
                    viewModel.getNoteSubTopics().get(topic.getTopicId()).forEach(subTopic -> {
                        if (subTopic.getTopicId() == topic.getTopicId()) {
                            ToggleButton button = new ToggleButton();
                            Pair<NoteTopic, NoteSubTopic> data = new Pair<>(topic, subTopic);
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
//            System.out.println(TAG + "SelectedNoteSubject -> " + Helper.toString(viewModel.getSelectedNoteSubject()));
//            System.out.println(TAG + "SelectedNoteTopic -> " + Helper.toString(viewModel.getSelectedNoteTopic()));
//            System.out.println(TAG + "SelectedNoteSubtopics -> " + Helper.toString(viewModel.getNoteSubTopics().get(viewModel.getSelectedNoteTopic().getTopicId())));
//            System.out.println(TAG + "SelectedNoteSubtopic -> " + Helper.toString(viewModel.getSelectedNoteSubTopic()));

            NotesScreenController.InitialData data = new NotesScreenController.InitialData(
                    viewModel.getSelectedNoteSubject(),
                    viewModel.getSelectedNoteTopic(),
                    viewModel.getNoteSubTopics().get(
                            viewModel.getSelectedNoteTopic().getTopicId()
                    ).stream().filter(subTopic -> subTopic.getTopicId() == viewModel.getSelectedNoteTopic().getTopicId()).collect(Collectors.toList()),
                    viewModel.getSelectedNoteSubTopic()
            );
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.NOTES_SCREEN);
        });


    }

    private void initializeViews() {
        ImageView backButtonImage = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        backButtonImage.setFitHeight(30);
        backButtonImage.setFitWidth(35);
        backButton.setBackground(Background.EMPTY);
        backButton.setGraphic(backButtonImage);
    }

    private void initializeFonts() {
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        topicsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        emptyTopicListLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        commenceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
    }


    public void backButtonClicked() {
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }
}
