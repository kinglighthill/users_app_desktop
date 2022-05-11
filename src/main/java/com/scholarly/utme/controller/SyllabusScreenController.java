package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.SyllabusCategory;
import com.scholarly.utme.data.model.newDb.SyllabusTopic;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.SyllabusScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/SyllabusScreen.fxml")
public class SyllabusScreenController implements FxmlView<SyllabusScreenVM>, Initializable {

    @FXML
    private VBox topicVBox, tabTopicsVBox;

    @FXML
    private Button syllabusBackButton;

    @FXML
    private Label subjectLabel, topicsLabel, topicItem;

    @FXML
    private ImageView searchIcon, refreshIcon, settingsIcon, notesImage;

    @FXML
    private TabPane syllabusTabPane;

    @FXML
    private Tab topicsTab, genObjectiveTab, recTextsTab;

    @FXML
    private StackPane syllabusPane;

    @FXML
    private ScrollPane syllabusScrollPane;

    @InjectViewModel
    private SyllabusScreenVM viewModel;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewModel.processInitialData(getInitialData());

        initializeViews();

        initializeFonts();

        ToggleGroup topicListToggleGroup = new ToggleGroup();

        viewModel.getTopics().forEach(topic -> {
            ToggleButton button = new ToggleButton();
            button.setUserData(topic);
            button.setMinHeight(48);
            button.setMaxHeight(48);
            button.setPadding(new Insets(0, 0, 0, 20));
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setText(topic.getTitle());
            button.setStyle(IDLE_BUTTON_STYLE);
            topicListToggleGroup.getToggles().add(button);

            button.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    viewModel.setSelectedTopic(topic);

                    button.setStyle(PRESSED_STYLE);
                    button.setTextFill(Color.WHITE);
                }else {
                    button.setStyle(IDLE_BUTTON_STYLE);
                    button.setTextFill(Color.BLACK);
                }

            }));

            if (topic == viewModel.getSelectedTopic()) {
                topicListToggleGroup.selectToggle(button);
            }

            topicVBox.getChildren().add(button);

            Label topicItem = new Label();
            topicItem.setText(topic.getTitle());
            topicItem.setMinHeight(50);

            topicItem.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
            topicItem.setPadding(new Insets(0, 0, 0, 10));
            Separator separator = new Separator();
            tabTopicsVBox.getChildren().addAll(topicItem, separator);

            viewModel.getSubtopics().forEach((s, subTopics) -> {
               subTopics.stream().filter(subTopic -> subTopic.getTopicId() == topic.getId()).forEach(topicSubtopic -> {
                   Label subtopicItem = new Label(topicSubtopic.getTitle());
                   subtopicItem.setPadding(new Insets(10, 0, 10, 10));
                   subtopicItem.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, FontUtil.FontSize.FOURTEEN.size));
                   Separator separator2 = new Separator();

                   tabTopicsVBox.getChildren().addAll(subtopicItem, separator2);
               });

            });

        });

        topicsTab.setContent(tabTopicsVBox);

        setupObjectivesTab();

        setupRecTextsTab();

        syllabusBackButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.SELECT_SYLLABUS_SCREEN);
        });
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/notes_back_icon_2x.png").toString()));
        backIcon.setFitWidth(20);
        backIcon.setFitHeight(20);
        backIcon.setPreserveRatio(true);
        backIcon.setPickOnBounds(true);
        syllabusBackButton.setBackground(Background.EMPTY);
        syllabusBackButton.setGraphic(backIcon);

        searchIcon.setImage(new Image(getClass().getResource("/drawable/note_search_icon_1.5x.png").toString()));
        refreshIcon.setImage(new Image(getClass().getResource("/drawable/note_refresh_icon_1.5x.png").toString()));
        settingsIcon.setImage(new Image(getClass().getResource("/drawable/note_settings_icon_2x.png").toString()));
        notesImage.setImage(new Image(getClass().getResource("/drawable/notes.png").toString()));

        syllabusTabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            syllabusTabPane.setTabMinWidth((Double) newValue/3.24);
        });
        
    }

    private void initializeFonts() {
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        topicsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
    }

    private void setupObjectivesTab() {
        String generalObjectives = viewModel.getSyllabusSubject().getGeneralObjectives();
        Label objectivesLabel = new Label(generalObjectives);
        objectivesLabel.setWrapText(true);
        objectivesLabel.setTextAlignment(TextAlignment.JUSTIFY);
        objectivesLabel.setPadding(new Insets(20, 15, 0, 15));
        objectivesLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));

        genObjectiveTab.setContent(objectivesLabel);
        genObjectiveTab.getContent().autosize();
    }

    private void setupRecTextsTab() {
        String recommendedText = viewModel.getSyllabusSubject().getRecommendedTexts();
        Label recTextLabel = new Label(recommendedText);
        recTextLabel.setWrapText(true);
        recTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        recTextLabel.setPadding(new Insets(20, 15, 0, 15));
        recTextLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));

//        recTextLabel.heightProperty().addListener(((observable, oldValue, newValue) -> {
//            System.out.println("Rec text label height -> " + newValue.doubleValue());
//            syllabusPane.setPrefHeight((Double) newValue);
//        }));

        recTextsTab.setContent(recTextLabel);
        recTextsTab.getContent().autosize();
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data with subject -> " + data.getSubject().getSubjectName() + " and topic -> " + data.getSelectedSyllabusTopic().getTitle());
        return data;
    }


    public static class InitialData {
        private Subject subject;
        private SyllabusCategory category;
        private List<SyllabusTopic> syllabusTopics;
        private SyllabusTopic selectedSyllabusTopic;

        public InitialData(Subject subject, SyllabusCategory category, List<SyllabusTopic> syllabusTopics, SyllabusTopic selectedSyllabusTopic) {
            this.subject = subject;
            this.category = category;
            this.syllabusTopics = syllabusTopics;
            this.selectedSyllabusTopic = selectedSyllabusTopic;
        }

        public Subject getSubject() {
            return subject;
        }

        public SyllabusCategory getCategory() {
            return category;
        }

        public List<SyllabusTopic> getSyllabusTopics() {
            return syllabusTopics;
        }

        public SyllabusTopic getSelectedSyllabusTopic() {
            return selectedSyllabusTopic;
        }
    }
}
