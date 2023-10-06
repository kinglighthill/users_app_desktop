package com.scholarly.utme.controller.note_screens;


import com.scholarly.utme.controller.landing_screens.LandingScreenController;
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
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

    private List<String> highlightColorsList = new ArrayList<>();

    @InjectViewModel
    private NotesScreenVM viewModel;

    @FXML
    private StackPane imageViewLayout, noteLayout, quizLayout;

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

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    private Section selectedSection;

    private int defaultFontSize = 16;

    Parser markdownParser = Parser.builder().build();

    HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        String latex = "\\\\begin{array}{l}";
//        LaTeXConverter converter = new LaTeXConverter();
//        try {
//            File output = converter.convertToImage(latex);
//            System.out.println(TAG + "Formatted Latex -> " + output.get);
//        } catch (Exception e) {
//            System.out.println(TAG + "Cannot parse Latex");
//        }

        viewModel.initialize(getInitialData());

        initializeViews();
        initializeTextFont(FontSize.MEDIUM);
        highlightColorsList.addAll(Arrays.stream(HighlightColors.values()).map(highlightColors1 -> highlightColors1.colorCode).toList());

        subjectLabel.setText(viewModel.getSubject().getTitle());
        noteTopicLabel.setText(viewModel.getTopic().getTitle());

        ObservableList<NoteSection> noteSections = viewModel.getNoteSections().get(viewModel.getTopic().getId());
        noteContentList.setCellFactory(new NoteContentListCellFactory());
        noteContentList.setItems(noteSections);
        noteContentList.setSelectionModel(new NoSelectionModel<>());
        noteContentList.setPadding(new Insets(10, 20, 10, 15));

        if (viewModel.getSelectedSubtopicSection() != null) {
            noteContentList.scrollTo(viewModel.getSelectedSubtopicSection());
        }

        if (viewModel.getSelectedSection() != null) {
            int lastSectionIndex = noteSections.indexOf(noteSections.stream().filter(section ->
                    section.getId() == viewModel.getSelectedSection().getId()).toList().get(0));
            System.out.println(TAG + "Note Last Section Index -> " + lastSectionIndex);
            noteContentList.scrollTo(lastSectionIndex);
        }

//        noteContentList.setOnScrollFinished(event -> {
//            System.out.println(TAG + "Scroll Y-axis value -> " + event.getY());
//            System.out.println(TAG + "Scroll Screen-Y value -> " + event.getScreenY());
//            System.out.println(TAG + "Scroll Delta-Y value -> " + event.getDeltaY());
//            System.out.println(TAG + "Scroll Multiplier-Y value -> " + event.getMultiplierY());
//            System.out.println(TAG + "Scroll TextDelta-Y Units value -> " + event.getTextDeltaYUnits());
//            System.out.println(TAG + "Scroll TextDelta-Y value -> " + event.getTextDeltaY());
//            System.out.println(TAG + "Scroll Total Delta-Y value -> " + event.getTotalDeltaY());
//            System.out.println(TAG + "Scroll Scene-Y value -> " + event.getSceneY());
//
//        });

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

        prevButton.setOnAction(event -> {
            nextButton.setDisable(false);
            viewModel.setSelectedTopicIndex(viewModel.getSelectedTopicIndex() - 1);
        });

        nextButton.setOnAction(event -> {
            prevButton.setDisable(false);
            viewModel.setSelectedTopicIndex(viewModel.getSelectedTopicIndex() + 1);
        });

        if (viewModel.getSelectedTopicIndex() == 0) {
            prevButton.setDisable(true);
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

        fontDropdownList.setConverter(new StringConverter<Font>() {
            @Override
            public String toString(Font font) {
                if (font.getName().contains("Gilroy")){
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
        });;

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
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
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

    private void renderNote(NoteSubTopic subTopic) {
        List<Node> contentElements = new ArrayList<>();

//        viewModel.getSubTopicSections()
//                .get(subTopic.getId())
//                .forEach(section -> {
//
//                    ContentViewType contentViewType = ContentViewTypes.convert(section);
//                    if (contentViewType instanceof TextViewType) {
//                        System.out.println("ContentViewType -> TextViewType");
//                        TextViewType textViewType = (TextViewType) contentViewType;
//
//                        if (textViewType.getTitle() != null) {
//                            contentElements.add(getTitle(textViewType.getTitle().getText()));
//                        }
//
//                        if (textViewType.getBody() != null) {
//
//                            if (textViewType.getBody().getBodyType() != null) {
//                                System.out.println("TextView Body types -> " + Arrays.toString(textViewType.getBody().getBodyType()));
//
//                                if (textViewType.getBody().getBodyType()[0].equalsIgnoreCase("example")) {
//                                    VBox exampleBox = new VBox();
//                                    exampleBox.setStyle("-fx-background-color: #FFFFFF");
//
//                                    Label label = new Label("EXAMPLES");
//                                    label.setTextFill(Paint.valueOf("#FFFFFF"));
//                                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//                                    label.setAlignment(Pos.CENTER);
//                                    label.setStyle("-fx-background-color: #034801");
//                                    label.setPadding(new Insets(7, 0, 7, 0));
//
//                                    exampleBox.widthProperty().addListener((observable, oldValue, newValue) -> {
//                                        label.setPrefWidth((Double) newValue);
//                                    });
//
//                                    String[] dummyContent = {"Undergoes", "Cell Membrane", "Develop", "Component", "Structure"};
//
//                                    ArrayList<UnorderedListItem> contentItems = new ArrayList<>();
//
//                                    for (int i = 0; i < dummyContent.length; i++) {
//                                        UnorderedListItem unorderedListItem = new UnorderedListItem(dummyContent[i]);
//                                        contentItems.add(unorderedListItem);
//                                    }
//
//                                    ObservableList<UnorderedListItem> items = FXCollections.observableArrayList(contentItems);
//
//                                    ListView<UnorderedListItem> contentList = new ListView<>();
//                                    contentList.setItems(items);
//                                    contentList.setBackground(Background.EMPTY);
//                                    contentList.setCellFactory(new UnorderedListCellFactory());
//                                    contentList.setSelectionModel(new NoSelectionModel<>());
//                                    contentList.setPadding(new Insets(15, 0, 0, 15));
//
//                                    VBox.setMargin(exampleBox, new Insets(10, 10, 10, 10));
//                                    exampleBox.getChildren().addAll(label, contentList);
//
//                                    contentElements.add(exampleBox);
//                                }
//                                else if (textViewType.getBody().getBodyType()[0].equalsIgnoreCase("definition")) {
//                                    Label definition = new Label("These are very tiny organisms ranging from 0.1 to 10 µm in length. They can only be seen through the high power of a light microscope. They can be spherical (coccus) or rod-like (bacillus) in shape. They can also occur singly, in groups or in chains.These are very tiny organisms ranging from 0.1 to 10 µm in length. They can only be seen through the high power of a light microscope.");
//                                    definition.setWrapText(true);
//                                    definition.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//
//                                    Line stroke = new Line(0, 0, 0, 100);
//                                    stroke.setStroke(Paint.valueOf("#51C46B"));
//                                    stroke.setStrokeWidth(3);
//
//                                    HBox definitionBox = new HBox(15);
//                                    definitionBox.setPadding(new Insets(15));
//                                    definitionBox.getChildren().addAll(stroke, definition);
//
//                                    contentElements.add(definitionBox);
//                                }
//                                else if (textViewType.getBody().getBodyType()[0].equalsIgnoreCase("quote")) {
//                                    ImageView quoteImage = new ImageView(new Image(getClass().getResource("/drawable/note_content_quote_icon_1x.png").toString()));
//                                    quoteImage.setPreserveRatio(true);
//                                    quoteImage.setPickOnBounds(true);
//
//                                    Label quote = new Label("Living things are made up of plants and animals. They range from tiny microscopic plants/animals");
//                                    quote.setWrapText(true);
//                                    quote.setTextAlignment(TextAlignment.CENTER);
//                                    quote.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM_ITALIC, FontUtil.FontSize.FOURTEEN.size));
//
//                                    Label author = new Label("-");
//                                    author.setText(author.getText() + "Charles Darwin");
//                                    author.setTextFill(Paint.valueOf("#F4D242"));
//                                    author.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//
//                                    VBox quoteBox = new VBox(10);
//                                    quoteBox.setPadding(new Insets(10));
//                                    VBox.setMargin(quoteBox, new Insets(15));
//                                    quoteBox.getChildren().addAll(quoteImage, quote, author);
//                                    quoteBox.setAlignment(Pos.CENTER);
//
//                                    contentElements.add(quoteBox);
//                                }
//                                else if (textViewType.getBody().getBodyType()[0].equalsIgnoreCase("dyk")) {
//                                    Label didyouknow = new Label("DID YOU\nKNOW?");
//                                    didyouknow.setWrapText(true);
//                                    didyouknow.setTextFill(Paint.valueOf("#F4D242"));
//                                    didyouknow.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.TWENTY_TWO.size));
//
//                                    ImageView bulbImage = new ImageView(new Image(getClass().getResource("/drawable/bulb_icon_1x.png").toString()));
//                                    bulbImage.setPreserveRatio(true);
//                                    bulbImage.setPickOnBounds(true);
//
//                                    HBox textAndImageBox = new HBox(15, didyouknow, bulbImage);
//                                    textAndImageBox.setAlignment(Pos.CENTER);
//
//                                    Label didyouknowText = new Label("There is enough DNA in the average person's body to stretch from the sun to Pluto and back ");
//                                    didyouknowText.setWrapText(true);
//                                    didyouknowText.setTextAlignment(TextAlignment.CENTER);
//                                    didyouknowText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM_ITALIC, FontUtil.FontSize.FOURTEEN.size));
//
//                                    VBox didyouknowBox = new VBox(20);
//                                    didyouknowBox.setAlignment(Pos.CENTER);
//                                    didyouknowBox.setPadding(new Insets(30, 20, 30, 20));
//                                    didyouknowBox.setStyle("-fx-background-color: #E7F7E9; -fx-border-radius: 5;");
//                                    didyouknowBox.getChildren().addAll(textAndImageBox, didyouknowText);
//                                    VBox.setMargin(didyouknowBox, new Insets(10));
//
//                                    didyouknowBox.widthProperty().addListener((observable, oldValue, newValue) -> {
//                                        didyouknowText.setPrefWidth(newValue.doubleValue()/1.2);
//                                    });
//
//                                    contentLayout.widthProperty().addListener((observable, oldValue, newValue) -> {
//                                        didyouknowBox.setMaxWidth(newValue.doubleValue()/2);
//                                    });
//
//                                    contentElements.add(didyouknowBox);
//                                }
//                                else if (textViewType.getBody().getBodyType()[0].equalsIgnoreCase("reference")) {
//                                    Label references = new Label("References");
//                                    references.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//                                    references.setTextAlignment(TextAlignment.CENTER);
//                                    references.setAlignment(Pos.CENTER);
//
//                                    Label reference1 = new Label("Ndu, F.O. C. Ndu, Abun A. and Aina J.O. (2001)\n" +
//                                            "Senior Secondary School Biology:\n" +
//                                            "Books 1 -3, Lagos: Longman \n");
//                                    reference1.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//                                    reference1.setTextAlignment(TextAlignment.CENTER);
//
//                                    Label reference2 = new Label("Ndu, F.O. C. Ndu, Abun A. and Aina J.O. (2001)\n" +
//                                            "Senior Secondary School Biology:\n" +
//                                            "Books 1 -3, Lagos: Longman \n");
//                                    reference2.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//                                    reference2.setTextAlignment(TextAlignment.CENTER);
//
//                                    VBox referenceBox = new VBox(15);
//                                    referenceBox.setPadding(new Insets(20, 0, 20, 0));
//                                    referenceBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #5C5C5C;");
//                                    referenceBox.getChildren().addAll(references, reference1, reference2);
//                                    referenceBox.setAlignment(Pos.CENTER);
//                                    VBox.setMargin(referenceBox, new Insets(20));
//
//                                    contentElements.add(referenceBox);
//                                }
//                            }
//
//                            if (textViewType.getBody().getLink() != null) {
//                                System.out.println("This text contains a link");
//                            }else {
//                                System.out.println("This text doesn't contain a link");
//                            }
//
//
//                            Label body = new Label();
//                            body.setText(textViewType.getBody().getText());
//                            if (textViewType.getBody().getText().contains("<br>")) {
//                                String newText = textViewType.getBody().getText().replaceAll("<br>", System.lineSeparator());
//                                body.setText(newText);
//                            }
//                            body.setWrapText(true);
//                            body.setLineSpacing(8);
//                            body.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
//
//                            List<Highlights> highlights = viewModel.getSubjectHighlights();
//                            for (int i = 0; i < highlights.size(); i ++) {
//                                if (highlights.get(i).getNoteId() == section.getId()) {
////                                System.out.println("Got highlight with id -> " + highlights.get(i).getNoteId());
////                                System.out.println("Got highlight color -> " + highlights.get(i).getColor());
//                                    body.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.web(highlights.get(i).getColor()), null, null)}, null));
//                                }
//                            }
//                            body.setOnMouseClicked(event -> {
//                                if (!noteOptions.isVisible()) {
//                                    showNoteOptions(section);
//                                } else {
//                                    hideNoteOptions();
//                                }
//                            });
//
//                            contentElements.add(body);
//                        }
////                        CustomWebView webView = new CustomWebView();
////                        webView.loadContent(textViewType.getBody().getText());
//
//                    }
//                    else if (contentViewType instanceof HtmlViewType) {
//                        System.out.println("ContentViewType -> HTMLViewType");
//                        HtmlViewType viewType = (HtmlViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//
//                        CustomWebView webView = new CustomWebView();
//                        webView.loadContent(viewType.getBody().getText());
//
//                        contentElements.add(webView);
//
//                    }
//                    else if (contentViewType instanceof WebViewType) {
//                        System.out.println("ContentViewType -> WebViewType");
//                        WebViewType viewType = (WebViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//                        CustomWebView webView = new CustomWebView();
//                        webView.loadContent(viewType.getBody().getText());
//
//                        contentElements.add(webView);
//
//                    }
//                    else if (contentViewType instanceof ImageViewType) {
//                        System.out.println("ContentViewType -> ImageViewType");
//                        ImageViewType imageViewType = (ImageViewType) contentViewType;
//
//                        if (imageViewType.getTitle() != null) {
//                            contentElements.add(getTitle(imageViewType.getTitle().getText()));
//                        }
//
//                        if (imageViewType.getBody() == null) {
//                            System.out.println("Empty image");
//                        }else {
//                            Image image = new Image(imageViewType.getBody().getUrl());
//                            ImageView imageView = new ImageView(image);
//
//                            imageView.setFitHeight(imageViewType.getBody().getHeightPx());
//                            imageView.setFitWidth(imageViewType.getBody().getWidthPx());
//                            imageView.setPreserveRatio(true);
//
//                           // System.out.println("Got ImageView with height -> " + imageViewType.getBody().getHeightPx() + " and width -> " + imageViewType.getBody().getWidthPx());
//
//                            contentElements.add(imageView);
//                        }
//
//                    }
//                    else if (contentViewType instanceof CBTViewType) {
//                        System.out.println("ContentViewType -> CBTViewType");
//                        CBTViewType cbtViewType = (CBTViewType) contentViewType;
//
//                        String questionTitle = "";
//
//                        if (cbtViewType.getTitle() != null) {
//                            questionTitle = cbtViewType.getTitle().getText().toUpperCase();
//                        }
//
//                        VBox cbtVBox = new VBox();
//                        cbtVBox.setPadding(new Insets(20, 20, 30, 20));
//                        cbtVBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #51C46B; -fx-border-radius: 10;");
//                        cbtVBox.setSpacing(10);
//                        VBox.setMargin(cbtVBox, new Insets(20, 0, 0, 0));
//
//                        Label questionTitleLabel = new Label();
//                        questionTitleLabel.setText(questionTitle);
//                        questionTitleLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//                        VBox.setMargin(questionTitleLabel, new Insets(0, 0, 10, 0));
//
//                        Label questionBox = new Label();
//                        questionBox.setText("This zygote undergoes meiosis to form spores. Each spore develops into a new organism.");
//                        questionBox.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
//                        questionBox.setWrapText(true);
//                        questionBox.setPadding(new Insets(10));
//                        questionBox.setStyle("-fx-background-color: #E7F7E9; -fx-border-color: #034801; -fx-border-radius: 5; ");
//
//                        RadioButton optionAButton = new RadioButton();
//                        optionAButton.setText("Cell membrane");
//                        optionAButton.setAlignment(Pos.CENTER);
//                        optionAButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//                        optionAButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
//                        optionAButton.setPadding(new Insets(10));
//
//                        RadioButton optionBButton = new RadioButton();
//                        optionBButton.setText("Cell structure");
//                        optionBButton.setAlignment(Pos.CENTER);
//                        optionBButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//                        optionBButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
//                        optionBButton.setPadding(new Insets(10));
//
//                        RadioButton optionCButton = new RadioButton();
//                        optionCButton.setText("Cell component");
//                        optionCButton.setAlignment(Pos.CENTER);
//                        optionCButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//                        optionCButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
//                        optionCButton.setPadding(new Insets(10));
//
//                        questionBox.widthProperty().addListener(((observable, oldValue, newValue) -> {
//                            optionAButton.setMinWidth((Double) newValue);
//                            optionBButton.setMinWidth((Double) newValue);
//                            optionCButton.setMinWidth((Double) newValue);
//                        }));
//
//                        ToggleGroup optionsToggle = new ToggleGroup();
//                        optionsToggle.getToggles().addAll(optionAButton, optionBButton, optionCButton);
//
//                        HBox hBox = new HBox();
//                        VBox.setMargin(hBox, new Insets(15, 0, 0, 5));
//                        Label seeExplanation = new Label("See explanation");
//                        seeExplanation.setAlignment(Pos.CENTER_LEFT);
//                        seeExplanation.setTextFill(Paint.valueOf("#51C46B"));
//                        seeExplanation.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//                        ImageView showExplanation = new ImageView(new Image(getClass().getResource("/drawable/cbtview_show_explanation_icon_1x.png").toString()));
//                        showExplanation.setFitWidth(15);
//                        showExplanation.setFitHeight(15);
//                        showExplanation.setPreserveRatio(true);
//                        showExplanation.setPickOnBounds(true);
//                        ImageView hideExplanation = new ImageView(new Image(getClass().getResource("/drawable/cbtview_hide_explanation_icon_1x.png").toString()));
//                        hideExplanation.setFitWidth(15);
//                        hideExplanation.setFitHeight(15);
//                        hideExplanation.setPreserveRatio(true);
//                        hideExplanation.setPickOnBounds(true);
//                        Button showHideExplanation = new Button();
//                        showHideExplanation.setBackground(Background.EMPTY);
//                        showHideExplanation.setGraphic(showExplanation);
//                        HBox.setMargin(showHideExplanation, new Insets(0, 0, 0, 10));
//
//                        hBox.getChildren().addAll(seeExplanation, showHideExplanation);
//
//                        Label explanationText = new Label();
//                        explanationText.setText("This zygote undergoes meiosis to form spores. Each spore develops into a new organism.");
//                        explanationText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
//                        explanationText.setWrapText(true);
//                        explanationText.setPadding(new Insets(10));
//                        explanationText.setStyle("-fx-border-color: #034801; -fx-border-radius: 5; ");
//
//                        cbtVBox.getChildren().addAll(questionTitleLabel, questionBox, optionAButton, optionBButton, optionCButton, hBox);
//
//                        showHideExplanation.setOnAction(event -> {
//                            if (showHideExplanation.getGraphic() == showExplanation){
//                                showHideExplanation.setGraphic(hideExplanation);
//                                seeExplanation.setText("Hide explanation");
//                                cbtVBox.getChildren().add(explanationText);
//                            }else {
//                                showHideExplanation.setGraphic(showExplanation);
//                                seeExplanation.setText("See explanation");
//                                cbtVBox.getChildren().remove(explanationText);
//                            }
//                        });
//
//                        VBox.setMargin(cbtVBox, new Insets(0, 100, 0, 0));
//                        contentElements.add(cbtVBox);
//
//                    }
//                    else if (contentViewType instanceof OrderedListViewType) {
//                        System.out.println("ContentViewType -> OrderedListViewType");
//                        OrderedListViewType viewType = (OrderedListViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//
//                        String[] content = viewType.getBody().getList();
//
//                        ArrayList<OrderedListItem> contentItems = new ArrayList<>();
//                        for (int i = 0; i < content.length; i++) {
//                            OrderedListItem orderedListItem = new OrderedListItem(String.valueOf(i+1), content[i]);
//                            contentItems.add(orderedListItem);
//
//                        }
//
//                        ObservableList<OrderedListItem> items = FXCollections.observableArrayList(contentItems);
//
//                        ListView<OrderedListItem> contentList = new ListView<>();
//                        contentList.setItems(items);
//                        contentList.setBackground(Background.EMPTY);
//                        contentList.setCellFactory(new OrderedListCellFactory());
//                        contentList.setSelectionModel(new NoSelectionModel<>());
//
//                        contentElements.add(contentList);
//
//                    }
//                    else if (contentViewType instanceof UnorderedListViewType) {
//                        System.out.println("ContentViewType -> UnorderedListViewType");
//
//                        UnorderedListViewType viewType = (UnorderedListViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//
//                        String[] content = viewType.getBody().getList();
//
//                        ArrayList<UnorderedListItem> contentItems = new ArrayList<>();
//                        for (int i = 0; i < content.length; i++) {
//                            UnorderedListItem unorderedListItem = new UnorderedListItem(content[i]);
//                            contentItems.add(unorderedListItem);
//                           // System.out.println("UnorderedListItem created with text -> " + unorderedListItem.getText());
//                        }
//
//                        ObservableList<UnorderedListItem> items = FXCollections.observableArrayList(contentItems);
//
//                        ListView<UnorderedListItem> contentList = new ListView<>();
//                        contentList.setItems(items);
//                        contentList.setBackground(Background.EMPTY);
//                        contentList.setCellFactory(new UnorderedListCellFactory());
//                        contentList.setSelectionModel(new NoSelectionModel<>());
//
//                        contentElements.add(contentList);
//
//                    }
//                    else if (contentViewType instanceof TableViewType) {
//                        System.out.println("ContentViewType -> TableViewType");
//                        TableViewType viewType = (TableViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//
//                        GridPane tableGrid = new GridPane();
//                        tableGrid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #F4D242");
//
//                        if (viewType.getBody().getHeader() != null) {
//                            ColumnConstraints[] columnConstraints = new ColumnConstraints[viewType.getBody().getHeader().length];
//
//                            for (int col = 0; col < viewType.getBody().getHeader().length; col++) {
//                                Label grid = new Label(viewType.getBody().getHeader()[col]);
//                                grid.setWrapText(true);
//                                grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//                                grid.setPadding(new Insets(5, 5, 5, 5));
//                               // grid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #F4D242;");
//
//                                columnConstraints[col] = new ColumnConstraints();
//                                columnConstraints[col].setPercentWidth(100.0 / viewType.getBody().getHeader().length);
//                                tableGrid.getColumnConstraints().add(columnConstraints[col]);
//
//                                tableGrid.add(grid, col, 0);
//                            }
//                        }
//
//                        if (viewType.getBody().getRows() != null) {
//
//                            for (int row = 0; row < viewType.getBody().getRows().length; row++) {
//                                for (int col = 0; col < viewType.getBody().getRows()[row].length; col++) {
//                                    Label grid = new Label(viewType.getBody().getRows()[row][col]);
//                                    grid.setWrapText(true);
//                                    grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//                                    grid.setPadding(new Insets(5, 5, 5, 5));
//                                    //grid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #F4D242;");
//
//                                    tableGrid.add(grid, col, row+1);
//                                }
//                            }
//                        }
//
//                        if (viewType.getBody().getFooter() != null) {
//
//                            for (int col = 0; col < viewType.getBody().getFooter().length; col++) {
//                                Label grid = new Label(viewType.getBody().getFooter()[col]);
//                                grid.setWrapText(true);
//                                grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
//                                grid.setPadding(new Insets(5, 5, 5, 5));
//
//                                tableGrid.add(grid, col, viewType.getBody().getRows().length + 1);
//                            }
//                        }
//
//                        /*TableView tableView = new TableView();
//
//                        if (viewType.getBody().getHeader() != null) {
//
//                            for (int i = 0; i < viewType.getBody().getHeader().length; i++) {
//
//                                TableColumn<Map, String> column = new TableColumn<>(viewType.getBody().getHeader()[i]);
//
//                                column.setCellValueFactory(new MapValueFactory<>(i));
//
//                                tableView.getColumns().add(column);
//                            }
//
//                        }else {
//                            for (int i = 0; i < viewType.getBody().getRows()[0].length; i++) {
//
//                                TableColumn<Map, String> column = new TableColumn<>();
//
//                                column.setCellValueFactory(new MapValueFactory<>(i));
//
//                                tableView.getColumns().add(column);
//                            }
//                        }
//                        for (int i = 0; i < viewType.getBody().getRows().length; i++) {
//                            Map<Integer, Object> item = new HashMap<>();
//
//                            for (int j = 0; j < viewType.getBody().getRows()[i].length; j++) {
//                                item.put(j, viewType.getBody().getRows()[i][j]);
//                                //  System.out.println("Key: j -> " + j + " -> " + item.get(j));
//                            }
//
//                            tableView.getItems().add(item);
//                        }
//
//                        if (viewType.getBody().getFooter() != null) {
//                            Map<Integer, Object> footerItem = new HashMap<>();
//
//                            for (int k = 0; k < viewType.getBody().getFooter().length; k++) {
//                                footerItem.put(k, viewType.getBody().getFooter()[k]);
//                                // System.out.println("Key: k -> " + k + " -> " + footerItem.get(k));
//                            }
//                            tableView.getItems().add(footerItem);
//                        }*/
//
//                        contentElements.add(tableGrid);
//
//                    }
//                    else if (contentViewType instanceof TableHHViewType) {
//                        System.out.println("ContentViewType -> TableHHViewType");
//                        TableHHViewType viewType = (TableHHViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//
//                        GridPane tableGrid = new GridPane();
//                        tableGrid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #F4D242");
//
//                        if (viewType.getBody().getHeader() != null) {
//                            ColumnConstraints[] columnConstraints = new ColumnConstraints[viewType.getBody().getHeader().length];
//
//                            for (int col = 0; col < viewType.getBody().getHeader().length; col++) {
//                                System.out.println("Column -> " + viewType.getBody().getHeader()[col]);
//
//                                Label grid = new Label(viewType.getBody().getHeader()[col]);
//                                grid.setWrapText(true);
//                                grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//                                grid.setPadding(new Insets(5, 5, 5, 5));
//
//                                columnConstraints[col] = new ColumnConstraints();
//                                columnConstraints[col].setPercentWidth(100.0 / viewType.getBody().getHeader().length);
//                                tableGrid.getColumnConstraints().add(columnConstraints[col]);
//
//                                tableGrid.add(grid, col, 0);
//                            }
//                        }
//
//                        if (viewType.getBody().getRows() != null) {
//                            System.out.println("Number of Maps -> " + viewType.getBody().getRows().size());
//
//                            if (viewType.getBody().getHeader() == null) {
//                                // Values List of the first map in Rows[]. It is used to set the ColumnConstraints for the Table Columns
//                                List<String> firstMapValuesList = viewType.getBody().getRows().get(0).values().iterator().next();
//
//                                ColumnConstraints[] columnConstraint = new ColumnConstraints[firstMapValuesList.size()+1];
//                                for (int i = 0; i < firstMapValuesList.size()+1; i++) {
//                                    columnConstraint[i] = new ColumnConstraints();
//                                    columnConstraint[i].setPercentWidth(100.0 / firstMapValuesList.size()+1);
//                                    tableGrid.getColumnConstraints().add(columnConstraint[i]);
//                                }
//                            }
//
//                            for (int row = 0; row < viewType.getBody().getRows().size(); row++) {
//                                System.out.println("Got Map -> " + viewType.getBody().getRows().get(row));
//                                List<String> valuesList = viewType.getBody().getRows().get(row).values().iterator().next();
//                                System.out.println("Map valuesList -> " + valuesList);
//                                String key = getKey(viewType.getBody().getRows().get(row), valuesList);
//                                System.out.println("Key in Map -> " + key);
//
//                                for (int col = 0; col < viewType.getBody().getRows().get(row).size(); col++) {
//                                    System.out.println("Number of columns -> " + viewType.getBody().getRows().get(row).size());
//
//                                    Label horizontalHeader = new Label(key);
//                                    horizontalHeader.setWrapText(true);
//                                    horizontalHeader.setPadding(new Insets(5, 5, 5, 5));
//                                    horizontalHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//
//                                    tableGrid.add(horizontalHeader, col, row + 1);
//
//
//                                    for (int i = 1; i < valuesList.size()+1; i++) {
//                                        System.out.println("Single value in valuesList -> " + valuesList.get(i-1));
//
//                                        Label grid = new Label(valuesList.get(i-1));
//                                        grid.setWrapText(true);
//                                        grid.setPadding(new Insets(5, 5, 5, 5));
//                                        grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//
//                                        tableGrid.add(grid, col + i, row + 1);
//                                    }
//
//                                }
//                            }
//                        }
//
//                        if (viewType.getBody().getFooter() != null) {
//                            System.out.println("Got Footer Map -> " + viewType.getBody().getFooter());
//                            List<String> valuesList = viewType.getBody().getFooter().values().iterator().next();
//                            System.out.println("FooterMap valuesList -> " + valuesList);
//                            String key = getKey(viewType.getBody().getFooter(), valuesList);
//                            System.out.println("Key in FooterMap -> " + key);
//
//                            Label footer = new Label(key);
//                            footer.setWrapText(true);
//                            footer.setPadding(new Insets(5, 5, 5, 5));
//                            footer.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));
//
//                            tableGrid.add(footer, 0, viewType.getBody().getRows().size() + 1);
//
//                            for (int i = 0; i < valuesList.size(); i++) {
//                                Label grid = new Label(valuesList.get(i));
//                                grid.setWrapText(true);
//                                grid.setPadding(new Insets(5, 5, 5, 5));
//                                grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
//
//                                tableGrid.add(grid, i+1, viewType.getBody().getRows().size() + 1);
//                            }
//                        }
//
//                        contentElements.add(tableGrid);
//                    }
//                    else if (contentViewType instanceof AudioViewType) {
//                        System.out.println("ContentViewType -> AudioViewType");
//                        AudioViewType viewType = (AudioViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//
//                        if (viewType.getBody() != null) {
//                            System.out.println("Got audio -> " + viewType.getBody().getUrl() + " with duration " + viewType.getBody().getDurationSeconds());
//
//                            String mediaSource = viewType.getBody().getUrl();
//
//                            Media media = new Media(mediaSource);
//                            MediaPlayer mediaPlayer = new MediaPlayer(media);
//                            mediaPlayer.setAutoPlay(false);
//                            media.setOnError(() -> {
//                                System.out.println("Media error -> " + media.getError());
//                            });
//                            mediaPlayer.setOnError(() -> {
//                                System.out.println("MediaPlayer error -> " + mediaPlayer.getError());
//                            });
//
//                            VBox vBox = new VBox();
//                            vBox.setPrefHeight(80);
//                            vBox.setPrefWidth(120);
//                            vBox.setPadding(new Insets(10, 0, 0, 0));
//                            vBox.setStyle("-fx-background-color: #034801; -fx-background-radius: 20 20 22 22;");
//
//                            StackPane controlsPane = new StackPane();
//                            controlsPane.setPadding(new Insets(10, 15, 10, 25));
//                            controlsPane.setPrefWidth(vBox.getPrefWidth());
//                            controlsPane.setStyle("-fx-background-color: #E7F7E9; -fx-background-radius: 20;");
//
//                            Image muteIcon = new Image(getClass().getResource("/drawable/audio_type_mute_icon_1x.png").toString());
//                            Image unMuteIcon = new Image(getClass().getResource("/drawable/audio_type_unmute_icon_1x.png").toString());
//                            ImageView speaker = new ImageView(muteIcon);
//                            speaker.setPreserveRatio(true);
//                            speaker.setPickOnBounds(true);
//                            speaker.setFitWidth(20);
//                            speaker.setFitHeight(20);
//                            StackPane.setAlignment(speaker, Pos.CENTER_RIGHT);
//                            StackPane.setMargin(speaker, new Insets(0, 10, 0, 0));
//                            speaker.setOnMouseClicked(event -> {
//                                if (mediaPlayer.getVolume() > 0.0) {
//                                    mediaPlayer.setVolume(0.0);
//                                    speaker.setImage(unMuteIcon);
//                                }else {
//                                    mediaPlayer.setVolume(1.0);
//                                    speaker.setImage(muteIcon);
//                                }
//                            });
//
//                            ImageView playIcon = new ImageView(new Image(getClass().getResource("/drawable/audio_type_play_icon_1x.png").toString()));
//                            playIcon.setPreserveRatio(true);
//                            playIcon.setPickOnBounds(true);
//                            playIcon.setFitWidth(20);
//                            playIcon.setFitHeight(20);
//                            ImageView pauseIcon = new ImageView(new Image(getClass().getResource("/drawable/audio_type_pause_icon_1x.png").toString()));
//                            pauseIcon.setPreserveRatio(true);
//                            pauseIcon.setPickOnBounds(true);
//                            pauseIcon.setFitWidth(20);
//                            pauseIcon.setFitHeight(20);
//
//                            Button playButton = new Button();
//                            playButton.setBackground(Background.EMPTY);
//                            playButton.setGraphic(playIcon);
//                            StackPane.setAlignment(playButton, Pos.CENTER_LEFT);
//
//                            Label duration = new Label("17:34 / 59:32");
//                            duration.setAlignment(Pos.CENTER);
//                            StackPane.setAlignment(duration, Pos.CENTER_LEFT);
//                            StackPane.setMargin(duration, new Insets(0, 0, 0, 40));
//
//                            controlsPane.getChildren().addAll(playButton, duration, speaker);
//
//                            ImageView equalizerImage = new ImageView(new Image(getClass().getResource("/drawable/audio_type_equalizer_view_1x.png").toString()));
//                            equalizerImage.setPreserveRatio(true);
//                            equalizerImage.setPickOnBounds(true);
//                            StackPane imagePane = new StackPane(equalizerImage);
//                            imagePane.setPadding(new Insets(10, 0, 10, 0));
//                            StackPane.setAlignment(equalizerImage, Pos.CENTER);
//
//                            vBox.getChildren().addAll(imagePane, controlsPane);
//
//                            playButton.setOnAction(event -> {
//                                Status mediaStatus = mediaPlayer.getStatus();
//                                System.out.println("Media Status -> " + mediaPlayer.getStatus());
//                                if (mediaStatus != Status.PLAYING){
//                                    mediaPlayer.play();
//                                }else {
//                                    mediaPlayer.pause();
//                                }
//
//                                if (mediaStatus == Status.STOPPED) {
//                                    mediaPlayer.play();
//                                }
//
//                                if (mediaStatus != Status.PLAYING) {
//                                    playButton.setGraphic(pauseIcon);
//                                }else {
//                                    playButton.setGraphic(playIcon);
//                                }
//
//                            });
//
//                            mediaPlayer.setOnEndOfMedia(() -> {
//                                playButton.setGraphic(playIcon);
//                                mediaPlayer.seek(mediaPlayer.getStartTime());
//                                mediaPlayer.pause();
//                            });
//
//                            contentElements.add(vBox);
//                        }
//
//                    }
//                    else if (contentViewType instanceof VideoViewType) {
//                        System.out.println("ContentViewType -> VideoViewType");
//                        VideoViewType viewType = (VideoViewType) contentViewType;
//
//                        if (viewType.getTitle() != null) {
//                            contentElements.add(getTitle(viewType.getTitle().getText()));
//                        }
//
////                        System.out.println("Got video -> " + viewType.getBody().getUrl() + " with duration " + viewType.getBody().getDurationSeconds());
//
//                        String mediaSource = viewType.getBody().getUrl();
//                        String mediaUrl = getClass().getResource("/assets/coding.mp4").toExternalForm();
//
//                        Media media = new Media(mediaUrl);
//                        MediaPlayer mediaPlayer = new MediaPlayer(media);
//
//                        System.out.println("Media Start time -> " + mediaPlayer.getStartTime() + " and Stop time -> " + mediaPlayer.getStopTime());
//                        media.setOnError(() -> {
//                            System.out.println("Media error -> " + media.getError());
//                        });
//                        mediaPlayer.setOnError(() -> {
//                            System.out.println("MediaPlayer error -> " + mediaPlayer.getError());
//                        });
//
//                        MediaView mediaView = new MediaView(mediaPlayer);
//                        mediaView.setSmooth(true);
//                        mediaView.setOnError(event -> {
//                            System.out.println("MediaView error -> " + event.getMediaError().toString());
//                        });
//
//                        DropShadow dropshadow = new DropShadow(20, Color.GRAY);
//                        mediaView.setEffect(dropshadow);
//
//                        StackPane videoPane = new StackPane();
//
//                        Label videoDescription = new Label("Cell structure and functions of cell components ");
//                        videoDescription.setBackground(Background.EMPTY);
//                        videoDescription.setTextFill(Paint.valueOf("#FFFFFF"));
//                        videoDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//
//                        Label duration = new Label("45 mins");
//                        duration.setBackground(Background.EMPTY);
//                        duration.setTextFill(Paint.valueOf("#FFFFFF"));
//                        duration.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//
//                        Button playButton = new Button();
//                        ImageView playIcon = new ImageView(new Image(getClass().getResource("/drawable/video_type_play_icon.png").toString()));
//                        ImageView pauseIcon = new ImageView(new Image(getClass().getResource("/drawable/video_type_pause_icon_1x.png").toString()));
//                        playButton.setGraphic(playIcon);
//                        playButton.setBackground(Background.EMPTY);
//                        playButton.setOnAction(event -> {
//                            Status status = mediaPlayer.getStatus();
//                            if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
//                                mediaPlayer.play();
//                                playButton.setGraphic(pauseIcon);
//                            } else {
//                                mediaPlayer.pause();
//                                playButton.setGraphic(playIcon);
//                            }
//                        });
//
//                        mediaPlayer.setOnEndOfMedia(() -> {
//                            // Implement later
//                        });
//
//                        StackPane.setAlignment(playButton, Pos.CENTER);
//                        StackPane.setAlignment(videoDescription, Pos.BOTTOM_LEFT);
//                        StackPane.setMargin(videoDescription, new Insets(0, 0, 10, 30));
//                        StackPane.setAlignment(duration, Pos.BOTTOM_RIGHT);
//                        StackPane.setMargin(duration, new Insets(0, 30, 10, 0));
//
//                        videoPane.setOnMouseEntered(event -> {
//                            Status mediaStatus = mediaPlayer.getStatus();
//                            if (mediaStatus == Status.READY) {
//                                if (playButton.isVisible() || videoDescription.isVisible() || duration.isVisible()) {
//                                    return;
//                                }
//                            }
//
//                            Animations.fadeIn(playButton, 300, 300);
//                            Animations.fadeIn(videoDescription, 300, 300);
//                            Animations.fadeIn(duration, 300, 300);
//
//                        });
//                        videoPane.setOnMouseExited(event -> {
//                            Animations.fadeOut(playButton, 300, 1000);
//                            Animations.fadeOut(videoDescription, 300, 1000);
//                            Animations.fadeOut(duration, 300, 1000);
//
//                        });
//                        videoPane.getChildren().addAll(mediaView, playButton, videoDescription, duration);
//
//
//                        contentElements.add(videoPane);
//                    }
//                });

        viewModel.getNoteSections()
                .get(subTopic.getTopicId())
                .forEach(section -> {

                    ContentViewType contentViewType = ContentViewTypes.convert(section);
                    if (contentViewType instanceof HeaderViewType) {
//                        System.out.println(TAG + "ContentViewType -> HeaderViewType");

                        HeaderViewType headerViewType = (HeaderViewType) contentViewType;
                        if (headerViewType.getText() != null) {
                            Document doc = Jsoup.parse(headerViewType.getText());
                            String formattedText = doc.body().text();
                            Label label = new Label(formattedText);
                            label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                            label.setWrapText(true);

                            contentElements.add(label);
                        }
                    } else if (contentViewType instanceof ParagraphViewType) {
//                        System.out.println(TAG + "ContentViewType -> ParagraphViewType");

                        ParagraphViewType paragraphViewType = (ParagraphViewType) contentViewType;
                        if (paragraphViewType.getText() != null) {
                            Document doc = Jsoup.parse(paragraphViewType.getText());
                            String formattedText = doc.body().text();
                            Label label = new Label(formattedText);
                            label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                            label.setWrapText(true);

                            contentElements.add(label);
                        }
                    } else if (contentViewType instanceof CBTViewType) {
                        System.out.println(TAG + "ContentViewType -> CBTViewType");
                        CBTViewType cbtViewType = (CBTViewType) contentViewType;

                        // Show the question

                        int yearId = cbtViewType.getYearId();
                        int questionId = cbtViewType.getQuestionId();

                        ObjectiveQuestion question = viewModel.getQuestion(yearId, questionId);

                        VBox cbtVBox = new VBox();
                        cbtVBox.setPadding(new Insets(20, 20, 30, 20));
                        cbtVBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #51C46B; -fx-border-radius: 10;");
                        cbtVBox.setSpacing(10);
                        VBox.setMargin(cbtVBox, new Insets(20, 0, 0, 0));

                        Document doc = Jsoup.parse(question.getQuestion());
                        String formattedText = doc.body().text();
                        Label questionBox = new Label(formattedText);
                        questionBox.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
                        questionBox.setWrapText(true);
                        questionBox.setPadding(new Insets(10));
                        questionBox.setStyle("-fx-background-color: #E7F7E9; -fx-border-color: #034801; -fx-border-radius: 5; ");

                        RadioButton optionAButton = new RadioButton();
                        optionAButton.setText(question.getOptionA().getText());
                        optionAButton.setAlignment(Pos.CENTER);
                        optionAButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
                        optionAButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
                        optionAButton.setPadding(new Insets(10));

                        RadioButton optionBButton = new RadioButton();
                        optionBButton.setText(question.getOptionB().getText());
                        optionBButton.setAlignment(Pos.CENTER);
                        optionBButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
                        optionBButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
                        optionBButton.setPadding(new Insets(10));

                        RadioButton optionCButton = new RadioButton();
                        optionCButton.setText(question.getOptionC().getText());
                        optionCButton.setAlignment(Pos.CENTER);
                        optionCButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
                        optionCButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
                        optionCButton.setPadding(new Insets(10));

                        RadioButton optionDButton = new RadioButton();
                        optionDButton.setText(question.getOptionD().getText());
                        optionDButton.setAlignment(Pos.CENTER);
                        optionDButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
                        optionDButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
                        optionDButton.setPadding(new Insets(10));

                        questionBox.widthProperty().addListener(((observable, oldValue, newValue) -> {
                            optionAButton.setMinWidth((Double) newValue);
                            optionBButton.setMinWidth((Double) newValue);
                            optionCButton.setMinWidth((Double) newValue);
                            optionDButton.setMinWidth((Double) newValue);
                        }));

                        ToggleGroup optionsToggle = new ToggleGroup();
                        optionsToggle.getToggles().addAll(optionAButton, optionBButton, optionCButton, optionDButton);

                        optionAButton.setOnAction(event -> {
                            if (question.getQuestionAnswer().getId() == 0) {
                                optionAButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                            } else {
                                optionAButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");

                            }
                        });
                        optionBButton.setOnAction(event -> {
                            if (question.getQuestionAnswer().getId() == 1) {
                                optionBButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                            } else {
                                optionBButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");
                            }
                        });
                        optionCButton.setOnAction(event -> {
                            if (question.getQuestionAnswer().getId() == 2) {
                                optionCButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                            } else {
                                optionCButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");
                            }
                        });
                        optionDButton.setOnAction(event -> {
                            if (question.getQuestionAnswer().getId() == 3) {
                                optionDButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                            } else {
                                optionDButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");
                            }
                        });

                        HBox hBox = new HBox();
                        VBox.setMargin(hBox, new Insets(15, 0, 0, 5));
                        Label seeExplanation = new Label("See explanation");
                        seeExplanation.setAlignment(Pos.CENTER_LEFT);
                        seeExplanation.setTextFill(Paint.valueOf("#51C46B"));
                        seeExplanation.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
                        ImageView showExplanation = new ImageView(new Image(getClass().getResource("/drawable/cbtview_show_explanation_icon_1x.png").toString()));
                        showExplanation.setFitWidth(15);
                        showExplanation.setFitHeight(15);
                        showExplanation.setPreserveRatio(true);
                        showExplanation.setPickOnBounds(true);
                        ImageView hideExplanation = new ImageView(new Image(getClass().getResource("/drawable/cbtview_hide_explanation_icon_1x.png").toString()));
                        hideExplanation.setFitWidth(15);
                        hideExplanation.setFitHeight(15);
                        hideExplanation.setPreserveRatio(true);
                        hideExplanation.setPickOnBounds(true);
                        Button showHideExplanation = new Button();
                        showHideExplanation.setBackground(Background.EMPTY);
                        showHideExplanation.setGraphic(showExplanation);
                        HBox.setMargin(showHideExplanation, new Insets(0, 0, 0, 10));

                        hBox.getChildren().addAll(seeExplanation, showHideExplanation);

                        Label explanationText = new Label();
                        explanationText.setText(question.getQuestionAnswer().getExplanation());
                        explanationText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
                        explanationText.setWrapText(true);
                        explanationText.setPadding(new Insets(10));
                        explanationText.setStyle("-fx-border-color: #034801; -fx-border-radius: 5; ");

                        cbtVBox.getChildren().addAll(questionBox, optionAButton, optionBButton, optionCButton, optionDButton, hBox);

                        showHideExplanation.setOnAction(event -> {
                            if (showHideExplanation.getGraphic() == showExplanation){
                                showHideExplanation.setGraphic(hideExplanation);
                                seeExplanation.setText("Hide explanation");
                                cbtVBox.getChildren().add(explanationText);
                            }else {
                                showHideExplanation.setGraphic(showExplanation);
                                seeExplanation.setText("See explanation");
                                cbtVBox.getChildren().remove(explanationText);
                            }
                        });

                        VBox.setMargin(cbtVBox, new Insets(0, 100, 0, 0));

                        contentElements.add(cbtVBox);

                    } else if (contentViewType instanceof LatexMathViewType) {
                        System.out.println(TAG + "ContentViewType -> LatexMathViewType");
                        LatexMathViewType latexMathViewType = (LatexMathViewType) contentViewType;

                        // Show the content

                        if (latexMathViewType.getKatex() != null) {
                            org.commonmark.node.Node document = markdownParser.parse(latexMathViewType.getKatex());
                            String htmlKatex = htmlRenderer.render(document);

                            Document doc = Jsoup.parse(htmlKatex);
                            String formattedText = doc.body().text();

                            Label label = new Label(formattedText);
                            label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                            label.setWrapText(true);

                            contentElements.add(label);
                        }

                    } else if (contentViewType instanceof ListViewType) {
                        System.out.println(TAG + "ContentViewType -> ListViewType");
                        ListViewType listViewType = (ListViewType) contentViewType;

                        // Show the first item in the List

                        if (listViewType.getStyle().equalsIgnoreCase("unordered")) {

                            ArrayList<UnorderedListItem> contentItems = new ArrayList<>();

                            for (int i = 0; i < listViewType.getItems().size(); i++) {
                                UnorderedListItem unorderedListItem = new UnorderedListItem(listViewType.getItems().get(i));
                                contentItems.add(unorderedListItem);
                            }

                            ObservableList<UnorderedListItem> items = FXCollections.observableArrayList(contentItems);

                            ListView<UnorderedListItem> contentList = new ListView<>();
                            contentList.setItems(items);
                            contentList.setBackground(Background.EMPTY);
                            contentList.setCellFactory(new UnorderedListCellFactory());
                            contentList.setSelectionModel(new NoSelectionModel<>());
                            contentList.setPadding(new Insets(0, 0, 0, 10));

                            contentElements.add(contentList);
                        }

                    } else if (contentViewType instanceof NestedListViewType) {
                        System.out.println(TAG + "ContentViewType -> NestedListViewType");
                    } else if (contentViewType instanceof ReferenceViewType) {
                        System.out.println(TAG + "ContentViewType -> ReferenceViewType");

                        // Show the Reference content

                        ReferenceViewType referenceViewType = (ReferenceViewType) contentViewType;
                        if (referenceViewType.getText() != null) {
                            Document doc = Jsoup.parse(referenceViewType.getText());
                            String formattedText = doc.body().text();

                            Label label = new Label(formattedText);
                            label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                            label.setWrapText(true);

                            contentElements.add(label);
                        }
                    } else if (contentViewType instanceof SimpleImageViewType) {
                        System.out.println(TAG + "ContentViewType -> SimpleImageViewType");
                    } else if (contentViewType instanceof TableViewType) {
                        System.out.println(TAG + "ContentViewType -> TableViewType");

                        TableViewType tableViewType = (TableViewType) contentViewType;

                        // Show the first two headers

                        GridPane tableGrid = new GridPane();
                        tableGrid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #F4D242");

                        List<List<String>> content = tableViewType.getContent();

                        for (int row = 0; row < content.size(); row++) {

                            System.out.println("Row Content -> " + content.get(row));
                            for (int col = 0; col < content.get(row).size(); col++) {
                                System.out.println("Column content -> " + content.get(row).get(col));
                                Label grid = new Label(content.get(row).get(col));
                                grid.setWrapText(true);
                                grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
                                grid.setPadding(new Insets(5, 5, 5, 5));

                                tableGrid.add(grid, col, row);
                            }
                        }

                        contentElements.add(tableGrid);

                    }

                });

//        contentLayout.setSpacing(20);
//        contentLayout.getChildren().clear();
//        contentLayout.getChildren().addAll(contentElements);
//        contentLayout.setAlignment(Pos.CENTER);
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
