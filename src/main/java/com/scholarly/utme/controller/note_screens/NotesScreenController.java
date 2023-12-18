package com.scholarly.utme.controller.note_screens;


import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.model.Highlights;
import com.scholarly.utme.data.model.Note;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.listItems.UnorderedListItem;
import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentViewType.*;
import com.scholarly.utme.ui.cellFactories.NoteContentListCellFactory;
import com.scholarly.utme.ui.cellFactories.UnorderedListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.viewmodels.note_screens.NotesScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@FxmlPath("/layouts/note_screens/NotesScreen.fxml")
public class NotesScreenController implements FxmlView<NotesScreenVM>, Initializable {
    private static final String TAG = "NotesScreenController: ";

    private enum FontSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    private enum HighlightColors {
        PINK("#FABFBF"),
        PURPLE("#DCA6FF"),
        BLUE("#A6FFEF"),
        GREEN("#A6FFAC"),
        DARK_PURPLE("#B2A6FF"),
        ORANGE("#FFD0A6");

        public final String colorCode;


        HighlightColors(String colorCode) {
            this.colorCode = colorCode;
        }
    }

    private final List<String> highlightColorsList = new ArrayList<>();

    @InjectViewModel
    private NotesScreenVM viewModel;

    @FXML
    private StackPane imageViewLayout, noteLayout, quizLayout;

    @FXML
    private AnchorPane noteContentPane;

    @FXML
    private ListView<NoteSection> noteContentList;

    @FXML
    private TilePane quizTilePane;

    @FXML
    private Pane dialogDimmer;

    @FXML
    private ToolBar noteTopBar;

    @FXML
    private ComboBox<Font> fontDropdownList;

    @FXML
    private VBox contentLayout, subtopicsVBox, noteOptions, settingsPane, onHyperlinkClickedOverlay, noteOptionsReportNoteLayout, noteOptionsLayout, noteSettingsLayout, refreshNoteLayout, refreshStatusContent, refreshNoteTextContent, noteOptionsNoteLayout, dictionaryMeaningOverlay, questionBox, quizPane, quizSubmitDialog, quizQuitDialog, quizQuestionPane, exitNotesDialog, quizScorePane;

    @FXML
    private VBox quizExplanationSection, quizBackNextAndQuitButtonsSection, activateNowDialog;

    @FXML
    private HBox highlightColors, highlightColorsBox, addNoteButton, noteSettingsCloseButton, noteOptionsCloseButton, toastLayout, reportOptionsCloseButton, refreshNotesCancelButton, onWordClickedOverlay, dictionaryCloseButton, quizScoreCloseButton;

    @FXML
    private Label pageTitle, subjectLabel, topicLabel, noteTopicLabel, addNoteText, bookmarkText, highlightHeader, refreshStatusFirstText, refreshStatusSecondText, refreshNotesCancelText, toastText, newNoteText, dictionaryText, currentNoteSubject, currentNoteSubjectTopic, quizQuestion;

    @FXML
    private Label fontText, fontSizeText, backgroundText, settingsCloseText, quizYourScoreText, quizScore, quizExplanationText, quizExplanationButton, activateHeaderText, noSubtopicsLabel;

    @FXML
    private ImageView notesImage, note_icon, bookmark_icon, closeIconImageViewLayout, closeIconNoteOptionLayout, imageViewLarge, settingsIcon, searchIcon, noteSettingsIcon, createNoteBackIcon, refreshStatusNoUpdateIcon, refreshIcon, refreshStatusUpdateFoundIcon, refreshStatusNoNetworkIcon;

    @FXML
    private ImageView noteOptionsNoteIcon, noteOptionsBookmarkIcon, noteOptionsShareIcon, noteOptionsReportIcon, noteOptionsAudioIcon, reportOptionReportIcon, notClearIcon, aLittleClearIcon, veryClearIcon, feedbackIcon, dictionarySpeakerIcon, dictionaryIcon;

    @FXML
    private ImageView quizShareIcon, quizBookmarkIcon, quizReportIcon, quizSpeakerIcon, quizScoreBar, activateNowPadlockIcon, activateNowCloseIcon, greenTickIcon1, greenTickIcon2, greenTickIcon3, greenTickIcon4, greenTickIcon5;

    @FXML
    private Button backButton, prevButton, nextButton, practiceTopicButton, quizButton, noteCloseButton, noteSaveButton, quizBackButton, quizNextButton, quizSubmitOrCloseButton, submitDialogSubmitButton, submitDialogCancelButton, quizQuitButton, quitDialogQuitButton, quitDialogCancelButton, exitDialogExitButton, exitDialogCancelButton, notesBackButton, quizScoreQuitButton, quizScoreAnswersButton, activateNowButton;
    @FXML
    private ToggleButton fontSmallButton, fontMediumButton, fontLargeButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Rectangle quizQuestionLayout;

    @FXML
    private ProgressIndicator refreshNoteProgressIndicator;

    @FXML
    private ProgressIndicator progressBar;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0; -fx-cursor: hand;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    private Section selectedSection;

    private int defaultFontSize = 16;

    Parser markdownParser = Parser.builder().build();

    HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProgressBar();
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Task<Boolean> contentTask = new Task<>() {
            @Override
            protected Boolean call() {
                viewModel.initialize(getInitialData());
                return true;
            }
        };
        contentTask.setOnSucceeded(
                event -> Platform.runLater(() -> {
                    subjectLabel.setText(viewModel.getSubject().getTitle());
                    noteTopicLabel.setText(viewModel.getTopic().getTitle());
                    noteTopicLabel.setTextFill(Paint.valueOf(SubjectDao.getNoteSubjectColor(viewModel.getSubject().getId())));

                    ObservableList<NoteSection> noteSections = viewModel.getNoteSections().get(viewModel.getTopic().getId());
                    noteContentList.setCellFactory(new NoteContentListCellFactory());
                    noteContentList.setItems(noteSections);
                    noteContentList.setSelectionModel(new NoSelectionModel<>());
                    noteContentList.setPadding(new Insets(10, 20, 30, 15));

                    if (viewModel.getSelectedSubtopicSection() != null) {
                        noteContentList.scrollTo(viewModel.getSelectedSubtopicSection());
                    }

                    if (viewModel.getSelectedSection() != null) {
                        int lastSectionIndex = noteSections.indexOf(noteSections.stream().filter(
                                section -> section.getId() == viewModel.getSelectedSection().getId()).toList().get(0)
                        );
                        System.out.println(TAG + "Note Last Section Index -> " + lastSectionIndex);
                        noteContentList.scrollTo(lastSectionIndex);
                    }

                    noSubtopicsLabel.setVisible(viewModel.getSubTopics().get(viewModel.getTopic().getId()).isEmpty());

                    ToggleGroup subtopicsListToggleGroup = new ToggleGroup();
                    viewModel.getSubTopics().get(viewModel.getSelectedTopic().getId()).forEach(subTopic -> {
                        ToggleButton button = new ToggleButton();
                        button.setUserData(subTopic);
                        subtopicsListToggleGroup.getToggles().add(button);

                        button.setMinHeight(48);
                        button.setMaxHeight(48);
                        button.setPadding(new Insets(0, 0, 0, 20));
                        button.setAlignment(Pos.BASELINE_LEFT);
                        button.setMaxWidth(Double.MAX_VALUE);
                        button.setText(subTopic.getTitle());
                        if (subTopic.getTitle().length() > 40) {
                            button.setTooltip(new Tooltip(subTopic.getTitle()));
                        }

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

                                viewModel.setSelectedSubTopic(subTopic);
                                noteContentList.scrollTo(viewModel.getNoteSubtopicSection(subTopic));
//                    renderNote(subTopic);
//                    topicLabel.setText(subTopic.getTitle());
                            } else {
                                button.setStyle(IDLE_BUTTON_STYLE);
                                button.setTextFill(Color.BLACK);

                                noteContentList.scrollTo(0);

                                viewModel.setSelectedSubTopic(null);
                            }
                        });

                        if (subTopic == viewModel.getSelectedSubTopic()) {
                            subtopicsListToggleGroup.selectToggle(button);
                        }

                        button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 12));

                        subtopicsVBox.getChildren().add(button);
                    });

                    viewModel.selectedTopicProperty().addListener(((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            ObservableList<NoteSection> topicSections = viewModel.getNoteSections().get(newValue.getId());
                            noteContentList.setItems(topicSections);
                            noteContentList.scrollTo(0);
                            noteTopicLabel.setText(newValue.getTitle());
                            subtopicsListToggleGroup.getToggles().clear();
                            subtopicsVBox.getChildren().clear();

                            noSubtopicsLabel.setVisible(viewModel.getSubTopics().get(newValue.getId()).isEmpty());

                            System.out.println(TAG + "Selected topic Id -> " + newValue.getId());
                            viewModel.getSubTopics().get(newValue.getId()).forEach(subTopic -> {
                                ToggleButton button = new ToggleButton();
                                button.setUserData(subTopic);
                                subtopicsListToggleGroup.getToggles().add(button);

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

                                        viewModel.setSelectedSubTopic(subTopic);
                                        noteContentList.scrollTo(viewModel.getNoteSubtopicSection(subTopic));
                                    } else {
                                        button.setStyle(IDLE_BUTTON_STYLE);
                                        button.setTextFill(Color.BLACK);

                                        noteContentList.scrollTo(0);

                                        viewModel.setSelectedSubTopic(null);
                                    }
                                });

                                if (subTopic == viewModel.getSelectedSubTopic()) {
                                    subtopicsListToggleGroup.selectToggle(button);
                                }

                                button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 12));

                                subtopicsVBox.getChildren().add(button);
                            });
                        }
                    }));



                    if (viewModel.getSelectedTopicIndex() == 0) {
                        prevButton.setDisable(true);
                    }
                    if (viewModel.getSelectedTopicIndex() == viewModel.getNoteTopics().size()-1) {
                        nextButton.setDisable(true);
                    }
                    viewModel.selectedTopicIndexProperty().addListener(((observable, oldValue, newValue) -> {
                        if (newValue < viewModel.getNoteTopics().size()) {
                            viewModel.setSelectedTopic(viewModel.getNoteTopics().get(newValue));
                            if (newValue == viewModel.getNoteTopics().size()-1) {
                                nextButton.setDisable(true);
                            }
                            if (newValue == 0) {
                                prevButton.setDisable(true);
                            }
                        }
                    }));

                    viewModel.selectedTopicProperty().addListener(((observable, oldValue, newValue) -> {
                        if (!newValue.isFree()) {
                            nextButton.setDisable(true);
                            Animations.showDialog(activateNowDialog, dialogDimmer);
                        }
                    }));
                    hideProgressBar();
                })
        );

        executorService.execute(contentTask);
        executorService.shutdown();

        initializeViews();
        initializeTextFont(FontSize.MEDIUM);
        highlightColorsList.addAll(Arrays.stream(HighlightColors.values()).map(highlightColors1 -> highlightColors1.colorCode).toList());

        prevButton.setOnAction(event -> {
            nextButton.setDisable(false);
            viewModel.setSelectedTopicIndex(viewModel.getSelectedTopicIndex() - 1);
        });

        nextButton.setOnAction(event -> {
            prevButton.setDisable(false);
            viewModel.setSelectedTopicIndex(viewModel.getSelectedTopicIndex() + 1);
        });

        activateNowCloseIcon.setOnMouseClicked(event -> {
            viewModel.setSelectedTopicIndex(viewModel.getSelectedTopicIndex() - 1);
            Animations.hideDialog(activateNowDialog, dialogDimmer);
        });

        activateNowButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

        notesBackButton.setOnAction(event -> {
            Animations.fadeIn(dialogDimmer, 300, 0.0, 0.5);

            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm", null, "Are you sure you want to exit?");
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    if (noteContentList.getSkin() != null) {
                        VirtualFlow<?> vf = (VirtualFlow<?>) ((ListViewSkin<?>) noteContentList.getSkin()).getChildren().get(0);

                        double lastSectionIndex = vf.getPosition() * vf.getCellCount();
                        System.out.println(TAG + "Last Section Index -> " + lastSectionIndex);

                        if ((int)lastSectionIndex == noteContentList.getItems().size()) {
                            lastSectionIndex = (int) lastSectionIndex - 1;
                        }

                        NoteSection lastSection = noteContentList.getItems().get((int) lastSectionIndex);

                        StringBuilder sectionTitle = new StringBuilder();
                        ContentViewType contentViewType = ContentViewTypes.convert(lastSection);
                        if (contentViewType instanceof HeaderViewType headerViewType) {
                            if (headerViewType.getText() != null) {
                                sectionTitle = new StringBuilder(headerViewType.getText());
                            }
                        } else if (contentViewType instanceof ParagraphViewType paragraphViewType) {
                            if (paragraphViewType.getText() != null) {
                                sectionTitle = new StringBuilder(paragraphViewType.getText());
                            }
                        } else if (contentViewType instanceof CBTViewType cbtViewType) {
                            int yearId = cbtViewType.getYearId();
                            int questionId = cbtViewType.getQuestionId();
                            int subjectId = cbtViewType.getSubjectId();
                            ObjectiveQuestion question = viewModel.getQuestion(subjectId, yearId, questionId);

                            sectionTitle = new StringBuilder(question.getQuestion());
                        } else if (contentViewType instanceof LatexMathViewType latexMathViewType) {
                            if (latexMathViewType.getKatex() != null) {
                                sectionTitle = new StringBuilder(latexMathViewType.getKatex());
                            }
                        } else if (contentViewType instanceof ListViewType listViewType) {
                            sectionTitle = new StringBuilder(listViewType.getItems().get(0));
                        } else if (contentViewType instanceof ReferenceViewType referenceViewType) {
                            sectionTitle = new StringBuilder(referenceViewType.getText());
                        } else if (contentViewType instanceof TableViewType tableViewType) {

                            List<List<String>> content = tableViewType.getContent();

                            for (int row = 0; row < 1; row++) {

                                System.out.println("Row Content -> " + content.get(row));
                                for (int col = 0; col < content.get(row).size(); col++) {
                                    System.out.println("Column content -> " + content.get(row).get(col));
                                    sectionTitle.append(" | ").append(content.get(row).get(col));
                                }
                            }
                        }
                        System.out.println(TAG + "Got Section title -> " + sectionTitle);


                        NoteLastSession noteLastSession = new NoteLastSession(
                                viewModel.getUser().getId().hashCode(),
                                lastSection.getId(),
                                sectionTitle.toString(),
                                viewModel.getUser().getId()
                        );

                        viewModel.putLastSession(noteLastSession);
                    }

                    ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);

                } else {
                    Animations.fadeOut(dialogDimmer, 300, 0.5, 0.0);
                }
                return buttonType;
            });
            dialog.show();
        });

        /*notesBackButton.setOnAction(event -> {
            Animations.translateIn(exitNotesDialog, 300);
            Animations.fadeIn(dialogDimmer, 300, 0.0, 0.5);
        });
        exitDialogExitButton.setOnAction(event -> {
            if (noteContentList.getSkin() != null) {
                VirtualFlow<?> vf = (VirtualFlow<?>) ((ListViewSkin<?>) noteContentList.getSkin()).getChildren().get(0);

//                System.out.println(TAG + "Position -> " + vf.getPosition());
//                System.out.println(TAG + "Cell count -> " + vf.getCellCount());

                double lastSectionIndex = vf.getPosition() * vf.getCellCount();
                System.out.println(TAG + "Last Section Index -> " + lastSectionIndex);

                if ((int)lastSectionIndex == noteContentList.getItems().size()) {
                    lastSectionIndex = (int) lastSectionIndex - 1;
                }

                NoteSection lastSection = noteContentList.getItems().get((int) lastSectionIndex);

                StringBuilder sectionTitle = new StringBuilder();
                ContentViewType contentViewType = ContentViewTypes.convert(lastSection);
                if (contentViewType instanceof HeaderViewType headerViewType) {
                    if (headerViewType.getText() != null) {
                        sectionTitle = new StringBuilder(headerViewType.getText());
                    }
                } else if (contentViewType instanceof ParagraphViewType paragraphViewType) {
                    if (paragraphViewType.getText() != null) {
                        sectionTitle = new StringBuilder(paragraphViewType.getText());
                    }
                } else if (contentViewType instanceof CBTViewType cbtViewType) {
                    int yearId = cbtViewType.getYearId();
                    int questionId = cbtViewType.getQuestionId();
                    ObjectiveQuestion question = viewModel.getQuestion(yearId, questionId);

                    sectionTitle = new StringBuilder(question.getQuestion());
                } else if (contentViewType instanceof LatexMathViewType latexMathViewType) {
                    if (latexMathViewType.getKatex() != null) {
                        sectionTitle = new StringBuilder(latexMathViewType.getKatex());
                    }
                } else if (contentViewType instanceof ListViewType listViewType) {
                    sectionTitle = new StringBuilder(listViewType.getItems().get(0));
                } else if (contentViewType instanceof ReferenceViewType referenceViewType) {
                    sectionTitle = new StringBuilder(referenceViewType.getText());
                } else if (contentViewType instanceof TableViewType tableViewType) {

                    List<List<String>> content = tableViewType.getContent();

                    for (int row = 0; row < 1; row++) {

                        System.out.println("Row Content -> " + content.get(row));
                        for (int col = 0; col < content.get(row).size(); col++) {
                            System.out.println("Column content -> " + content.get(row).get(col));
                            sectionTitle.append(" | ").append(content.get(row).get(col));
                        }
                    }
                }
                System.out.println(TAG + "Got Section title -> " + sectionTitle);


                NoteLastSession noteLastSession = new NoteLastSession(
                        viewModel.getUser().getId().hashCode(),
                        lastSection.getId(),
                        sectionTitle.toString(),
                        viewModel.getUser().getId()
                );

                viewModel.putLastSession(noteLastSession);
            }

            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });

        exitDialogCancelButton.setOnAction(event -> {
            Animations.translateOut(exitNotesDialog, 300);
            Animations.fadeOut(dialogDimmer, 300, 0.5, 0.0);
        });*/


        /***************** Refresh Notes Section *******************/
//        refreshIcon.setImage(new Image(getClass().getResource("/drawable/note_refresh_icon_1.5x.png").toString()));
//        refreshIcon.setOnMouseClicked((event -> {
//            if (!refreshNoteLayout.isVisible()) {
//                refreshStatusContent.getChildren().remove(refreshNoteProgressIndicator);
//                refreshStatusContent.getChildren().remove(refreshStatusNoUpdateIcon);
//                refreshStatusContent.getChildren().remove(refreshStatusUpdateFoundIcon);
//                refreshStatusFirstText.setText("Network Unavailable");
//                refreshStatusFirstText.setTextFill(Paint.valueOf("#EE8989"));
//                refreshStatusFirstText.setPadding(new Insets(10, 0, 0, 0));
//                refreshStatusSecondText.setText("Connect your device and try again");
//                //refreshStatusSecondText.setTextFill(Paint.valueOf("#51C46B"));
//                //refreshNoteTextContent.getChildren().remove(refreshStatusSecondText);
//                Animations.translateIn(refreshNoteLayout, 200);
//            }
//
//        }));
        refreshNotesCancelButton.setOnMouseEntered(event -> {
            refreshNotesCancelButton.setStyle(HOVERED_BUTTON_STYLE);
        });
        refreshNotesCancelButton.setOnMouseExited(event -> {
            refreshNotesCancelButton.setStyle(IDLE_BUTTON_STYLE);
        });
        refreshNotesCancelButton.setOnMouseClicked(event -> {
            Animations.translateOut(refreshNoteLayout, 300);
        });


        /**************** Note Settings Section ***************/
//        noteSettingsIcon.setImage(new Image(getClass().getResource("/drawable/note_settings_icon_2x.png").toString()));
        refreshStatusNoUpdateIcon.setImage(new Image(getClass().getResource("/drawable/no_update_found_icon_1x.png").toString()));
        refreshStatusUpdateFoundIcon.setImage(new Image(getClass().getResource("/drawable/updates_found_icon_1x.png").toString()));
        refreshStatusNoNetworkIcon.setImage(new Image(getClass().getResource("/drawable/no_network_icon_1x.png").toString()));
//        noteSettingsIcon.setOnMouseClicked(event -> {
//            if (!noteSettingsLayout.isVisible()) {
//                Animations.translateIn(noteSettingsLayout, 200);
//            }
//
//            int stackItems = noteLayout.getChildren().size();
//            System.out.println("StackPane Items -> " + stackItems);
//
//        });
        fontDropdownList.setItems(FXCollections.observableArrayList(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size)));
        fontDropdownList.valueProperty().addListener((observer, oldValue, newValue) -> {
            fontDropdownList.getSelectionModel().select(newValue);
            System.out.println("Selected font -> " + newValue.getName());
        });

        fontDropdownList.setConverter(new StringConverter<>() {
            @Override
            public String toString(Font font) {
                if (font.getName().contains("Gilroy")) {
                    return "Gilroy";
                }
                return font.getFamily();
            }

            @Override
            public Font fromString(String string) {
                return null;
            }
        });

        ToggleGroup fontSizeToggleGroup = new ToggleGroup();
        fontSizeToggleGroup.getToggles().addAll(fontSmallButton, fontMediumButton, fontLargeButton);
        fontSizeToggleGroup.selectedToggleProperty().addListener((observer, oldValue, newValue) -> {
            handleFontSizeChanged(newValue);
        });

        noteSettingsCloseButton.setOnMouseEntered(event -> {
            noteSettingsCloseButton.setStyle(HOVERED_BUTTON_STYLE);
        });
        noteSettingsCloseButton.setOnMouseExited(event -> {
            noteSettingsCloseButton.setStyle(IDLE_BUTTON_STYLE);
        });
        noteSettingsCloseButton.setOnMouseClicked(event -> {
            Animations.translateOut(noteSettingsLayout, 300);
        });


        /******************** Note Options Section ************************/
        noteOptionsNoteIcon.setImage(new Image(getClass().getResource("/drawable/note_options_note_icon_1x.png").toString()));
        noteOptionsBookmarkIcon.setImage(new Image(getClass().getResource("/drawable/note_options_bookmark_icon_1x.png").toString()));
        noteOptionsShareIcon.setImage(new Image(getClass().getResource("/drawable/note_options_share_icon_1x.png").toString()));
        noteOptionsReportIcon.setImage(new Image(getClass().getResource("/drawable/note_options_report_icon_1x.png").toString()));
        noteOptionsAudioIcon.setImage(new Image(getClass().getResource("/drawable/note_options_audio_icon_1x.png").toString()));
        createNoteBackIcon.setImage(new Image(getClass().getResource("/drawable/create_note_back_icon_1x.png").toString()));


        noteOptionsNoteIcon.setOnMouseClicked(event -> {
            noteOptionsNoteLayout.setVisible(true);
//            Dialog<String> noteDialog = createNoteDialog(null);
//
//            Optional<String> result = noteDialog.showAndWait();
        });
        createNoteBackIcon.setOnMouseClicked(event -> {
            noteOptionsNoteLayout.setVisible(false);
        });
        noteCloseButton.setOnMouseClicked(event -> {
            noteOptionsLayout.setVisible(false);
            noteOptionsNoteLayout.setVisible(false);

        });
        noteSaveButton.setOnMouseClicked(event -> {
            toastText.setText("Note Saved");
            Animations.fadeInAndOut(toastLayout);
        });
        noteOptionsBookmarkIcon.setOnMouseClicked(event -> {
            toastText.setText("Bookmarked Successfully");
            Animations.fadeInAndOut(toastLayout);
        });

        noteOptionsCloseButton.setOnMouseEntered(event -> {
            noteOptionsCloseButton.setStyle(HOVERED_BUTTON_STYLE);
        });
        noteOptionsCloseButton.setOnMouseExited(event -> {
            noteOptionsCloseButton.setStyle(IDLE_BUTTON_STYLE);
        });
        noteOptionsCloseButton.setOnMouseClicked(event -> {
            Animations.translateOut(noteOptionsLayout, 300);
        });


        /********** Note Options Report Layout Section **********/
        reportOptionReportIcon.setImage(new Image(getClass().getResource("/drawable/note_options_report_icon_1x.png").toString()));
        notClearIcon.setImage(new Image(getClass().getResource("/drawable/not_clear_emoji_1x.png").toString()));
        aLittleClearIcon.setImage(new Image(getClass().getResource("/drawable/a_little_clear_emoji_1x.png").toString()));
        veryClearIcon.setImage(new Image(getClass().getResource("/drawable/very_clear_emoji_1x.png").toString()));
        feedbackIcon.setImage(new Image(getClass().getResource("/drawable/feedback_emoji_1x.png").toString()));

        noteOptionsReportIcon.setOnMouseClicked(event -> {
            noteOptionsReportNoteLayout.setVisible(true);
        });
        reportOptionsCloseButton.setOnMouseEntered(event -> {
            reportOptionsCloseButton.setStyle(HOVERED_BUTTON_STYLE);
        });
        reportOptionsCloseButton.setOnMouseExited(event -> {
            reportOptionsCloseButton.setStyle(IDLE_BUTTON_STYLE);
        });
        reportOptionsCloseButton.setOnMouseClicked(event -> {
            noteOptionsReportNoteLayout.setVisible(false);
        });


        /****************** Note Hyperlink Section *******************/
        dictionaryText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, 16));
        dictionaryIcon.setImage(new Image(getClass().getResource("/drawable/dictionary_icon_1x.png").toString()));
        dictionarySpeakerIcon.setImage(new Image(getClass().getResource("/drawable/dictionary_speaker_icon_2x.png").toString()));
        dictionaryIcon.setOnMouseClicked(event -> {
            onWordClickedOverlay.setVisible(false);
           // dictionaryMeaningOverlay.setVisible(true);
            Animations.fadeIn(dictionaryMeaningOverlay, 100);
        });
        dictionaryCloseButton.setOnMouseEntered(event -> {
            dictionaryCloseButton.setStyle(HOVERED_BUTTON_STYLE);
        });
        dictionaryCloseButton.setOnMouseExited(event -> {
            dictionaryCloseButton.setStyle(IDLE_BUTTON_STYLE);
        });
        dictionaryCloseButton.setOnMouseClicked(event -> {
            Animations.fadeOut(dictionaryMeaningOverlay, 300);
        });

//        contentLayout.setOnMouseClicked(event -> {
//            /*if (!onWordClickedOverlay.isVisible() && !dictionaryMeaningOverlay.isVisible()) {
//                onWordClickedOverlay.setVisible(true);
//            }*/
//            if (!noteOptionsLayout.isVisible()) {
//                Animations.translateIn(noteOptionsLayout, 300);
//            }
//        });


        /******************* Note Quiz Section *******************/
        //quizLayout.getChildren().remove(quizLayout.getChildren().get(0));
        //quizLayout.getChildren().remove(quizScorePane);
//        quizYourScoreText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.THIRTY.size));
//        currentNoteSubject.setText(currentNoteSubject.getText().toUpperCase());

//        quizShareIcon.setImage(new Image(getClass().getResource("/drawable/quiz_share_icon_1x.png").toString()));
//        quizBookmarkIcon.setImage(new Image(getClass().getResource("/drawable/quiz_bookmark_icon_1x.png").toString()));
//        quizReportIcon.setImage(new Image(getClass().getResource("/drawable/quiz_report_icon_1x.png").toString()));
//        quizSpeakerIcon.setImage(new Image(getClass().getResource("/drawable/quiz_speaker_icon_1x.png").toString()));
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/quiz_back_button_icon_1x.png").toString()));
        ImageView nextIcon = new ImageView(new Image(getClass().getResource("/drawable/quiz_next_button_icon_1x.png").toString()));
        backIcon.setFitHeight(12);
        backIcon.setFitWidth(12);
        backIcon.setPreserveRatio(true);
        backIcon.setPickOnBounds(true);
//        quizBackButton.setGraphicTextGap(10);
//        quizBackButton.setGraphic(backIcon);
//        quizBackButton.setOnMouseClicked(event -> {
//            if (quizQuestionPane.isVisible()) {
//                Animations.slideOut(quizQuestionPane);
//            }
//        });

        nextIcon.setFitHeight(12);
        nextIcon.setFitWidth(12);
        nextIcon.setPreserveRatio(true);
        nextIcon.setPickOnBounds(true);
//        quizNextButton.setGraphic(nextIcon);
//        quizNextButton.setGraphicTextGap(10);
//        quizNextButton.setOnMouseClicked(event -> {
//            if (quizQuestionPane.isVisible()) {
//                Animations.slideIn(quizQuestionPane);
//            }
//        });
//        quizButton.setOnAction(event -> {
//            quizSubmitOrCloseButton.setText("Submit");
//            quizSubmitOrCloseButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.TWELVE.size));
//            quizSubmitOrCloseButton.setTextFill(Paint.valueOf("#FFFFFF"));
//            quizSubmitOrCloseButton.setStyle("-fx-background-color: #51C46B; -fx-background-radius: 4");
//            quizExplanationSection.getChildren().remove(quizExplanationText);
//            quizExplanationButton.setVisible(false);
//            Animations.slideIn(quizPane);
//            if (!quizPane.isVisible()) {
//                Animations.slideIn(quizPane);
//            }
//        });
//        quizSubmitOrCloseButton.setOnAction(event -> {
//            if (quizSubmitOrCloseButton.getText().contains("Submit")) {
//                Animations.fadeIn(quizSubmitDialog, 100);
//            }else {
//                Animations.fadeIn(quizQuitDialog, 100);
//                if (!quizBackNextAndQuitButtonsSection.getChildren().contains(quizQuitButton)) {
//                    quizBackNextAndQuitButtonsSection.getChildren().add(quizQuitButton);
//                }
//            }
//            Animations.fadeIn(dialogDimmer, 300, 0.0, 0.5);
//
//        });
        submitDialogCancelButton.setOnMouseClicked(event -> {
            Animations.fadeOut(quizSubmitDialog, 300);
            Animations.fadeOut(dialogDimmer, 300, 0.5, 0.0);
        });
        submitDialogSubmitButton.setOnAction(event -> {
//            Animations.slideIn(quizScorePane);
            Animations.fadeOut(quizSubmitDialog, 300);
            Animations.fadeOut(dialogDimmer, 300, 0.5, 0.0);
//            quizPane.setVisible(false);
            showQuizScore();
        });
//        quizQuitButton.setOnMouseClicked(event -> {
//            Animations.fadeIn(quizQuitDialog, 100);
//            Animations.fadeIn(dialogDimmer, 300, 0.0, 0.5);
//        });
        quitDialogQuitButton.setOnMouseClicked(event -> {
            Animations.fadeOut(quizQuitDialog, 300);
            Animations.fadeOut(dialogDimmer, 300, 0.5, 0.0);
//            Animations.slideOut(quizPane);
        });
        quitDialogCancelButton.setOnMouseClicked(event -> {
            Animations.fadeOut(quizQuitDialog, 300);
            Animations.fadeOut(dialogDimmer, 300, 0.5, 0.0);
        });
        setupQuizTilePane();
        setupQuizQuestion();


        highlightColors.getChildren().clear();
        highlightColors.getChildren().addAll(
                highlightColorsList
                        .stream()
                        .map(colorCode -> {
                            Circle circle = new Circle();

                            circle.setFill(Color.web(colorCode));

                            circle.setRadius(15);

                            circle.setOnMouseClicked(event -> {
//                                viewModel.handleHighlight(selectedSection, colorCode);
                                hideNoteOptions();
                            });

                            return circle;
                        }).toList()
        );


        viewModel.getSubjectHighlights().addListener((ListChangeListener<? super Highlights>) c -> {
            System.out.println("Highlight list changed");
//            renderNote(viewModel.getSelectedSubTopic());
        });

        notesImage.setImage(new Image(getClass().getResource("/drawable/notes.png").toString()));
        note_icon.setImage(new Image(getClass().getResource("/drawable/note_icon.png").toString()));
        bookmark_icon.setImage(new Image(getClass().getResource("/drawable/bookmark_stroke.png").toString()));

        ImageView view = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        view.setFitHeight(25);
        view.setPreserveRatio(true);

        //settingsIcon.setImage(new Image(getClass().getResource("/drawable/preferences.png").toString()));
        /*settingsIcon.setOnMouseClicked(event -> {
            Dialog<Double> dialog = createSettingsDialog(defaultFontSize);
            Optional<Double> result = dialog.showAndWait();

            if (result.isPresent()) {
                defaultFontSize = result.get().intValue();
                renderNote(viewModel.getSelectedTopic());
            }

        });*/

        /*searchTextField.setOnMouseClicked(event -> {
            searchIcon.setVisible(false);
        });*/

//        closeIconImageViewLayout.setImage(new Image(getClass().getResource("/drawable/close_icon_white.png").toString()));
//        closeIconImageViewLayout.setOnMouseClicked(event -> {
//            hideImageViewLayout();
//        });

        closeIconNoteOptionLayout.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        closeIconNoteOptionLayout.setOnMouseClicked(event -> {
            hideNoteOptions();
        });

//        backButton.setGraphic(view);


//        backButton.setBackground(Background.EMPTY);

        /*backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });*/

        addNoteButton.setOnMouseClicked(event -> {
            List<Note> notes = viewModel.getSubjectNotes();

            String currentNote = null;

            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getNoteId() == selectedSection.getId()) {
                    currentNote = notes.get(i).getNote();
                    break;
                }
            }

            Dialog<String> noteDialog = createNoteDialog(currentNote);

            Optional<String> result = noteDialog.showAndWait();

            result.ifPresent(note -> {
//                viewModel.addNote(selectedSection, note);
            });

            hideNoteOptions();
        });
        MainApplication.timeTakenTo("load note screen");
    }

    private void initializeViews() {
        ImageView noteBackIcon = new ImageView(new Image(getClass().getResource("/drawable/notes_back_icon_2x.png").toString()));
        noteBackIcon.setFitWidth(20);
        noteBackIcon.setFitHeight(20);
        noteBackIcon.setPreserveRatio(true);
        noteBackIcon.setPickOnBounds(true);
//        searchIcon.setImage(new Image(getClass().getResource("/drawable/note_search_icon_1.5x.png").toString()));

        notesBackButton.setBackground(Background.EMPTY);
        notesBackButton.setGraphic(noteBackIcon);

        ImageView nextView = new ImageView(new Image(getClass().getResource("/drawable/next_icon.png").toString()));
        nextView.setFitHeight(40);
        nextView.setPreserveRatio(true);

        nextButton.setGraphic(nextView);
        nextButton.setShape(new Circle(60.0));
        nextButton.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.rgb(0, 0, 0, 0.25), 0.6, 0.5, 0.0, 5.0));
        nextButton.setBackground(Background.EMPTY);

        ImageView prevView = new ImageView(new Image(getClass().getResource("/drawable/prev_icon.png").toString()));
        prevView.setFitHeight(40);
        prevView.setPreserveRatio(true);

        prevButton.setGraphic(prevView);
        prevButton.setShape(new Circle(60.0));
        prevButton.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.rgb(0, 0, 0, 0.25), 0.6, 0.5, 0.0, 5.0));
        prevButton.setBackground(Background.EMPTY);

        activateHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
        noSubtopicsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));

        activateNowCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        activateNowPadlockIcon.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/padlock_icon.png").toString()));
        greenTickIcon1.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon2.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon3.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon4.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon5.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));

//        imageViewLayout.setVisible(false);
//        imageViewLayout.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.web("#000000", 0.8), null, null)}, null));
    }

    private void initializeTextFont(FontSize size) {
        switch (size) {
            case SMALL -> setFontSizesToSmall();
            case MEDIUM -> setFontSizesToMedium();
            case LARGE -> setFontSizesToLarge();
        }

        /*pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        topicLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        topicTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 16));
        addNoteText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        bookmarkText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        highlightHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 14));
        quizButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        newNoteText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));

        practiceTopicButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));*/
    }

    private void setFontSizesToSmall() {
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.SIXTEEN.size));
//        quizButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.TWELVE.size));
        noteTopicLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        // Quiz Section Texts
//        currentNoteSubject.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
//        currentNoteSubjectTopic.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
//        quizQuestion.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
        //Note Update Texts
        refreshStatusFirstText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
        refreshStatusSecondText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.TWELVE.size));
        refreshNotesCancelText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
        // Note Settings Texts
        fontText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.TWELVE.size));
        fontSizeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.TWELVE.size));
        backgroundText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.TWELVE.size));
        settingsCloseText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
    }
    private void setFontSizesToMedium() {
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 20));
//        quizButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
        noteTopicLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
        // Quiz Section Texts
//        currentNoteSubject.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
//        currentNoteSubjectTopic.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
//        quizQuestion.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
        //Note Update Texts
        refreshStatusFirstText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
        refreshStatusSecondText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
        refreshNotesCancelText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
        // Note Settings Texts
        fontText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.FOURTEEN.size));
        fontSizeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
        backgroundText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
        settingsCloseText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
    }
    private void setFontSizesToLarge() {
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.TWENTY.size));
//        quizButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
        noteTopicLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.EIGHTEEN.size));
        // Quiz Section Texts
//        currentNoteSubject.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.EIGHTEEN.size));
//        currentNoteSubjectTopic.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.EIGHTEEN.size));
//        quizQuestion.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.EIGHTEEN.size));
        //Note Update Texts
        refreshStatusFirstText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.EIGHTEEN.size));
        refreshStatusSecondText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
        refreshNotesCancelText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.EIGHTEEN.size));
        // Note Settings Texts
        fontText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.SIXTEEN.size));
        fontSizeText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
        backgroundText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.SIXTEEN.size));
        settingsCloseText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.EIGHTEEN.size));
    }

    private void setupQuizTilePane() {
//        quizTilePane.getChildren().clear();
//
//        quizTilePane.setHgap(10);
//        quizTilePane.setVgap(10);

        for (int i = 1; i <= 30; i++) {
            Circle circle = new Circle(15);
            circle.setFill(Color.web("#FFFFFF"));
            circle.setStroke(Color.web("#EDEDED"));

            Label questionNumber = new Label(Integer.toString(i));

            StackPane s = new StackPane();
            Circle dot = new Circle(1);
            dot.setFill(Paint.valueOf("#000000"));
            StackPane.setAlignment(dot, Pos.BOTTOM_CENTER);
            StackPane.setMargin(dot, new Insets(10, 0, 0, 0));
            s.getChildren().addAll(circle, questionNumber, dot);

//            quizTilePane.getChildren().add(s);
        }
    }

    private void setupQuizQuestion() {
//        quizQuestion.setText("This zygote undergoes meiosis to form spores. Each spore develops into a new organism.");
    }

    private void showQuizScore() {
        quizScoreBar.setImage(new Image(getClass().getResource("/drawable/quiz_score_progress_icon_1x.png").toString()));
        quizScore.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.THIRTY_FOUR.size));

        quizScoreQuitButton.setOnAction(event -> {
            Animations.fadeIn(quizQuitDialog, 100);
            Animations.fadeIn(dialogDimmer, 300, 0.0, 0.5);

            quitDialogQuitButton.setOnAction(event1 -> {
//                Animations.slideOut(quizScorePane);
            });
            //TODO: Slide out Quiz Pane
        });

        quizScoreAnswersButton.setOnAction(event -> {
            showQuizAnswers();
        });

        quizScoreCloseButton.setOnMouseEntered(event -> {
            quizScoreCloseButton.setStyle(HOVERED_BUTTON_STYLE);
        });
        quizScoreCloseButton.setOnMouseExited(event -> {
            quizScoreCloseButton.setStyle(IDLE_BUTTON_STYLE);
        });
        quizScoreCloseButton.setOnMouseClicked(event -> {
            //TODO: Close Quiz Score Screen
        });
    }

    private void showQuizAnswers() {
//        quizSubmitOrCloseButton.setText("Close");
//        quizSubmitOrCloseButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
//        quizSubmitOrCloseButton.setTextFill(Paint.valueOf("#EE8989"));
//        quizSubmitOrCloseButton.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 4");
//        quizExplanationButton.setVisible(true);
//        quizExplanationSection.getChildren().add(quizExplanationText);
//        quizExplanationText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.TWELVE.size));
//        quizBackNextAndQuitButtonsSection.getChildren().remove(quizQuitButton);

//        Animations.slideIn(quizPane);
//        quizScorePane.setVisible(false);
    }

    private ScrollBar getVerticalScrollBar(ListView<?> listView) {
        for (Node node : listView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation() == Orientation.VERTICAL) {
                    return scrollBar;
                }
            }
        }
        return null;
    }

    private int getFirstVisibleIndex(Skin<?> skin) {
        if (skin instanceof ListViewSkin) {
            VirtualFlow<?> vf = (VirtualFlow<?>) ((ListViewSkin<?>) skin).getChildren().get(0);
            System.out.println(TAG + "Got VirtualFlow -> " + vf);
            System.out.println(TAG + "Position -> " + vf.getPosition());
            System.out.println(TAG + "Cell count -> " + vf.getCellCount());
//            System.out.println(TAG + "First Visible Cell -> " + vf.getLastVisibleCell().getIndex());
//            return vf.getFirstVisibleCell().getIndex();
        }
        return -1;
    }

    public void getFirstAndLast(ListView<?> t) {
        try {
            ListViewSkin<?> ts = (ListViewSkin<?>) t.getSkin();
            VirtualFlow<?> vf = (VirtualFlow<?>) ts.getChildren().get(0);
            int first = vf.getFirstVisibleCell().getIndex();
            int last = vf.getLastVisibleCell().getIndex();
            System.out.println(TAG + "##### Scrolling first " + first + " last " + last);
        } catch (Exception ex) {
            System.out.println(TAG + "##### Scrolling: Exception " + ex);
        }
    }

    /**
     * Utility method to retrieve a key from a Map using its value
     * @param map the default Map
     * @param value the value which you want to return its key
     * @return the key that was looked up for using its value in the Map
     */
    public String getKey(Map<String, List<String>> map, List<String> value) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private Label getTitle(String text) {
        Label title = new Label();
        title.setText(text);
        title.setWrapText(true);
        title.setUnderline(true);
        title.setLineSpacing(8);
        title.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
        return title;
    }

    private void showNoteOptions(Section section) {
        selectedSection = section;

        noteOptions.setVisible(true);
        TranslateTransition slideUp = new TranslateTransition();

        slideUp.setFromY(200f);
        slideUp.setToY(0f);
        slideUp.setDuration(Duration.millis(150));
        slideUp.setNode(noteOptions);

        slideUp.play();
    }

    private void handleFontSizeChanged(Toggle newValue) {
        if (newValue == fontSmallButton){
            setFontSizesToSmall();

            fontSmallButton.setStyle("-fx-background-color: #4BB036; -fx-background-radius: 5 0 0 5; -fx-border-radius: 5 0 0 5; -fx-text-fill: #FFFFFF;");
            fontMediumButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B8B8B8; -fx-border-radius: 0 0 0 0; -fx-text-fill: #000000;");
            fontLargeButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B8B8B8; -fx-border-radius: 0 5 5 0; -fx-text-fill: #000000;");
        } else if (newValue == fontMediumButton) {
            setFontSizesToMedium();

            fontSmallButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B8B8B8; -fx-border-radius: 5 0 0 5; -fx-text-fill: #000000;");
            fontMediumButton.setStyle("-fx-background-color: #4BB036; -fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0; -fx-text-fill: #FFFFFF;");
            fontLargeButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B8B8B8; -fx-border-radius: 0 5 5 0; -fx-text-fill: #000000;");
        } else if (newValue == fontLargeButton) {
            setFontSizesToLarge();

            fontSmallButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B8B8B8; -fx-border-radius: 5 0 0 5; -fx-text-fill: #000000;");
            fontMediumButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B8B8B8; -fx-border-radius: 0 0 0 0; -fx-text-fill: #000000;");
            fontLargeButton.setStyle("-fx-background-color: #4BB036; -fx-background-radius: 0 5 5 0; -fx-border-radius: 0 5 5 0; -fx-text-fill: #FFFFFF;");
        }
    }

    private void showImageViewLayout(Image image) {

//        imageViewLayout.setVisible(true);
//        imageViewLarge.setImage(image);

        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        fadeTransition.setDuration(Duration.millis(300));

//        fadeTransition.setNode(imageViewLayout);

        fadeTransition.play();
    }

    private void hideImageViewLayout() {

        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setDuration(Duration.millis(300));

//        fadeTransition.setNode(imageViewLayout);
//
//        fadeTransition.setOnFinished(event -> {
//            imageViewLayout.setVisible(false);
//        });
        fadeTransition.play();
    }

    private void hideNoteOptions() {
        selectedSection = null;

        TranslateTransition slideUp = new TranslateTransition();

        slideUp.setFromY(0f);
        slideUp.setToY(200f);
        slideUp.setDuration(Duration.millis(150));
        slideUp.setNode(noteOptions);

        slideUp.setOnFinished(event -> {
            noteOptions.setVisible(false);
        });

        slideUp.play();
    }

    private Dialog<Double> createSettingsDialog(int defaultFontSize) {
        Dialog<Double> dialog = new Dialog<>();
        dialog.setTitle("Note Settings");

        dialog.setHeaderText("Modify font size");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label font = new Label();
        font.setText("Aa");
        font.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, defaultFontSize));

        Slider slider = new Slider();
        slider.setMax(25);
        slider.setMin(8);

        slider.setValue(defaultFontSize);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int size = newValue.intValue();

            font.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, size));
        });

        VBox box = new VBox();
        box.setSpacing(20);
        box.setPadding(new Insets(50, 10, 50, 10));
        box.setAlignment(Pos.CENTER);

        box.getChildren().addAll(font, slider);

        dialog.getDialogPane().setContent(box);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return slider.getValue();
            }
            return null;
        });

        return dialog;
    }

    private Dialog<String> createNoteDialog(String note) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Note");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/drawable/app_logo.png").toString()));

        if (note == null) {
            dialog.setHeaderText("Add new note");
        } else {
            dialog.setHeaderText("Edit note");
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextArea noteArea = new TextArea();

        VBox box = new VBox();

        box.getChildren().add(noteArea);

        dialog.getDialogPane().setContent(box);

        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(
                Bindings.createBooleanBinding(() -> noteArea.getText().trim().isEmpty(), noteArea.textProperty()));


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return noteArea.getText();
            }
            return null;
        });

        if (note != null) {
            noteArea.setText(note);
        }

        return dialog;
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        noteContentPane.setVisible(true);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        noteContentPane.setVisible(false);
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
//        System.out.println(TAG + "Got data -> " + Helper.toString(data));
        return data;
    }

    public static class InitialData {
        private NoteSubject subject;
        private List<NoteTopic> topics;
        private NoteTopic selectedNoteTopic;
        private List<NoteSubTopic> subTopics;
        private NoteSubTopic selectedSubTopic;
        private NoteSection selectedSection;

        public InitialData(NoteSubject subject, List<NoteTopic> topics, NoteTopic selectedNoteTopic, List<NoteSubTopic> subTopics, NoteSubTopic selectedSubTopic, NoteSection selectedSection) {
            this.subject = subject;
            this.topics = topics;
            this.selectedNoteTopic = selectedNoteTopic;
            this.subTopics = subTopics;
            this.selectedSubTopic = selectedSubTopic;
            this.selectedSection = selectedSection;
        }

        public NoteSubject getSubject() {
            return subject;
        }

        public List<NoteTopic> getTopics() {
            return topics;
        }

        public NoteTopic getSelectedNoteTopic() {
            return selectedNoteTopic;
        }

        public List<NoteSubTopic> getSubTopics() {
            return subTopics;
        }

        public NoteSubTopic getSelectedSubTopic() {
            return selectedSubTopic;
        }

        public NoteSection getSelectedSection() {
            return selectedSection;
        }
    }
}
