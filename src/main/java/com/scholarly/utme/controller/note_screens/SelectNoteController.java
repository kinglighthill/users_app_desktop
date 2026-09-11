package com.scholarly.utme.controller.note_screens;


import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.ui.utils.*;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/note_screens/SelectNoteScreen.fxml")
public class SelectNoteController implements FxmlView<SelectNoteVM>, Initializable {
    private static final String TAG = "SelectNoteController: ";

    @InjectViewModel
    private SelectNoteVM viewModel;

    @FXML
    private BorderPane subtopicsDialog;
    @FXML
    private Panel startAllPanel;
    @FXML
    private VBox subjectListVBox, topicListVBox, dimmer, subtopicsVBox;
    @FXML
    private ScrollPane subjectListScrollPane, topicListScrollPane, subtopicScrollPane;
    @FXML
    private ListView<Topic> topicList;
    @FXML
    private Label emptyTopicListLabel, subjectsTitle, topicsTitle, pageTitle, subtopicsLabel, subtopicsStartAllText;
    @FXML
    private Button commenceButton, backButton, subtopicsCommenceButton;
    @FXML
    private ImageView startAllExpandIcon, subtopicCloseDialog;


    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    final String PRESSED_STYLE2 = "-fx-background-color: #EDEDED; -fx-background-radius: 10; -fx-border-radius: 10;";
    final String HOVERED_BUTTON_STYLE2 = "-fx-background-color: #F7F7F7; -fx-background-radius: 10; -fx-border-radius: 10;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        ToggleGroup noteSubjectsToggleGroup = new ToggleGroup();
        noteSubjectsToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
            noteSubjectsToggleGroup.getToggles().add(button);


            button.setMinHeight(70);
            button.setMaxHeight(70);
            button.setPadding(new Insets(0, 0, 0, 20));
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setText(subject.getTitle());
            button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 15));

            ImageView graphic = new ImageView(new Image(getClass().getResource("/drawable/subject_images/" + subject.getShortTitle() + "_image.png").toString()));
            graphic.setFitWidth(27);
            graphic.setFitHeight(27);
            button.setGraphic(graphic);
            button.setGraphicTextGap(25.0);

            button.setStyle(IDLE_BUTTON_STYLE);
            button.setOnMouseEntered(e -> {
                if (!button.isSelected()) {
                    button.setStyle(HOVERED_BUTTON_STYLE2);
                }
            });
            button.setOnMouseExited(e -> {
                if (!button.isSelected()) {
                    button.setStyle(IDLE_BUTTON_STYLE);
                }
            });

            button.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    button.setStyle(PRESSED_STYLE2);
//                    button.setTextFill(Color.WHITE);
                } else {
                    button.setStyle(IDLE_BUTTON_STYLE);
//                    button.setTextFill(Color.BLACK);
                }
            });

            subjectListVBox.getChildren().add(button);
        });


        ToggleGroup topicListToggleGroup = new ToggleGroup();
        ToggleGroup subtopicsListToggleGroup = new ToggleGroup();

        topicListToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
//                Pair<NoteTopic, NoteSubTopic> selectedTopic = (Pair<NoteTopic, NoteSubTopic>) newValue.getUserData();
//                viewModel.setSelectedNoteTopic(selectedTopic.getKey());
//                viewModel.setSelectedNoteSubTopic(selectedTopic.getValue());

                NoteTopic selectedTopic = (NoteTopic) newValue.getUserData();
                viewModel.setSelectedNoteTopic(selectedTopic);

                if (viewModel.getNoteSubTopics().get(selectedTopic.getId()).isEmpty()) {
                    if (!commenceButton.isVisible())
                        Animations.translateIn(commenceButton, 300);
                } else {
                    subtopicsVBox.getChildren().clear();
                    viewModel.getNoteSubTopics().get(selectedTopic.getId()).forEach(subTopic -> {
                        ToggleButton button = new ToggleButton();
                        button.setText(subTopic.getTitle());
                        button.setUserData(subTopic);
                        subtopicsVBox.getChildren().add(button);
                        subtopicsListToggleGroup.getToggles().add(button);

                        button.setMinHeight(48);
                        button.setMaxHeight(48);
                        button.setPadding(new Insets(0, 15, 0, 15));
                        button.setAlignment(Pos.BASELINE_LEFT);
                        button.setMaxWidth(Integer.MAX_VALUE);
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
                    });
                    if (commenceButton.isVisible())
                        Animations.translateOut(commenceButton, 300);
                    dimmer.setVisible(true);
                    Animations.translateIn(subtopicsDialog, 300);
                }
            } else {
                if (commenceButton.isVisible())
                    Animations.translateOut(commenceButton, 300);
            }
        });

        subtopicsListToggleGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                viewModel.setSelectedNoteSubTopic((NoteSubTopic) newValue.getUserData());
                if (!subtopicsCommenceButton.isVisible()) {
                    Animations.translateIn(subtopicsCommenceButton, 200);
                }
            } else {
                viewModel.setSelectedNoteSubTopic(null);
                Animations.translateOut(subtopicsCommenceButton, 200);
            }
        }));

        viewModel.selectedNoteSubjectProperty().addListener((observable, oldValue, newValue) -> {
            topicListToggleGroup.getToggles().clear();
            topicListVBox.getChildren().clear();

            if (newValue != null) {
                viewModel.getNoteSubjectTopics().get(newValue.getId()).forEach(topic -> {
                    StackPane pane = new StackPane();
                    ToggleButton button = new ToggleButton();
                    button.setText(topic.getTitle());
                    button.setUserData(topic);
                    topicListToggleGroup.getToggles().add(button);

                    button.setMinHeight(48);
                    button.setMaxHeight(48);
                    button.setPadding(new Insets(0, 0, 0, 20));
                    button.setAlignment(Pos.BASELINE_LEFT);
                    button.setMaxWidth(Double.MAX_VALUE);
                    button.setText(topic.getTitle());

                    pane.setMinHeight(48);
                    pane.setMinWidth(48);
                    pane.setMaxWidth(Double.MAX_VALUE);

                    ImageView padlockIcon = new ImageView(new Image(getClass().getResource("/drawable/novel_images/padlock_icon.png").toString()));
                    padlockIcon.setFitWidth(15);
                    padlockIcon.setFitHeight(20);
                    StackPane.setAlignment(padlockIcon, Pos.CENTER_RIGHT);
                    StackPane.setMargin(padlockIcon, new Insets(0, 20, 0, 0));

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

                    button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 13));

                    if (topic.isFree()) {
                        pane.getChildren().add(button);
                    } else {
                        button.setDisable(true);
                        pane.getChildren().addAll(button, padlockIcon);
                    }
                    topicListVBox.getChildren().add(pane);
                });
            }
        });

        commenceButton.setOnAction(event -> {
//            System.out.println(TAG + "SelectedNoteSubject -> " + Helper.toString(viewModel.getSelectedNoteSubject()));
//            System.out.println(TAG + "SelectedNoteTopic -> " + Helper.toString(viewModel.getSelectedNoteTopic()));
//            System.out.println(TAG + "SelectedNoteSubtopics -> " + Helper.toString(viewModel.getNoteSubTopics().get(viewModel.getSelectedNoteTopic().getTopicId())));
//            System.out.println(TAG + "SelectedNoteSubtopic -> " + Helper.toString(viewModel.getSelectedNoteSubTopic()));

            NotesScreenController.InitialData data = new NotesScreenController.InitialData(
                    viewModel.getSelectedNoteSubject(),
                    viewModel.getNoteSubjectTopics().get(
                            viewModel.getSelectedNoteSubject().getId()
                    ),
                    viewModel.getSelectedNoteTopic(),
                    viewModel.getNoteSubTopics().get(
                            viewModel.getSelectedNoteTopic().getId()
                    ),
                    viewModel.getSelectedNoteSubTopic(),
                    null
            );
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.NOTES_SCREEN);
        });

        subtopicsCommenceButton.setOnAction(event -> {
//            System.out.println(TAG + "SelectedNoteSubject -> " + Helper.toString(viewModel.getSelectedNoteSubject()));
//            System.out.println(TAG + "SelectedNoteTopic -> " + Helper.toString(viewModel.getSelectedNoteTopic()));
//            System.out.println(TAG + "SelectedNoteSubtopics -> " + Helper.toString(viewModel.getNoteSubTopics().get(viewModel.getSelectedNoteTopic().getId())));
//            System.out.println(TAG + "SelectedNoteSubtopics with Filter -> " + Helper.toString(viewModel.getNoteSubTopics().get(viewModel.getSelectedNoteTopic().getId()).stream().filter(subTopic -> subTopic.getTopicId() == viewModel.getSelectedNoteTopic().getId()).collect(Collectors.toList())));
//            System.out.println(TAG + "SelectedNoteSubtopic -> " + Helper.toString(viewModel.getSelectedNoteSubTopic()));

            NotesScreenController.InitialData data = new NotesScreenController.InitialData(
                    viewModel.getSelectedNoteSubject(),
                    viewModel.getNoteSubjectTopics().get(
                            viewModel.getSelectedNoteSubject().getId()
                    ),
                    viewModel.getSelectedNoteTopic(),
                    viewModel.getNoteSubTopics().get(
                            viewModel.getSelectedNoteTopic().getId()
                    ),
                    viewModel.getSelectedNoteSubTopic(),
                    null
            );
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.NOTES_SCREEN);
        });

        startAllPanel.setOnMouseEntered(event -> {
            if (subtopicsListToggleGroup.getSelectedToggle() != null) {
                startAllPanel.setStyle(IDLE_BUTTON_STYLE);
            } else {
                startAllPanel.setStyle(HOVERED_BUTTON_STYLE);
            }
        });
        startAllPanel.setOnMouseExited(event -> {
            startAllPanel.setStyle(IDLE_BUTTON_STYLE);
        });
        startAllPanel.setOnMouseClicked(event -> {
            if (subtopicsListToggleGroup.getSelectedToggle() == null) {
                NotesScreenController.InitialData data = new NotesScreenController.InitialData(
                        viewModel.getSelectedNoteSubject(),
                        viewModel.getNoteSubjectTopics().get(
                                viewModel.getSelectedNoteSubject().getId()
                        ),
                        viewModel.getSelectedNoteTopic(),
                        viewModel.getNoteSubTopics().get(
                                viewModel.getSelectedNoteTopic().getId()
                        ),
                        null,
                        null
                );
                ViewSwitcher.passData(data);
                ViewSwitcher.showScreen(View.NOTES_SCREEN);
            }
        });

        subtopicCloseDialog.setOnMouseClicked(event -> {
            topicListToggleGroup.getSelectedToggle().setSelected(false);
            if (subtopicsListToggleGroup.getSelectedToggle() != null) {
                subtopicsListToggleGroup.getSelectedToggle().setSelected(false);
            }
            dimmer.setVisible(false);
            Animations.translateOut(subtopicsDialog, 300);
        });

    }

    private void initializeViews() {
        ImageView backButtonImage = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        backButtonImage.setFitHeight(25);
        backButtonImage.setPreserveRatio(true);
        backButton.setBackground(Background.EMPTY);
        backButton.setGraphic(backButtonImage);

        startAllExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        subtopicCloseDialog.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
    }

    private void initializeFonts() {
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        topicsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        emptyTopicListLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        commenceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        subtopicsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        subtopicsStartAllText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
    }


    public void backButtonClicked() {
        ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }
}
