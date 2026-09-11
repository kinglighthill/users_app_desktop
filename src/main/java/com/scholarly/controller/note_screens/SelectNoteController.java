package com.scholarly.controller.note_screens;


import com.scholarly.MainApplication;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.data.model.newDb.*;
import com.scholarly.ui.utils.*;
import com.scholarly.util.Helper;
import com.scholarly.viewmodels.note_screens.SelectNoteVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private VBox subjectListVBox, topicListVBox, dimmer, subtopicsVBox, activateNowDialog;
    @FXML
    private ScrollPane subjectListScrollPane, topicListScrollPane, subtopicScrollPane;
    @FXML
    private ListView<Topic> topicList;
    @FXML
    private Label emptyTopicListLabel, subjectsTitle, topicsTitle, pageTitle, subtopicsLabel, subtopicsStartAllText, activateHeaderText;
    @FXML
    private Button commenceButton, backButton, subtopicsCommenceButton, activateNowButton;
    @FXML
    private ImageView startAllExpandIcon, subtopicCloseDialog, activateNowPadlockIcon, activateNowCloseIcon, greenTickIcon1, greenTickIcon2, greenTickIcon3, greenTickIcon4, greenTickIcon5;

    @FXML
    private ProgressIndicator progressBar;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0; -fx-cursor: hand;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0; -fx-cursor: hand;";

    final String PRESSED_STYLE2 = "-fx-background-color: #EDEDED; -fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";
    final String HOVERED_BUTTON_STYLE2 = "-fx-background-color: #F7F7F7; -fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeFonts();

        ToggleGroup noteSubjectsToggleGroup = new ToggleGroup();

        ToggleGroup topicListToggleGroup = new ToggleGroup();
        ToggleGroup subtopicsListToggleGroup = new ToggleGroup();

        viewModel.getSubjectsLoaded().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
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

                    ImageView graphic = new ImageView(Helper.getSubjectIcon(getClass(), subject.getTitle()));
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

                    button.selectedProperty().addListener((btnObservable, btnOldValue, btnNewValue) -> {
                        if (btnNewValue) {
                            button.setStyle(PRESSED_STYLE2);
                        } else {
                            button.setStyle(IDLE_BUTTON_STYLE);
                        }
                    });

                    subjectListVBox.getChildren().add(button);
                });

                noteSubjectsToggleGroup.selectedToggleProperty().addListener((subjObservable, subjOldValue, subjNewValue) -> {
                    if (subjNewValue != null) {
                        NoteSubject selectedSubject = (NoteSubject) subjNewValue.getUserData();
                        viewModel.setSelectedNoteSubject(selectedSubject);
                        emptyTopicListLabel.setVisible(false);
                    } else {
                        viewModel.setSelectedNoteSubject(null);
                        emptyTopicListLabel.setVisible(true);
                    }
                });

                topicListToggleGroup.selectedToggleProperty().addListener((topicObservable, topicOldValue, topicNewValue) -> {
                    if (topicNewValue != null) {
                        NoteTopic selectedTopic = (NoteTopic) topicNewValue.getUserData();

                        if(!selectedTopic.isFree()) {
                            dimmer.setVisible(true);
                            Animations.translateIn(activateNowDialog, 300);
                        } else {
                            viewModel.setSelectedNoteTopic(selectedTopic);

                            if (viewModel.getNoteSubTopics().get(selectedTopic.getId()).isEmpty()) {
                                if (!commenceButton.isVisible())
                                    Animations.translateIn(commenceButton, 300);
                            } else {
                                subtopicsVBox.getChildren().clear();
                                viewModel.getNoteSubTopics().get(selectedTopic.getId()).forEach(subTopic -> {
                                    ToggleButton button = new ToggleButton();
                                    button.setText(subTopic.getTitle());
                                    if (subTopic.getTitle().length() > 50)
                                        button.setTooltip(new Tooltip(subTopic.getTitle()));
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
                                            button.setStyle(PRESSED_STYLE2);
//                                    button.setTextFill(Color.WHITE);
                                        } else {
                                            button.setStyle(IDLE_BUTTON_STYLE);
                                            button.setTextFill(Color.BLACK);
                                        }
                                    });

                                    button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 13));
                                });
                                if (commenceButton.isVisible())
                                    Animations.translateOut(commenceButton, 300);
                                dimmer.setVisible(true);
                                Animations.translateIn(subtopicsDialog, 300);
                            }
                        }
                    } else {
                        if (commenceButton.isVisible())
                            Animations.translateOut(commenceButton, 300);
                    }
                });

                subtopicsListToggleGroup.selectedToggleProperty().addListener(((subTopicObservable, subTopicOldValue, subTopicNewValue) -> {
                    if (subTopicNewValue != null) {
                        viewModel.setSelectedNoteSubTopic((NoteSubTopic) subTopicNewValue.getUserData());
                        if (!subtopicsCommenceButton.isVisible()) {
                            Animations.translateIn(subtopicsCommenceButton, 200);
                        }
                    } else {
                        viewModel.setSelectedNoteSubTopic(null);
                        Animations.translateOut(subtopicsCommenceButton, 200);
                    }
                }));

                viewModel.selectedNoteSubjectProperty().addListener((selectedNoteObservable, topicOldValue, selectedNoteNewValue) -> {
                    topicListToggleGroup.getToggles().clear();
                    topicListVBox.getChildren().clear();

                    if (selectedNoteNewValue != null) {
                        showProgressBar();

                        ExecutorService executorService = Executors.newSingleThreadExecutor();

                        Task<List<Node>> topicsTask = new Task<>() {
                            @Override
                            protected List<Node> call() {
                                List<Node> topicNodes = new ArrayList<>();
                                viewModel.getNoteSubjectTopics().get(selectedNoteNewValue.getId()).forEach(topic -> {
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
                                            button.setStyle(PRESSED_STYLE2);
                                        } else {
                                            button.setStyle(IDLE_BUTTON_STYLE);
                                            button.setTextFill(Color.BLACK);
                                        }
                                    });

                                    button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 13));

                                    if (topic.isFree()) {
                                        pane.getChildren().add(button);
                                    } else {
                                        pane.getChildren().addAll(button, padlockIcon);
                                    }
                                    topicNodes.add(pane);
                                });
                                return topicNodes;
                            }
                        };
                        topicsTask.setOnSucceeded(
                                event -> Platform.runLater(() -> {
                                    topicListVBox.getChildren().addAll(topicsTask.valueProperty().getValue());
                                    hideProgressBar();
                                })
                        );
                        executorService.execute(topicsTask);
                        executorService.shutdown();
                    }
                });
            }
        });

        commenceButton.setOnAction(event -> startNoteScreen(viewModel.getSelectedNoteSubTopic()));

        subtopicsCommenceButton.setOnAction(event -> {
            startNoteScreen(viewModel.getSelectedNoteSubTopic());
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
                startNoteScreen(null);
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

        activateNowCloseIcon.setOnMouseClicked(event -> {
            if (topicListToggleGroup.getSelectedToggle() != null)
                topicListToggleGroup.getSelectedToggle().setSelected(false);
            dimmer.setVisible(false);
            Animations.translateOut(activateNowDialog, 300);
        });

        activateNowButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        ImageView backButtonImage = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        backButtonImage.setFitHeight(25);
        backButtonImage.setPreserveRatio(true);
        backButton.setBackground(Background.EMPTY);
        backButton.setGraphic(backButtonImage);

        activateHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));

        startAllExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        subtopicCloseDialog.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));

        activateNowCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        activateNowPadlockIcon.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/padlock_icon.png").toString()));
        greenTickIcon1.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon2.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon3.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon4.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon5.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
    }

    private void initializeFonts() {
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        topicsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        emptyTopicListLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        commenceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        subtopicsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 17));
        subtopicsStartAllText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
    }

    private void startNoteScreen(NoteSubTopic noteSubTopic) {
        MainApplication.resetTime();
        NotesScreenController.InitialData data = new NotesScreenController.InitialData(
                viewModel.getSelectedNoteSubject(),
                viewModel.getNoteSubjectTopics().get(
                        viewModel.getSelectedNoteSubject().getId()
                ),
                viewModel.getSelectedNoteTopic(),
                viewModel.getNoteSubTopics().get(
                        viewModel.getSelectedNoteTopic().getId()
                ),
                noteSubTopic,
                null
        );
        MainApplication.timeTakenTo("get note data");
        ViewSwitcher.passData(data);
        MainApplication.timeTakenTo("pass data");
        ViewSwitcher.showScreen(View.NOTES_SCREEN);
        MainApplication.timeTakenTo("show screen");
    }

    public void backButtonClicked() {
        ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        topicListVBox.setVisible(true);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        topicListVBox.setVisible(false);
    }
}
