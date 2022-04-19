package com.scholarly.utme.controller;


import com.scholarly.utme.data.model.Highlights;
import com.scholarly.utme.data.model.Note;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.Section;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.Topic;
import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.ContentTypes;
import com.scholarly.utme.data.model.newDb.contentType.audio.AudioViewType;
import com.scholarly.utme.data.model.newDb.contentType.cbt.CBTViewType;
import com.scholarly.utme.data.model.newDb.contentType.html.HtmlViewType;
import com.scholarly.utme.data.model.newDb.contentType.image.ImageViewType;
import com.scholarly.utme.data.model.newDb.contentType.orderedList.OrderedListViewType;
import com.scholarly.utme.data.model.newDb.contentType.table.TableViewType;
import com.scholarly.utme.data.model.newDb.contentType.text.TextViewType;
import com.scholarly.utme.data.model.newDb.contentType.unorderedList.UnorderedListViewType;
import com.scholarly.utme.data.model.newDb.contentType.video.VideoViewType;
import com.scholarly.utme.data.model.newDb.contentType.webview.WebViewType;
import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.NotesScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


@FxmlPath("/layouts/NotesScreen.fxml")
public class NotesScreenController implements FxmlView<NotesScreenVM>, Initializable {

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
    private StackPane imageViewLayout, noteLayout;

    @FXML
    private VBox contentLayout, topicVBox, noteOptions, settingsPane, onHyperlinkClickedOverlay, noteOptionsReportNoteLayout, noteOptionsLayout, noteSettingsLayout, refreshNoteLayout, refreshStatusContent, refreshNoteTextContent, noteOptionsNoteLayout, dictionaryMeaningOverlay;

    @FXML
    private HBox highlightColors, addNoteButton, noteTopBar, noteSettingsCloseButton, noteOptionsCloseButton, toastLayout, reportOptionsCloseButton, refreshNotesCancelButton, onWordClickedOverlay, dictionaryCloseButton;

    @FXML
    private Label pageTitle, subjectLabel, topicLabel, topicTitle, addNoteText, bookmarkText, highlightHeader, refreshStatusFirstText, refreshStatusSecondText, toastText, newNoteText, dictionaryText;

    @FXML
    private ImageView notesImage, note_icon, bookmark_icon, closeIconImageViewLayout, closeIconNoteOptionLayout, imageViewLarge, settingsIcon, searchIcon, noteSettingsIcon, createNoteBackIcon, refreshStatusNoUpdateIcon, refreshIcon, refreshStatusUpdateFoundIcon, refreshStatusNoNetworkIcon;

    @FXML
    private ImageView noteOptionsNoteIcon, noteOptionsBookmarkIcon, noteOptionsShareIcon, noteOptionsReportIcon, noteOptionsAudioIcon, reportOptionReportIcon, notClearIcon, aLittleClearIcon, veryClearIcon, feedbackIcon, dictionarySpeakerIcon, dictionaryIcon;

    @FXML
    private Button backButton, prevButton, nextButton, practiceTopicButton, quizButton, noteCloseButton, noteSaveButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private ProgressIndicator refreshNoteProgressIndicator;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";


    private Section selectedSection;

    private int defaultFontSize = 16;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchTextField.requestFocus();
        highlightColorsList.addAll(Arrays.stream(HighlightColors.values()).map(highlightColors1 -> highlightColors1.colorCode).collect(Collectors.toList()));

        imageViewLayout.setVisible(false);
        imageViewLayout.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.web("#000000", 0.8), null, null)}, null));

        viewModel.initialize(getInitialData());

        ToggleGroup topicListToggleGroup = new ToggleGroup();

        //topicLabel.setText(viewModel.getTopic().getTitle());

        searchIcon.setImage(new Image(getClass().getResource("/drawable/note_search_icon_1.5x.png").toString()));
        refreshIcon.setImage(new Image(getClass().getResource("/drawable/note_refresh_icon_1.5x.png").toString()));
        noteSettingsIcon.setImage(new Image(getClass().getResource("/drawable/note_settings_icon_2x.png").toString()));
        refreshStatusNoUpdateIcon.setImage(new Image(getClass().getResource("/drawable/no_update_found_icon_1x.png").toString()));
        refreshStatusUpdateFoundIcon.setImage(new Image(getClass().getResource("/drawable/updates_found_icon_1x.png").toString()));
        refreshStatusNoNetworkIcon.setImage(new Image(getClass().getResource("/drawable/no_network_icon_1x.png").toString()));


        /***************** Refresh Notes Section *******************/
        refreshIcon.setOnMouseClicked((event -> {
            if (!refreshNoteLayout.isVisible()) {
                refreshStatusContent.getChildren().remove(refreshNoteProgressIndicator);
                refreshStatusContent.getChildren().remove(refreshStatusNoUpdateIcon);
                refreshStatusContent.getChildren().remove(refreshStatusUpdateFoundIcon);
                refreshStatusFirstText.setText("Network Unavailable");
                refreshStatusFirstText.setTextFill(Paint.valueOf("#EE8989"));
                refreshStatusFirstText.setPadding(new Insets(10, 0, 0, 0));
                refreshStatusSecondText.setText("Connect your phone and try again");
                //refreshStatusSecondText.setTextFill(Paint.valueOf("#51C46B"));
                //refreshNoteTextContent.getChildren().remove(refreshStatusSecondText);
                Animations.translateIn(refreshNoteLayout, 200);
            }

        }));
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
        noteSettingsIcon.setOnMouseClicked(event -> {
            if (!noteSettingsLayout.isVisible()) {
                Animations.translateIn(noteSettingsLayout, 200);
            }

            // TODO: Use ListActionView to populate the Font Dropdown
            int stackItems = noteLayout.getChildren().size();
            System.out.println("StackPane Items -> " + stackItems);

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

        contentLayout.setOnMouseClicked(event -> {
            if (!onWordClickedOverlay.isVisible() && !dictionaryMeaningOverlay.isVisible()) {
                onWordClickedOverlay.setVisible(true);
            }
            /*if (!noteOptionsLayout.isVisible()) {
                Animations.translateIn(noteOptionsLayout);
            }*/
        });

        viewModel.getSubTopics().forEach(subTopic -> {
            ToggleButton button = new ToggleButton();
            button.setUserData(subTopic);
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

                    viewModel.setSelectedSubTopic(subTopic);
                    renderNote(subTopic);
//                    topicLabel.setText(subTopic.getTitle());
                } else {
                    button.setStyle(IDLE_BUTTON_STYLE);
                    button.setTextFill(Color.BLACK);

//                    viewModel.setSelectedTopic(null);
                }
            });

            if (subTopic == viewModel.getSelectedSubTopic()) {
                topicListToggleGroup.selectToggle(button);
            }

            button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 12));

            topicVBox.getChildren().add(button);
        });


        highlightColors.getChildren().clear();
        highlightColors.getChildren().addAll(
                highlightColorsList
                        .stream()
                        .map(colorCode -> {
                            Circle circle = new Circle();

                            circle.setFill(Color.web(colorCode));

                            circle.setRadius(15);

                            circle.setOnMouseClicked(event -> {
                                viewModel.handleHighlight(selectedSection, colorCode);
                                hideNoteOptions();
                            });

                            return circle;
                        })
                        .collect(Collectors.toList())
        );

        viewModel.getSubjectHighlights().addListener((ListChangeListener<? super Highlights>) c -> {
            System.out.println("Highlight list changed");
            renderNote(viewModel.getSelectedSubTopic());
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
      
        closeIconImageViewLayout.setImage(new Image(getClass().getResource("/drawable/close_icon_white.png").toString()));
        closeIconImageViewLayout.setOnMouseClicked(event -> {
            hideImageViewLayout();
        });

        closeIconNoteOptionLayout.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        closeIconNoteOptionLayout.setOnMouseClicked(event -> {
            hideNoteOptions();
        });

//        backButton.setGraphic(view);

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
//        backButton.setBackground(Background.EMPTY);

//        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        //topicLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        topicTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 16));
        addNoteText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        bookmarkText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        highlightHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 14));
        quizButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        newNoteText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));

        //practiceTopicButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

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
                viewModel.addNote(selectedSection, note);
            });

            hideNoteOptions();
        });;

    }

    private void renderNote(SubTopic subTopic) {
        List<Node> contentElements = new ArrayList<>();


        viewModel.getSubTopicSections()
                .get(subTopic.getId())
                .forEach(section -> {

                    ContentType contentType = ContentTypes.convert(section);
                    if (contentType instanceof TextViewType) {
                        TextViewType textViewType = (TextViewType) contentType;

                        if (textViewType.getTitle() != null) {
                            contentElements.add(getTitle(textViewType.getTitle().getText()));
                        }

                        Label body = new Label();
                        body.setText(textViewType.getBody().getText());
                        body.setWrapText(true);
                        body.setLineSpacing(8);
                        body.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 17));



                        List<Highlights> highlights = viewModel.getSubjectHighlights();
                        for (int i = 0; i < highlights.size(); i ++) {
                            if (highlights.get(i).getNoteId() == section.getId()) {
                                body.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.web(highlights.get(i).getColor()), null, null)}, null));
                            }
                        }
                        body.setOnMouseClicked(event -> {
                            if (!noteOptions.isVisible()) {
                                showNoteOptions(section);
                            } else {
                                hideNoteOptions();
                            }
                        });

                        contentElements.add(body);


                    }
                    else if (contentType instanceof HtmlViewType) {
                        HtmlViewType viewType = (HtmlViewType) contentType;

                        if (viewType.getTitle() != null) {

                            WebView webView = new WebView();
                            webView.getEngine().loadContent(viewType.getBody().getText());

                            contentElements.add(webView);
                        }
                        WebView webView = new WebView();
                        webView.getEngine().loadContent(viewType.getBody().getText());

                        contentElements.add(webView);

                    }
                    else if (contentType instanceof WebViewType) {
                        WebViewType viewType = (WebViewType) contentType;

                        if (viewType.getTitle() != null) {
                            contentElements.add(getTitle(viewType.getTitle().getText()));
                        }
                        WebView webView = new WebView();
                        webView.getEngine().loadContent(viewType.getBody().getText());

                        contentElements.add(webView);

                    }
                    else if (contentType instanceof ImageViewType) {
                        ImageViewType imageViewType = (ImageViewType) contentType;

                        ImageView image = new ImageView(new Image(getClass().getResource("/drawable/dummy_image.jpg").toString()));
                        image.setFitHeight(240);
                        image.setPreserveRatio(true);

                        contentElements.add(image);

                    }
                    else if (contentType instanceof CBTViewType) {

                    }
                    else if (contentType instanceof OrderedListViewType) {
                        OrderedListViewType viewType = (OrderedListViewType) contentType;

                        if (viewType.getTitle() != null) {
                            contentElements.add(getTitle(viewType.getTitle().getText()));
                        }

                        ListView<String> contentList = new ListView<>();

                        contentList.setItems(FXCollections.observableArrayList(Arrays.asList(viewType.getBody().getList())));

                        contentElements.add(contentList);

                    }
                    else if (contentType instanceof UnorderedListViewType) {

                        UnorderedListViewType viewType = (UnorderedListViewType) contentType;

                        if (viewType.getTitle() != null) {
                            contentElements.add(getTitle(viewType.getTitle().getText()));
                        }

                        ListView<String> contentList = new ListView<>();

                        contentList.setItems(FXCollections.observableArrayList(Arrays.asList(viewType.getBody().getList())));

                        contentElements.add(contentList);

                    }
                    else if (contentType instanceof TableViewType) {
                        TableViewType viewType = (TableViewType) contentType;

                        TableView tableView = new TableView();

                        for (int i = 0; i < viewType.getBody().getHeader().length; i++) {
                            TableColumn<Map, String> column = new TableColumn<>(viewType.getBody().getHeader()[i]);
                            column.setCellValueFactory(new MapValueFactory<>(i));

                            tableView.getColumns().add(column);
                        }

                        for (int i = 0; i < viewType.getBody().getRows().length; i++) {
                            Map<Integer, Object> item = new HashMap<>();
                            for (int j = 0; j < viewType.getBody().getRows()[i].length; j++) {
                                item.put(j, viewType.getBody().getRows()[i][j]);
                            }

                            tableView.getItems().add(item);
                        }

                        contentElements.add(tableView);

                    }
                    else if (contentType instanceof AudioViewType) {

                    } else if (contentType instanceof VideoViewType) {

                    }
                });


        contentLayout.getChildren().clear();
        contentLayout.getChildren().addAll(contentElements);
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

    private void showImageViewLayout(Image image) {

        imageViewLayout.setVisible(true);
        imageViewLarge.setImage(image);

        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        fadeTransition.setDuration(Duration.millis(300));

        fadeTransition.setNode(imageViewLayout);

        fadeTransition.play();
    }
    private void hideImageViewLayout() {

        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setDuration(Duration.millis(300));

        fadeTransition.setNode(imageViewLayout);

        fadeTransition.setOnFinished(event -> {
            imageViewLayout.setVisible(false);
        });
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
        System.out.println("Got data -> " + data);
        return data;
    }

    public static class InitialData {
        private Subject subject;
        private Topic topic;
        private List<SubTopic> subTopics;
        private SubTopic selectedSubTopic;

        public InitialData(Subject subject, Topic topic, List<SubTopic> subTopics, SubTopic selectedSubTopic) {
            this.subject = subject;
            this.topic = topic;
            this.subTopics = subTopics;
            this.selectedSubTopic = selectedSubTopic;
        }

        public Subject getSubject() {
            return subject;
        }

        public Topic getTopic() {
            return topic;
        }

        public List<SubTopic> getSubTopics() {
            return subTopics;
        }

        public SubTopic getSelectedSubTopic() {
            return selectedSubTopic;
        }
    }
}
