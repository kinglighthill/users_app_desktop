package com.scholarly.controller.syllabus_screens;

import com.scholarly.MainApplication;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.data.model.newDb.*;
import com.scholarly.data.model.newDb.contentType.ContentViewType;
import com.scholarly.data.model.newDb.contentViewType.HeaderViewType;
import com.scholarly.ui.utils.*;
import com.scholarly.viewmodels.syllabus_screens.SelectSyllabusVM;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@FxmlPath("/layouts/syllabus_screens/SelectSyllabusScreen.fxml")
public class SelectSyllabusController implements FxmlView<SelectSyllabusVM>, Initializable {
    private static final String TAG = "SelectSyllabusController: ";

    @InjectViewModel
    private SelectSyllabusVM viewModel;

    @FXML
    private Button backButton, viewSyllabusButton;

    @FXML
    private Label pageTitle, subjectsTitle, objectivesLabel, recTextLabel, emptySyllabusListLabel, topicTitle, objectivesTab, contentTab, evaluationTab, sectionContent, emptySectionText;

    @FXML
    private ImageView sectionLayoutCloseIcon, emptySectionMouseIcon;

    @FXML
    private VBox subjectListVBox, categoryListVBox, tabTopicsVBox, sectionPane;

    @FXML
    private HBox topicTitleBox;

    @FXML
    private Tab topicsTab, genObjectiveTab, recTextsTab;

    @FXML
    private TabPane syllabusTabPane, sectionTabPane;

    @FXML
    private ProgressIndicator progressBar;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    final String PRESSED_STYLE2 = "-fx-background-color: #EDEDED; -fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";
    final String HOVERED_BUTTON_STYLE2 = "-fx-background-color: #F7F7F7; -fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeFonts();

        ToggleGroup subjectListToggleGroup = new ToggleGroup();

        viewModel.getSubjectsLoaded().addListener((mObservable, mOldValue, mNewValue) -> {
            viewModel.getSyllabusSubjects().forEach(syllabusSubject -> {
                ToggleButton button = new ToggleButton();
                button.setUserData(syllabusSubject);
                subjectListToggleGroup.getToggles().add(button);

                button.setMinHeight(70);
                button.setMaxHeight(70);
                button.setPadding(new Insets(0, 0, 0, 20));
                button.setAlignment(Pos.BASELINE_LEFT);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setText(syllabusSubject.getTitle());
                button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

                ImageView graphic = new ImageView(new Image(getClass().getResource("/drawable/subject_images/" + syllabusSubject.getShortTitle() + "_image.png").toString()));
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
                    } else {
                        button.setStyle(IDLE_BUTTON_STYLE);
                    }
                });

                subjectListVBox.getChildren().add(button);
            });

            subjectListToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                MainApplication.resetTime();
                if (newValue != null) {
                    MainApplication.timeTakenTo("start selecting subject");
                    SyllabusSubject selectedSubject = (SyllabusSubject) newValue.getUserData();

                    if (topicsTab.isSelected()) {
                        showProgressBar();
                    } else  {
                        topicsTab.setDisable(true);
                    }

                    emptySyllabusListLabel.setVisible(false);

                    viewModel.setSelectedSyllabusSubject(selectedSubject);
                    MainApplication.timeTakenTo("done selecting subject");
                } else {
                    viewModel.setSelectedSyllabusSubject(null);
                    emptySyllabusListLabel.setVisible(true);
                    recTextsTab.setContent(null);
                    genObjectiveTab.setContent(null);
                }
            });

            viewModel.selectedSyllabusSubjectProperty().addListener(((observable, oldValue, newValue) -> {
                MainApplication.timeTakenTo("start loading syllabus");
                tabTopicsVBox.getChildren().clear();
                if (sectionPane.isVisible()) {
                    Animations.slideOut(sectionPane);
                    emptySectionText.setVisible(true);
                }

                if (newValue != null) {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                    Task<List<Node>> sectionsTask = new Task<>() {
                        @Override
                        protected List<Node> call() {
                            List<Node> sectionNodes = new ArrayList<>();
                            viewModel.getCategories().forEach(syllabusCategory -> {
                                if (syllabusCategory.getSubjectId() == newValue.getId()) {
                                    VBox categoryContent = new VBox(5.0);

                                    Label categoryTitle = new Label(syllabusCategory.getTitle());
                                    categoryTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 19));
                                    categoryContent.getChildren().add(categoryTitle);

                                    viewModel.getSyllabusTopics().get(syllabusCategory.getId()).forEach(topic -> {
                                        HBox topicBottomBox = new HBox(35.0);
                                        SyllabusSection currentSection = null;

                                        for (int i = 0; i < viewModel.getSyllabusSections().get(syllabusCategory.getSubjectId()).size(); i++) {
                                            VBox topicsVBox = new VBox(5.0);

                                            SyllabusSection section = viewModel.getSyllabusSections().get(syllabusCategory.getSubjectId()).get(i);

                                            if (section.getId() == topic.getSectionId()) {
                                                currentSection = section;
                                                ContentViewType contentViewType = ContentViewTypes.convert(section);
                                                HeaderViewType headerViewType = (HeaderViewType) contentViewType;

                                                Label topicHeader = new Label();
                                                topicHeader.setText(i+1 + ". " + headerViewType.getText());
                                                topicHeader.setWrapText(true);
                                                topicHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

                                                topicsVBox.getChildren().add(topicHeader);

                                                categoryContent.getChildren().add(topicsVBox);
                                            }

                                            if (section.getParentSectionId() == topic.getSectionId()) {
                                                ContentViewType sectionContentViewType = ContentViewTypes.convert(section);
                                                HeaderViewType sectionViewType = (HeaderViewType) sectionContentViewType;

                                                String index = numberToAlphabet(section.getChildSectionOrder(), false);

                                                Label topicContent = new Label();
                                                topicContent.setText("   " + index + ". " + sectionViewType.getText());
                                                topicContent.setWrapText(true);
                                                topicContent.setTextAlignment(TextAlignment.JUSTIFY);
                                                topicContent.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, 14));

                                                topicsVBox.getChildren().add(topicContent);

                                                categoryContent.getChildren().add(topicsVBox);
                                            }
                                        }

                                        Label objectives = new Label("Objectives");
                                        objectives.setTextFill(Paint.valueOf("#4081FF"));
                                        objectives.setStyle("-fx-cursor: hand;");
                                        objectives.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, 16));
                                        SyllabusSection finalCurrentSection = currentSection;
                                        objectives.setOnMouseClicked(event -> {
                                            if (finalCurrentSection != null) {
                                                renderSection(finalCurrentSection);
                                            }
                                        });
                                        objectives.setOnMouseEntered(event -> {
                                            objectives.setUnderline(true);
                                        });
                                        objectives.setOnMouseExited(event -> {
                                            objectives.setUnderline(false);
                                        });

                                        Circle dot1 = new Circle(3.0);
                                        dot1.setFill(Paint.valueOf("#233D2C"));

                                        Label content = new Label("Content");
                                        content.setTextFill(Paint.valueOf("#4081FF"));
                                        content.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, 15));
                                        content.setDisable(true);
                                        content.setOnMouseEntered(event -> {
                                            content.setUnderline(true);
                                        });
                                        content.setOnMouseExited(event -> {
                                            content.setUnderline(false);
                                        });

                                        Circle dot2 = new Circle(3.0);
                                        dot2.setFill(Paint.valueOf("#233D2C"));

                                        Label evaluation = new Label("Evaluation");
                                        evaluation.setTextFill(Paint.valueOf("#4081FF"));
                                        evaluation.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, 15));
                                        evaluation.setDisable(true);
                                        evaluation.setOnMouseEntered(event -> {
                                            evaluation.setUnderline(true);
                                        });
                                        evaluation.setOnMouseExited(event -> {
                                            evaluation.setUnderline(false);
                                        });

                                        topicBottomBox.setPadding(new Insets(5.0, 0.0, 10.0, 0.0));
                                        topicBottomBox.setAlignment(Pos.CENTER_LEFT);
                                        topicBottomBox.getChildren().addAll(objectives, dot1, content, dot2, evaluation);

                                        categoryContent.getChildren().add(topicBottomBox);
                                    });

                                    sectionNodes.add(categoryContent);
                                }
                            });
                            return sectionNodes;
                        }
                    };
                    sectionsTask.setOnSucceeded(
                            event -> Platform.runLater(() -> {
                                tabTopicsVBox.getChildren().addAll(sectionsTask.valueProperty().getValue());
                                tabTopicsVBox.setBackground(Background.EMPTY);
                                hideProgressBar();
                            })
                    );
                    executorService.execute(sectionsTask);
                    executorService.shutdown();

                    renderObjectives();
                    renderRecommendedTexts();
                } else {
                    hideProgressBar();
                }

                MainApplication.timeTakenTo("done loading syllabus");
            }));
        });
    }

    private void renderSection(SyllabusSection section) {
        if (!sectionPane.isVisible()) {
            emptySectionText.setVisible(false);
            Animations.slideIn(sectionPane);
        }

        ContentViewType sectionContentViewType = ContentViewTypes.convert(section);
        HeaderViewType sectionViewType = (HeaderViewType) sectionContentViewType;
        topicTitle.setText(sectionViewType.getText());

        String formattedText = section.getObjectives().replaceAll("<br>", System.lineSeparator());
        sectionContent.setText(formattedText);
        sectionContent.setWrapText(true);
        sectionContent.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, 16));
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        backIcon.setFitHeight(25);
        backIcon.setPreserveRatio(true);
        backButton.setGraphic(backIcon);
        backButton.setBackground(Background.EMPTY);

        sectionLayoutCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));

        syllabusTabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            syllabusTabPane.setTabMinWidth((Double) newValue/3);
            syllabusTabPane.setTabMaxWidth((Double) newValue/3);
        });

        sectionLayoutCloseIcon.setOnMouseClicked(event -> {
            if (sectionPane.isVisible()) {
                Animations.slideOut(sectionPane);
                emptySectionText.setVisible(true);
            }
        });
    }

    private void initializeFonts() {
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
//        categoriesTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        emptySyllabusListLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        topicTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 17));
        objectivesTab.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        contentTab.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        evaluationTab.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
        emptySectionText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
//        viewSyllabusButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));

    }

    private void renderObjectives() {
        String formattedText = viewModel.getSelectedSyllabusSubject().getGeneralObjectives().replaceAll("<br>", System.lineSeparator());
        objectivesLabel.setText(formattedText);
        objectivesLabel.setWrapText(true);
        objectivesLabel.setTextAlignment(TextAlignment.JUSTIFY);
        objectivesLabel.setPadding(new Insets(20, 15, 0, 15));
        objectivesLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

        genObjectiveTab.setContent(objectivesLabel);
        genObjectiveTab.getContent().autosize();
        topicsTab.getContent().autosize();
    }

    private void renderRecommendedTexts() {
        String recommendedText = viewModel.getSelectedSyllabusSubject().getRecommendedTexts();
        String formattedText = recommendedText
                .replaceAll("<br>", System.lineSeparator())
                .replaceAll("<i>", "").replaceAll("</i>", "");
        recTextLabel.setText(formattedText);
        recTextLabel.setWrapText(true);
        recTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        recTextLabel.setPadding(new Insets(20, 15, 0, 15));
        recTextLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

        topicsTab.getContent().autosize();
    }

    private String numberToAlphabet(int number, boolean capitalize) {
        return switch (number) {
            case 1 -> capitalize ? "A" : "a";
            case 2 -> capitalize ? "B" : "b";
            case 3 -> capitalize ? "C" : "c";
            case 4 -> capitalize ? "D" : "d";
            case 5 -> capitalize ? "E" : "e";
            case 6 -> capitalize ? "F" : "f";
            case 7 -> capitalize ? "G" : "g";
            case 8 -> capitalize ? "H" : "h";
            case 9 -> capitalize ? "I" : "i";
            case 10 -> capitalize ? "J" : "j";
            case 11 -> capitalize ? "K" : "k";
            case 12 -> capitalize ? "L" : "l";
            case 13 -> capitalize ? "M" : "m";
            case 14 -> capitalize ? "N" : "n";
            case 15 -> capitalize ? "O" : "o";
            case 16 -> capitalize ? "P" : "p";
            case 17 -> capitalize ? "Q" : "q";
            case 18 -> capitalize ? "R" : "r";
            case 19 -> capitalize ? "S" : "s";
            case 20 -> capitalize ? "T" : "t";
            case 21 -> capitalize ? "U" : "u";
            case 22 -> capitalize ? "V" : "v";
            case 23 -> capitalize ? "W" : "w";
            case 24 -> capitalize ? "X" : "x";
            case 25 -> capitalize ? "Y" : "y";
            case 26 -> capitalize ? "Z" : "z";
            default -> "a";
        };
    }

    public void backButtonClicked(MouseEvent event) {
        ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        tabTopicsVBox.setVisible(true);
        topicsTab.setDisable(false);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        tabTopicsVBox.setVisible(false);
    }
}
