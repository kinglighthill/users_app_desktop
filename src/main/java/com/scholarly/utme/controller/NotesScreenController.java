package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.*;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.NotesScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
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
import javafx.scene.shape.Circle;
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
    private StackPane imageViewLayout;



    @FXML
    private VBox contentLayout, topicVBox, noteOptions;

    @FXML
    private HBox highlightColors, addNoteButton;

    @FXML
    private Label pageTitle, subjectLabel, topicLabel, topicTitle, addNoteText, bookmarkText, highlightHeader;

    @FXML
    private ImageView notesImage, note_icon, bookmark_icon, closeIconImageViewLayout, closeIconNoteOptionLayout, imageViewLarge, settingsIcon;

    @FXML
    private Button backButton, prevButton, nextButton, practiceTopicButton;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";


    private SubSection selectedSubSection;

    private int defaultFontSize = 16;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        highlightColorsList.addAll(Arrays.stream(HighlightColors.values()).map(highlightColors1 -> highlightColors1.colorCode).collect(Collectors.toList()));

        imageViewLayout.setVisible(false);
        imageViewLayout.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.web("#000000", 0.8), null, null)}, null));

        viewModel.initialize(getInitialData());

        ToggleGroup topicListToggleGroup = new ToggleGroup();

        viewModel.getTopics().forEach(topic -> {
            ToggleButton button = new ToggleButton();
            button.setUserData(topic);
            topicListToggleGroup.getToggles().add(button);


            button.setMinHeight(48);
            button.setMaxHeight(48);
            button.setPadding(new Insets(0, 0, 0, 20));
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setText(topic.getTitle());

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

                    viewModel.setSelectedTopic(topic);
                    renderNote(topic);
                    topicLabel.setText(topic.getTitle());
                } else {
                    button.setStyle(IDLE_BUTTON_STYLE);
                    button.setTextFill(Color.BLACK);

//                    viewModel.setSelectedTopic(null);
                }
            });

            if (topic == viewModel.getSelectedTopic()) {
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
                                viewModel.handleHighlight(selectedSubSection, colorCode);
                                hideNoteOptions();
                            });

                            return circle;
                        })
                        .collect(Collectors.toList())
        );

        viewModel.getSectionsHighlights().forEach((integer, highlights) -> {
            highlights.addListener((ListChangeListener<? super Highlights>) c -> {
                System.out.println("Highlight list changed");
                Section highlightSection = null;
                for(int i = 0; i < viewModel.getSections().size(); i++) {
                    if (integer == viewModel.getSections().get(i).getId()) {
                        highlightSection = viewModel.getSections().get(i);
                    }
                }
                if (highlightSection.getTopicId() == viewModel.getSelectedTopic().getId()) {
                    renderNote(viewModel.getSelectedTopic());
                }
            });
        });

        notesImage.setImage(new Image(getClass().getResource("/drawable/notes.png").toString()));
        note_icon.setImage(new Image(getClass().getResource("/drawable/note_icon.png").toString()));
        bookmark_icon.setImage(new Image(getClass().getResource("/drawable/bookmark_stroke.png").toString()));

        ImageView view = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        view.setFitHeight(25);
        view.setPreserveRatio(true);

        settingsIcon.setImage(new Image(getClass().getResource("/drawable/preferences.png").toString()));
        settingsIcon.setOnMouseClicked(event -> {
            Dialog<Double> dialog = createSettingsDialog(defaultFontSize);
            Optional<Double> result = dialog.showAndWait();

            if (result.isPresent()) {
                defaultFontSize = result.get().intValue();
                renderNote(viewModel.getSelectedTopic());
            }

        });

        closeIconImageViewLayout.setImage(new Image(getClass().getResource("/drawable/close_icon_white.png").toString()));
        closeIconImageViewLayout.setOnMouseClicked(event -> {
            hideImageViewLayout();
        });

        closeIconNoteOptionLayout.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        closeIconNoteOptionLayout.setOnMouseClicked(event -> {
            hideNoteOptions();
        });

        backButton.setGraphic(view);

        ImageView nextView = new ImageView(new Image(getClass().getResource("/drawable/next_icon.png").toString()));
        nextView.setFitHeight(20);
        nextView.setPreserveRatio(true);

        nextButton.setGraphic(nextView);

        ImageView prevView = new ImageView(new Image(getClass().getResource("/drawable/prev_icon.png").toString()));
        prevView.setFitHeight(20);
        prevView.setPreserveRatio(true);

        prevButton.setGraphic(prevView);
        backButton.setBackground(Background.EMPTY);

        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        topicLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        topicTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 16));
        addNoteText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        bookmarkText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        highlightHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 14));

        practiceTopicButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

        prevButton.setOnAction(event -> {
//            if (topicList.getSelectionModel().getSelectedIndex() > 0) {
//                topicList.getSelectionModel().select(topicList.getSelectionModel().getSelectedIndex() - 1);
//            }

            int currentIndex = viewModel.getTopics().indexOf(((Topic) topicListToggleGroup.getSelectedToggle().getUserData()));
            if (currentIndex > 0) {
                topicListToggleGroup.getToggles().forEach(toggle -> {
                    if (toggle.getUserData() == viewModel.getTopics().get(currentIndex - 1)) {
                        topicListToggleGroup.selectToggle(toggle);
                    }
                });
            }
        });

        nextButton.setOnAction(event -> {
//            if (topicList.getSelectionModel().getSelectedIndex() < topicList.getItems().size() - 1) {
//                topicList.getSelectionModel().select(topicList.getSelectionModel().getSelectedIndex() + 1);
//            }
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });

        addNoteButton.setOnMouseClicked(event -> {

            List<Note> notes = viewModel.getSectionsNotes().get(selectedSubSection.getSectionId());

            String currentNote = null;

            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getNoteId() == selectedSubSection.getId()) {
                    currentNote = notes.get(i).getNote();
                    break;
                }
            }

            Dialog<String> noteDialog = createNoteDialog(currentNote);

            Optional<String> result = noteDialog.showAndWait();

            result.ifPresent(note -> {
                viewModel.addNote(selectedSubSection, note);
            });

            hideNoteOptions();
        });

    }

    private void renderNote(Topic topic) {
        List<Node> contentElements = new ArrayList<>();


        contentElements.addAll(
                viewModel.getSections()
                        .stream()
                        .filter(section -> section.getTopicId() == topic.getId())
                        .map(section -> {
                            if (section.getContent() != null) {
                                Label header = new Label();
                                header.setText(section.getContent());
                                header.setWrapText(true);
                                header.setUnderline(true);
                                header.setLineSpacing(8);
                                header.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));

                                return header;
                            } else {
                                ImageView image = new ImageView(new Image(getClass().getResource("/drawable/dummy_image.jpg").toString()));
                                image.setFitHeight(240);
                                image.setPreserveRatio(true);

                                return image;
                            }
                        })
                        .collect(Collectors.toList())
        );



        for (int i = 0; i < viewModel.getSections().size(); i++) {
            Section section = viewModel.getSections().get(i);
            if (section.getTopicId() == topic.getId()) {
                List<Highlights> highlights = viewModel.getSectionsHighlights().get(section.getId());
                contentElements.addAll(
                        viewModel.getSectionsSubSections().get(section.getId())
                                .stream()
                                .map(subSection -> {
                                    if (subSection.getContent() != null) {
                                        Label header = new Label();
                                        header.setUserData(subSection);
                                        header.setText(subSection.getContent());
                                        header.setWrapText(true);
                                        header.setLineSpacing(5);
                                        header.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, defaultFontSize));

                                        for (int j = 0; j < highlights.size(); j ++) {
                                            if (highlights.get(j).getNoteId() == subSection.getId()) {
                                                header.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.web(highlights.get(j).getColor()), null, null)}, null));
                                            }
                                        }
                                        header.setOnMouseClicked(event -> {
                                            if (!noteOptions.isVisible()) {
                                                showNoteOptions(subSection);
                                            } else {
                                                hideNoteOptions();
                                            }
                                        });
                                        return header;
                                    } else {
                                        Image image = new Image(getClass().getResource("/drawable/dummy_image.jpg").toString());
                                        ImageView imageView = new ImageView(image);
                                        imageView.setFitHeight(240);
                                        imageView.setPreserveRatio(true);

                                        imageView.setImage(image);
                                        imageView.setOnMouseClicked(event -> {
                                            showImageViewLayout(image);
                                        });
                                        return imageView;
                                    }
                                })
                                .collect(Collectors.toList())
                );
            }
        }


        contentLayout.getChildren().clear();
        contentLayout.getChildren().addAll(contentElements);
    }

    private void showNoteOptions(SubSection subSection) {
        selectedSubSection = subSection;

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
        selectedSubSection = null;

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
        private List<Topic> topics;
        private Topic selectedTopic;

        public InitialData(Subject subject, List<Topic> topics, Topic selectedTopic) {
            this.subject = subject;
            this.topics = topics;
            this.selectedTopic = selectedTopic;
        }

        public Subject getSubject() {
            return subject;
        }

        public Topic getSelectedTopic() {
            return selectedTopic;
        }

        public List<Topic> getTopics() {
            return topics;
        }
    }
}
