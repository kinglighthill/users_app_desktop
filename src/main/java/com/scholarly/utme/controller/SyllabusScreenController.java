package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SyllabusCategory;
import com.scholarly.utme.data.model.newDb.SyllabusTopic;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.SyllabusScreenVM;
import com.sun.speech.freetts.PathExtractorImpl;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@FxmlPath("/layouts/SyllabusScreen.fxml")
public class SyllabusScreenController implements FxmlView<SyllabusScreenVM>, Initializable {

    @FXML
    private VBox topicVBox;

    @FXML
    private Button syllabusBackButton;

    @FXML
    private Label subjectLabel, topicsLabel;

    @FXML
    private ImageView searchIcon, refreshIcon, settingsIcon, notesImage;

    @FXML
    private TabPane syllabusTabPane;

    @FXML
    private Tab topicsTab;

    @FXML
    private StackPane syllabusPane;

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
        });

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

        syllabusPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            syllabusTabPane.setTabMinWidth((Double) newValue/3.6);
        });

//        Line line = new Line(0, 0, 100, 0);
//        line.setStroke(Paint.valueOf("#000000"));
//        line.setStrokeWidth(5);
//        topicsTab.setGraphic(line);
        
    }

    private void initializeFonts() {
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        topicsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
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
