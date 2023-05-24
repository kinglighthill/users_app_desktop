package com.scholarly.utme.controller.syllabus_screens;

import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentViewType.HeaderViewType;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.syllabus_screens.SelectSyllabusVM;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/syllabus_screens/SelectSyllabusScreen.fxml")
public class SelectSyllabusController implements FxmlView<SelectSyllabusVM>, Initializable {
    private static final String TAG = "SelectSyllabusController: ";

    @InjectViewModel
    private SelectSyllabusVM viewModel;

    @FXML
    private Button backButton, viewSyllabusButton;

    @FXML
    private Label pageTitle, subjectsTitle, categoriesTitle, emptySyllabusListLabel;

    @FXML
    private VBox subjectListVBox, categoryListVBox, tabTopicsVBox;

    @FXML
    private Tab topicsTab, genObjectiveTab, recTextsTab;

    @FXML
    private TabPane syllabusTabPane;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        ToggleGroup subjectListToggleGroup = new ToggleGroup();
        subjectListToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SyllabusSubject selectedSubject = (SyllabusSubject) newValue.getUserData();
                viewModel.setSelectedSyllabusSubject(selectedSubject);
                renderObjectives();
                renderRecommendedTexts();
                emptySyllabusListLabel.setVisible(false);
            } else {
                viewModel.setSelectedSyllabusSubject(null);
                emptySyllabusListLabel.setVisible(true);
                recTextsTab.setContent(null);
                genObjectiveTab.setContent(null);
            }
        });

        viewModel.getSyllabusSubjects().forEach(syllabusSubject -> {

            ToggleButton button = new ToggleButton();
            button.setUserData(syllabusSubject);
            subjectListToggleGroup.getToggles().add(button);

            button.setMinHeight(70);
            button.setMaxHeight(70);
            button.setPadding(new Insets(0, 0, 0, 20));
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setText(syllabusSubject. getTitle());

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

       /* ToggleGroup topicListToggleGroup = new ToggleGroup();
        topicListToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                Pair<SyllabusSubject, SyllabusCategory> userData = (Pair<SyllabusSubject, SyllabusCategory>) newValue.getUserData();
                SyllabusSubject syllabusSubject = userData.getKey();
                SyllabusCategory selectedCategory = userData.getValue();
                viewModel.setSelectedSyllabusSubject(syllabusSubject);
                viewModel.setSelectedCategory(selectedCategory);

                if (!viewSyllabusButton.isVisible()) {
                    Animations.translateIn(viewSyllabusButton, 300);
                }

                System.out.println("Selected Category Title -> " + selectedCategory.getTitle());
//                System.out.println("Selected SyllabusTopic Section ID -> " + selectedTopic.getSectionId());

            } else {
                Animations.translateOut(viewSyllabusButton, 300);
            }

        });*/


        viewModel.selectedSyllabusSubjectProperty().addListener(((observable, oldValue, newValue) -> {
            tabTopicsVBox.getChildren().clear();

            if (newValue != null) {

                viewModel.getCategories().forEach(syllabusCategory -> {

                    if (syllabusCategory.getSubjectId() == newValue.getId()) {
                        VBox categoryContent = new VBox(5.0);

                        Label categoryTitle = new Label(syllabusCategory.getTitle());
                        categoryTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                        categoryContent.getChildren().add(categoryTitle);

                        viewModel.getSyllabusTopics().get(syllabusCategory.getId()).forEach(topic -> {

                            viewModel.getSyllabusSections().get(syllabusCategory.getSubjectId()).forEach(section -> {
                                if (topic.getSectionId() == section.getId()) {
                                    System.out.println("Got Section -- " + section.getContent());
                                    ContentViewType contentViewType = ContentViewTypes.convert(section);
                                    HeaderViewType headerViewType = (HeaderViewType) contentViewType;

                                    Label topicHeader = new Label(headerViewType.getText());
                                    topicHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

                                    categoryContent.getChildren().add(topicHeader);
                                }
                            });

                        });
                        tabTopicsVBox.getChildren().add(categoryContent);
                        tabTopicsVBox.setBackground(Background.EMPTY);
                    }

                });

            }

        }));

        /*viewSyllabusButton.setOnAction(event -> {
            SyllabusScreenController.InitialData data = new SyllabusScreenController.InitialData(
                    viewModel.getSelectedSyllabusSubject(),
                    viewModel.getSelectedCategory(),
                    viewModel.getSyllabusCategories().get(
                            viewModel.getSelectedSyllabusSubject().getSubjectId()
                    ),
                    viewModel.getSyllabusTopics().get(
                            viewModel.getSelectedCategory().getId()
                    ).stream().filter(syllabusTopic -> syllabusTopic.getCategoryId() == viewModel.getSelectedCategory().getId()).collect(Collectors.toList())
            );

            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.SYLLABUS_SCREEN);

        });*/
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        backIcon.setFitHeight(25);
        backIcon.setPreserveRatio(true);
        backButton.setGraphic(backIcon);
        backButton.setBackground(Background.EMPTY);

        syllabusTabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            syllabusTabPane.setTabMinWidth((Double) newValue/3);
            syllabusTabPane.setTabMaxWidth((Double) newValue/3);
        });

    }

    private void initializeFonts() {
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
//        categoriesTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        emptySyllabusListLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
//        viewSyllabusButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));

    }

    private void renderObjectives() {
        Document doc = Jsoup.parse(viewModel.getSelectedSyllabusSubject().getGeneralObjectives());
        String formattedText = doc.body().text();
        Label objectivesLabel = new Label(formattedText);
        objectivesLabel.setWrapText(true);
        objectivesLabel.setTextAlignment(TextAlignment.JUSTIFY);
        objectivesLabel.setPadding(new Insets(20, 15, 0, 15));
        objectivesLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

        genObjectiveTab.setContent(objectivesLabel);
        genObjectiveTab.getContent().autosize();
        topicsTab.getContent().autosize();
    }

    private void renderRecommendedTexts() {
        Document doc = Jsoup.parse(viewModel.getSelectedSyllabusSubject().getRecommendedTexts());
        String formattedText = doc.body().text();
        Label recTextLabel = new Label(formattedText);
        recTextLabel.setWrapText(true);
        recTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        recTextLabel.setPadding(new Insets(20, 15, 0, 15));
        recTextLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

        recTextsTab.setContent(recTextLabel);
        recTextsTab.getContent().autosize();
        topicsTab.getContent().autosize();
    }

    public void backButtonClicked(MouseEvent event) {
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }

}
