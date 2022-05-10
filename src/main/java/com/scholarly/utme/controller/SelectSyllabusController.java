package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.SyllabusCategory;
import com.scholarly.utme.data.model.newDb.SyllabusTopic;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.SelectSyllabusVM;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/SelectSyllabusScreen.fxml")
public class SelectSyllabusController implements FxmlView<SelectSyllabusVM>, Initializable {

    @InjectViewModel
    private SelectSyllabusVM viewModel;

    @FXML
    private Button backButton, viewSyllabusButton;

    @FXML
    private Label pageTitle, subjectsTitle, categoriesTitle, emptyCategoryListLabel;

    @FXML
    private VBox subjectListVBox, categoryListVBox;

    final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ECF2EB; -fx-background-radius: 0; -fx-border-radius: 0;";
    final String PRESSED_STYLE = "-fx-background-color: #759D6C; -fx-background-radius: 0; -fx-border-radius: 0;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        categoriesTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        emptyCategoryListLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        viewSyllabusButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));

        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        backIcon.setFitHeight(25);
        backIcon.setPreserveRatio(true);
        backButton.setGraphic(backIcon);
        backButton.setBackground(Background.EMPTY);

        ToggleGroup subjectListToggleGroup = new ToggleGroup();
        subjectListToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Subject selectedSubject = (Subject) newValue.getUserData();
                viewModel.setSelectedSubject(selectedSubject);
                emptyCategoryListLabel.setVisible(false);

            }else {
                viewModel.setSelectedSubject(null);
                emptyCategoryListLabel.setVisible(true);
            }
        });

        viewModel.getSubjects().forEach(subject -> {

            ToggleButton button = new ToggleButton();
            button.setUserData(subject);
            subjectListToggleGroup.getToggles().add(button);

            button.setMinHeight(70);
            button.setMaxHeight(70);
            button.setPadding(new Insets(0, 0, 0, 20));
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setText(subject.getSubjectName());

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

        ToggleGroup topicListToggleGroup = new ToggleGroup();
        topicListToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Pair<SyllabusCategory, SyllabusTopic> userData = (Pair<SyllabusCategory, SyllabusTopic>) newValue.getUserData();
                SyllabusCategory category = userData.getKey();
                SyllabusTopic selectedTopic = userData.getValue();
                viewModel.setSelectedTopic(selectedTopic);
                viewModel.setSelectedCategory(category);
                System.out.println("Selected Category -> " + category.getTitle());
                System.out.println("Selected SyllabusTopic -> " + selectedTopic.getTitle());

            }
        });


        viewModel.selectedSubjectProperty().addListener(((observable, oldValue, newValue) -> {
            topicListToggleGroup.getToggles().clear();
            categoryListVBox.getChildren().clear();

            if (newValue != null) {

                ObservableList<SubTopic> subTopics = viewModel.getSyllabusSubTopics().get(newValue.getSubjectName());

                ObservableList<SyllabusCategory> categories = viewModel.getCategories().get(newValue.getSubjectName());

                categories.forEach(category -> {

                    VBox vBox = new VBox(5);
                    TitledPane categoryTitlePane = new TitledPane(category.getTitle(), vBox);
                    categoryTitlePane.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));

                    viewModel.getSyllabusTopics().get(newValue.getSubjectName()).forEach(syllabusTopic -> {

                        if (category.getId() == syllabusTopic.getCategoryId()) {

                            Pair<SyllabusCategory, SyllabusTopic> data = new Pair<>(category, syllabusTopic);

                            ToggleButton topicButton = new ToggleButton();
                            topicButton.setUserData(data);
                            topicButton.setText(syllabusTopic.getTitle());
                            topicListToggleGroup.getToggles().add(topicButton);

                            topicButton.setMinHeight(48);
                            topicButton.setMaxHeight(48);
                            topicButton.setPadding(new Insets(0, 0, 0, 20));
                            topicButton.setAlignment(Pos.BASELINE_LEFT);
                            topicButton.setMaxWidth(Double.MAX_VALUE);
                            topicButton.setText(syllabusTopic.getTitle());
                            topicButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.TWELVE.size));

                            topicButton.setStyle(IDLE_BUTTON_STYLE);
                            topicButton.setOnMouseEntered(e -> {
                                if (!topicButton.isSelected()) {
                                    topicButton.setStyle(HOVERED_BUTTON_STYLE);
                                }
                            });
                            topicButton.setOnMouseExited(e -> {
                                if (!topicButton.isSelected()) {
                                    topicButton.setStyle(IDLE_BUTTON_STYLE);
                                }
                            });

                            topicButton.selectedProperty().addListener((observe, old, newVal) -> {
                                if (newVal) {
                                    topicButton.setStyle(PRESSED_STYLE);
                                    topicButton.setTextFill(Color.WHITE);
                                } else {
                                    topicButton.setStyle(IDLE_BUTTON_STYLE);
                                    topicButton.setTextFill(Color.BLACK);
                                }
                            });

                            vBox.getChildren().add(topicButton);

                            /*subTopics.forEach(subTopic -> {
                                if (syllabusTopic.getId() == subTopic.getTopicId()) {
                                    System.out.println("Got subTopic for topic: " + syllabusTopic.getTitle() + " subTopic -> " + subTopic.getTitle());

                                    Pair<SyllabusTopic, SubTopic> data = new Pair<>(syllabusTopic, subTopic);
                                    topicButton.setUserData(data);
                                }
                            });*/

                        }

                    });

                    categoryListVBox.getChildren().add(categoryTitlePane);
                });
            }

        }));

        viewSyllabusButton.setOnAction(event -> {
            SyllabusScreenController.InitialData data = new SyllabusScreenController.InitialData(
                    viewModel.getSelectedSubject(),
                    viewModel.getSelectedCategory(),
                    viewModel.getSyllabusTopics().get(
                            viewModel.getSelectedSubject().getSubjectName()
                    ).stream().filter(syllabusTopic -> syllabusTopic.getCategoryId() == viewModel.getSelectedTopic().getCategoryId()).collect(Collectors.toList()),
                    viewModel.getSelectedTopic()
            );

            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.SYLLABUS_SCREEN);

        });
    }

    public void backButtonClicked(MouseEvent event) {
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }

}
