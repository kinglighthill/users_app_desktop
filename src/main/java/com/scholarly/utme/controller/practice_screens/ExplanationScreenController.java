package com.scholarly.utme.controller.practice_screens;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.QuestionDescription;
import com.scholarly.utme.data.model.TheoryQuestion;
import com.scholarly.utme.data.model.Year;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.practice_screens.ExplanationScreenVM;
import com.scholarly.utme.viewmodels.practice_screens.PracticeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static com.scholarly.utme.viewmodels.SubjectListItemVM.*;


@FxmlPath("/layouts/practice_screens/ExplanationScreen.fxml")
public class ExplanationScreenController implements FxmlView<ExplanationScreenVM>, Initializable {
    private static final String TAG = "ExplanationScreenController: ";

    @InjectViewModel
    private ExplanationScreenVM viewModel;

    @FXML
    private WebView questionWebView, explanationWebView, questionWithImageWebView, explanationWithImageWebView;
    @FXML
    private TilePane tilePane;
    @FXML
    private ScrollPane tileScrollPane, questionScrollPane;
    @FXML
    private StackPane explanationPane, explanationVideo, questionStackPane;
    @FXML
    private AnchorPane explanationAnchor;
    @FXML
    private Pane dialogDimmer;
    @FXML
    private Line questionLine;
    @FXML
    private VBox centerVBox, questionDescriptionDialog, explanationVBox, questionCenterVBox, questionVBox, explanationWithImageVbox;
    @FXML
    private HBox toggleBox, quesDescriptionHBox, questionWithImageHBox, questionDescriptionHBox, questionLabelHBox;
    @FXML
    private ListView<PQSubject> subjectList;
    @FXML
    private Panel optionAPanel, optionBPanel, optionCPanel, optionDPanel;
    @FXML
    private Label questionOverviewLabel, questionLabel, optionA, optionB, optionC, optionD, noOptionSelected, questionDescriptionLabel, readQuestionDesc, questionDescriptionText, explanationLabel, appBarTitle;
    @FXML
    private Button prevButton, nextButton, exitButton;
    @FXML
    private RadioButton optionAButton, optionBButton, optionCButton, optionDButton;
    @FXML
    private ToggleButton textExplanation, videoExplanation;
    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, optionAIcon, optionBIcon, optionCIcon, optionDIcon, questionImage, quesDescriptionCloseIcon, explanationImage;

    private Image correctImage, incorrectImage;

    private Screens previousScreen;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        previousScreen = getInitialData().previousScreen;

        viewModel.processInitialData(getInitialData());

        initializeViews();

        initializeFont();

//        setupVideoPlayer();

        subjectList.setCellFactory(new PracticeSubjectListCellFactory());
        subjectList.setItems(viewModel.getSubjects());
        subjectList.getSelectionModel().selectFirst();
        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super PQSubject>) c -> {
            if (c.getList().size() == 1) {
                PQSubject subject = c.getList().get(0);
                viewModel.setSelectedSubject(subject);
                appBarTitle.setText("CBT PRACTICE");
                appBarTitle.setText(appBarTitle.getText() + "  " + viewModel.getSelectedSubjectYear().get(viewModel.getSelectedSubject().getShortTitle()).getYear());
            } else {
                viewModel.setSelectedSubject(null);
            }
        });

        viewModel.setSelectedSubject(subjectList.getSelectionModel().getSelectedItem());
        setupQuestionView(viewModel.getSelectedSubject());
        setupTilePane(viewModel.getSelectedSubject());

        appBarTitle.setText(appBarTitle.getText() + "  " + viewModel.getSelectedSubjectYear().get(viewModel.getSelectedSubject().getShortTitle()).getYear());

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupQuestionView(newValue);
                setupTilePane(newValue);
            }
        });

        viewModel.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
            subjectQuestionsState.selectedQuestionProperty().addListener((observable, oldValue, newValue) -> {
                if (viewModel.getSelectedSubject().getShortTitle().equalsIgnoreCase(s)) {
                    if (newValue.intValue() <= subjectQuestionsState.getQuestions().size()) {
                        changeSelectedTile(oldValue.intValue(), newValue.intValue());
                        changeSelectedQuestion(newValue.intValue());
                    }
                }
            });
        });

        // Integer value to track subjectList subject selection
        AtomicInteger subjectListSelectionAtomicIndex = new AtomicInteger(0);

        subjectList.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<? super Integer>) observable -> {
            if (observable.getList().size() == 1) {
                subjectListSelectionAtomicIndex.set(observable.getList().get(0));
            }
        });

        prevButton.setOnAction(event -> {
            nextButton.setDisable(false);

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            if (subjectList.getSelectionModel().getSelectedIndex() == 0) {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestion - 1);

                if (selectedQuestion == 2) {
                    prevButton.setDisable(true);
                }
            } else {
                if (selectedQuestion == 1) {
                    subjectList.getSelectionModel().select(subjectListSelectionAtomicIndex.decrementAndGet());
                } else {
                    viewModel.getSubjectsQuestions()
                            .get(viewModel.getSelectedSubject().getShortTitle())
                            .setSelectedQuestion(selectedQuestion - 1);
                }
            }
        });

        nextButton.setOnAction(event -> {
            prevButton.setDisable(false);

            PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
            List<PracticeScreenVM.QuestionState> questionStates = subjectQuestionsState.getQuestions();

            int selectedQuestionIndex = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            if (selectedQuestionIndex == questionStates.size()) {
                subjectList.getSelectionModel().select(subjectListSelectionAtomicIndex.incrementAndGet());
                prevButton.setDisable(false);

            } else {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestionIndex + 1);

                int newSelectedQuestionIndex = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .getSelectedQuestion();

                if (newSelectedQuestionIndex == questionStates.size() && subjectList.getItems().size()-1 == subjectListSelectionAtomicIndex.get())
                    nextButton.setDisable(true);
            }

        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            PracticeScreenVM.QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getQuestions()
                    .get(selectedQuestion - 1);

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                // System.out.println("Question: " + ((ObjectiveQuestion) questionState.getQuestion()).getQuestion());
                ObjectiveQuestion currentQuestion = ((ObjectiveQuestion) questionState.getQuestion());
                StringBuilder textToRead = new StringBuilder(currentQuestion.getQuestion());
                textToRead.append(". Option A, ").append(currentQuestion.getOptionA().getText());
                textToRead.append(". Option B, ").append(currentQuestion.getOptionB().getText());
                textToRead.append(". Option C, ").append(currentQuestion.getOptionC().getText());
                textToRead.append(". Option D, ").append(currentQuestion.getOptionD().getText());

                if (!Helper.isWebView(currentQuestion.getQuestion())) {
                    TextToSpeech.play(textToRead.toString());
                }
            } else {
                TextToSpeech.play(((TheoryQuestion) questionState.getQuestion()).getQuestion());
            }
        });

//        textExplanation.selectedProperty().addListener(((observable, oldValue, newValue) -> {
//            if (newValue) {
//                textExplanation.setText("Hide Explanation");
//                explanationLabel.setVisible(true);
////                explanationWebView.setVisible(true);
//            } else {
//                textExplanation.setText("Show Explanation");
//                explanationLabel.setVisible(false);
////                explanationWebView.setVisible(false);
//            }
//        }));

//        toggleBox.getChildren().remove(videoExplanation);
        ToggleGroup explanationGroup = new ToggleGroup();
        explanationGroup.getToggles().addAll(textExplanation);
        /*explanationGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == textExplanation) {
                textExplanation.setStyle("-fx-background-color: #12AF20; -fx-border-color: #12AF20;");
                textExplanation.setTextFill(Paint.valueOf("#FFFFFF"));
//                videoExplanation.setStyle("-fx-border-color: #12AF20;");
//                videoExplanation.setTextFill(Paint.valueOf("#12AF20"));

                explanationPane.getChildren().remove(explanationVideo);
                if (!explanationPane.getChildren().contains(explanationLabel)) {
                    explanationPane.getChildren().add(explanationLabel);
                }

            } else if (newValue == videoExplanation) {
                explanationVideo.setVisible(true);
                videoExplanation.setStyle("-fx-background-color: #12AF20; -fx-border-color: #12AF20;");
                videoExplanation.setTextFill(Paint.valueOf("#FFFFFF"));
                textExplanation.setStyle("-fx-border-color: #12AF20;");
                textExplanation.setTextFill(Paint.valueOf("#12AF20"));

                explanationPane.getChildren().remove(explanationLabel);
                if (!explanationPane.getChildren().contains(explanationVideo)) {
                    explanationPane.getChildren().add(explanationVideo);
                }

            }
        }));*/

        exitButton.setOnAction(event -> {
            dialogDimmer.setVisible(true);
            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Exit", null, "Are you sure you want to exit?");
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    dialogDimmer.setVisible(false);

                    ViewSwitcher.passData(new HomeScreenController.InitialData(previousScreen, null));
                    ViewSwitcher.showScreen(View.HOME_SCREEN);
                } else {
                    dialogDimmer.setVisible(false);
                }
                return buttonType;
            });
            dialog.show();
        });

//        readQuestionDesc.setOnMouseClicked(mouseEvent -> {
//            Animations.showDialog(questionDescriptionDialog, dialogDimmer);
//        });
//        readQuestionDesc.setOnMouseEntered(event -> readQuestionDesc.setUnderline(true));
//        readQuestionDesc.setOnMouseExited(event -> readQuestionDesc.setUnderline(false));

        quesDescriptionCloseIcon.setOnMouseClicked(mouseEvent -> {
            Animations.hideDialog(questionDescriptionDialog, dialogDimmer);
        });

    }

    private void initializeViews() {
        subjectList.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
        prevButton.setBackground(Background.EMPTY);
//        tileScrollPane.setBackground(Background.EMPTY);
        textExplanation.setBackground(Background.EMPTY);
//        videoExplanation.setBackground(Background.EMPTY);

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
//        flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));

        quesDescriptionCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));

        correctImage = new Image(getClass().getResource("/drawable/practice_screen_images/correct_icon.png").toString());
        incorrectImage = new Image(getClass().getResource("/drawable/practice_screen_images/incorrect_icon.png").toString());

        ImageView prevImage = new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/prev_btn_icon.png").toString()));
        prevButton.setGraphicTextGap(15);
        prevButton.setGraphic(prevImage);
        ImageView nextImage = new ImageView(getClass().getResource("/drawable/practice_screen_images/next_btn_icon.png").toString());
        nextButton.setContentDisplay(ContentDisplay.RIGHT);
        nextButton.setGraphicTextGap(15);
        nextButton.setGraphic(nextImage);
    }

    private void initializeFont() {
        exitButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        explanationLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
    }

    /**
     * Initializes question layout
     */
    private void setupQuestionView(PQSubject selectedSubject) {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();
        PracticeScreenVM.QuestionState question = questions.get(subjectQuestionsState.getSelectedQuestion() - 1);

        int selectedQuestionIndex = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + selectedQuestionIndex + " of " + questions.size());

        prevButton.setDisable(subjectList.getSelectionModel().getSelectedIndex() == 0 && selectedQuestionIndex == 1);

        if (viewModel.getQuestionType() == Type.OBJECTIVE) {
            ObjectiveQuestion objectiveQuestion = question.getObjectiveQuestion();

            List<QuestionDescription> quesDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == question.getObjectiveQuestion().getQuestionDescriptionId()).toList();

            String questionText = question.getObjectiveQuestion().getQuestion().replaceAll("<br>", System.lineSeparator());
            questionLabel.setText(questionText);

            questionCenterVBox.getChildren().removeAll(questionScrollPane, questionLabelHBox);
            questionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);

            if (questionText.contains("<img")) {
                questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView);
                if (!questionCenterVBox.getChildren().contains(questionWithImageHBox))
                    questionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(question.getObjectiveQuestion(), true));
            } else {
                showObjectiveQuestion(questionText, quesDescriptionInList, Helper.isWebView(question.getObjectiveQuestion(), true));
            }

            Helper.loadOption(getClass(), optionA, objectiveQuestion.getOptionA().getText(), " (A) ", "#F3FBF4;");
            Helper.loadOption(getClass(), optionB, objectiveQuestion.getOptionB().getText(), " (B) ", "#F3FBF4;");
            Helper.loadOption(getClass(), optionC, objectiveQuestion.getOptionC().getText(), " (C) ", "#F3FBF4;");
            Helper.loadOption(getClass(), optionD, objectiveQuestion.getOptionD().getText(), " (D) ", "#F3FBF4;");

//            optionA.setText(" (A) " + question.getObjectiveQuestion().getOptionA().getText());
//            optionB.setText(" (B) " + question.getObjectiveQuestion().getOptionB().getText());
//            optionC.setText(" (C) " + question.getObjectiveQuestion().getOptionC().getText());
//            optionD.setText(" (D) " + question.getObjectiveQuestion().getOptionD().getText());

            optionA.setTextFill(Color.BLACK);
            optionB.setTextFill(Color.BLACK);
            optionC.setTextFill(Color.BLACK);
            optionD.setTextFill(Color.BLACK);

            optionAIcon.setImage(null);
            optionBIcon.setImage(null);
            optionCIcon.setImage(null);
            optionDIcon.setImage(null);

            optionAButton.setSelected(false);
            optionBButton.setSelected(false);
            optionCButton.setSelected(false);
            optionDButton.setSelected(false);

//            String selectedOption = question.getSelectedOption();
//            String questionAnswer = question.getObjectiveQuestion().getQuestionAnswer().getAnswer();

            int selectedOptionId = question.getSelectedOptionId();
            int questionAnswerId = question.getObjectiveQuestion().getQuestionAnswer().getId();

            int optionAId = question.getObjectiveQuestion().getOptionA().getId();
            int optionBId = question.getObjectiveQuestion().getOptionB().getId();
            int optionCId = question.getObjectiveQuestion().getOptionC().getId();
            int optionDId = question.getObjectiveQuestion().getOptionD().getId();

            if (selectedOptionId != -1) { // an option was selected

                if (selectedOptionId == optionAId) {
                    optionAButton.setSelected(true);
                } else if (selectedOptionId == optionBId) {
                    optionBButton.setSelected(true);
                } else if (selectedOptionId == optionCId) {
                    optionCButton.setSelected(true);
                } else if (selectedOptionId  == optionDId) {
                    optionDButton.setSelected(true);
                }

                if (questionAnswerId == optionAId) { // correct option was selected
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);
                }


                if (selectedOptionId != questionAnswerId) { // wrong option was selected

                    if (selectedOptionId == optionAId) {
                        optionA.setTextFill(Color.RED);
                        optionAIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionBId) {
                        optionB.setTextFill(Color.RED);
                        optionBIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionCId) {
                        optionC.setTextFill(Color.RED);
                        optionCIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionDId) {
                        optionD.setTextFill(Color.RED);
                        optionDIcon.setImage(incorrectImage);

                    }

                }

                noOptionSelected.setVisible(false);

            } else { // no option was selected

                noOptionSelected.setText("No option was selected");
                noOptionSelected.setTextFill(Paint.valueOf("#FF0000"));

                if (questionAnswerId == optionAId) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);

                } else if (questionAnswerId == -1) {
                    noOptionSelected.setText("No correct answer for this question (Bonus)");
                    noOptionSelected.setTextFill(Paint.valueOf("#12AF20"));
                }

                noOptionSelected.setVisible(true);
            }

            String explanationText = question.getObjectiveQuestion().getQuestionAnswer().getExplanation();

            if (explanationText.contains("<img")) {
                showExplanationWithImage(explanationText, Helper.isWebView(explanationText));
            } else {
                showExplanation(explanationText, Helper.isWebView(explanationText));
            }

        } else if (viewModel.getQuestionType() == Type.THEORY){

            String questionText = question.getTheoryQuestion().getQuestion();
//            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            String explanationText = question.getTheoryQuestion().getQuestionAnswer().getExplanation();

            if (explanationText.contains("<img")) {
                explanationText = formatExplanationTextWithImage(explanationText);
                explanationWebView.getEngine().loadContent(explanationText);
            } else {
                explanationWebView.getEngine().loadContent(explanationText);
            }

            centerVBox.getChildren().removeAll(optionAPanel, optionBPanel, optionCPanel, optionDPanel, noOptionSelected);
        }

        if (previousScreen == Screens.CBT_GAME_SCREEN) {
            noOptionSelected.setVisible(false);
        }

    }

    /**
     * Changes selected question of the explanation screen and re-renders views
     * @param questionNumber number of the selected question
     */
    private void changeSelectedQuestion(int questionNumber) {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + questionNumber + " of " + questions.size());

        if (viewModel.getQuestionType() == Type.OBJECTIVE) {

            ObjectiveQuestion objectiveQuestion = questions.get(questionNumber - 1).getObjectiveQuestion();

            List<QuestionDescription> quesDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == objectiveQuestion.getQuestionDescriptionId()).toList();

            String questionText = objectiveQuestion.getQuestion().replaceAll("<br>", System.lineSeparator());
            questionLabel.setText(questionText);

//            questionVBox.getChildren().removeAll(questionDescriptionHBox, questionLine);

            questionCenterVBox.getChildren().removeAll(questionScrollPane);
            questionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox, questionLabelHBox);

            if (questionText.contains("<img")) {
                questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView);
                if (!questionCenterVBox.getChildren().contains(questionWithImageHBox))
                    questionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(objectiveQuestion, true));
            } else {
                showObjectiveQuestion(questionText, quesDescriptionInList, Helper.isWebView(objectiveQuestion, true));
            }

            Helper.loadOption(getClass(), optionA, objectiveQuestion.getOptionA().getText(), " (A) ", "#F3FBF4;");
            Helper.loadOption(getClass(), optionB, objectiveQuestion.getOptionB().getText(), " (B) ", "#F3FBF4;");
            Helper.loadOption(getClass(), optionC, objectiveQuestion.getOptionC().getText(), " (C) ", "#F3FBF4;");
            Helper.loadOption(getClass(), optionD, objectiveQuestion.getOptionD().getText(), " (D) ", "#F3FBF4;");

//            optionA.setText(" (A) " + objectiveQuestion.getOptionA().getText());
//            optionB.setText(" (B) " + objectiveQuestion.getOptionB().getText());
//            optionC.setText(" (C) " + objectiveQuestion.getOptionC().getText());
//            optionD.setText(" (D) " + objectiveQuestion.getOptionD().getText());

            optionA.setTextFill(Color.BLACK);
            optionB.setTextFill(Color.BLACK);
            optionC.setTextFill(Color.BLACK);
            optionD.setTextFill(Color.BLACK);

            optionAIcon.setImage(null);
            optionBIcon.setImage(null);
            optionCIcon.setImage(null);
            optionDIcon.setImage(null);

            optionAButton.setSelected(false);
            optionBButton.setSelected(false);
            optionCButton.setSelected(false);
            optionDButton.setSelected(false);

//            String selectedOption = questions.get(questionNumber - 1).getSelectedOption();
//            String questionAnswer = questions.get(questionNumber - 1).getObjectiveQuestion().getQuestionAnswer().getAnswer();

            int selectedOptionId = questions.get(questionNumber - 1).getSelectedOptionId();
            int questionAnswerId = questions.get(questionNumber - 1).getObjectiveQuestion().getQuestionAnswer().getId();

            int optionAId = objectiveQuestion.getOptionA().getId();
            int optionBId = objectiveQuestion.getOptionB().getId();
            int optionCId = objectiveQuestion.getOptionC().getId();
            int optionDId = objectiveQuestion.getOptionD().getId();

            if (selectedOptionId != -1) { // an option was selected

                if (selectedOptionId == optionAId) {
                    optionAButton.setSelected(true);
                } else if (selectedOptionId == optionBId) {
                    optionBButton.setSelected(true);
                } else if (selectedOptionId == optionCId) {
                    optionCButton.setSelected(true);
                } else if (selectedOptionId == optionDId) {
                    optionDButton.setSelected(true);
                }

                if (questionAnswerId == optionAId) { // correct option was selected
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);

                }


                if (selectedOptionId != questionAnswerId) { // wrong option was selected

                    if (selectedOptionId == optionAId) {
                        optionA.setTextFill(Color.RED);
                        optionAIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionBId) {
                        optionB.setTextFill(Color.RED);
                        optionBIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionCId) {
                        optionC.setTextFill(Color.RED);
                        optionCIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionDId) {
                        optionD.setTextFill(Color.RED);
                        optionDIcon.setImage(incorrectImage);

                    }

                }

                noOptionSelected.setVisible(false);

            } else { // no option was selected

                noOptionSelected.setText("No option was selected");
                noOptionSelected.setTextFill(Paint.valueOf("#FF0000"));

                if (questionAnswerId == optionAId) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);

                } else if (questionAnswerId == -1) {
                    noOptionSelected.setText("No correct answer for this question (Bonus)");
                    noOptionSelected.setTextFill(Paint.valueOf("#12AF20"));
                }

                noOptionSelected.setVisible(true);
            }

            String explanationText = objectiveQuestion.getQuestionAnswer().getExplanation();

            if (explanationText.contains("<img")) {
                showExplanationWithImage(explanationText, Helper.isWebView(explanationText));

                System.out.println(TAG + "Explanation with Image Text -> " + explanationText);

            } else {
                showExplanation(explanationText, Helper.isWebView(explanationText));
            }

        } else if (viewModel.getQuestionType() == Type.THEORY) {

            String questionText = questions.get(questionNumber - 1).getTheoryQuestion().getQuestion();
//            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            String explanationText = questions.get(questionNumber - 1).getTheoryQuestion().getQuestionAnswer().getExplanation();

            if (explanationText.contains("<img")) {
                explanationText = formatExplanationTextWithImage(explanationText);
                explanationWebView.getEngine().loadContent(explanationText);
            } else {
                explanationWebView.getEngine().loadContent(explanationText);
            }

        }

        if (previousScreen == Screens.CBT_GAME_SCREEN) {
            noOptionSelected.setVisible(false);
        }

    }

    /**
     * Initializes question tiles at bottom of the screen
     */
    private void setupTilePane(PQSubject selectedSubject) {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(34, 34);
            r.setFill(Color.web("#FFFFFF"));
            r.setStroke(Paint.valueOf("#12AF20"));
            r.setStrokeType(StrokeType.OUTSIDE);

            Label l = new Label(Integer.toString(i));

            if (viewModel.getQuestionType() == Type.OBJECTIVE) {
                if (questions.get(i - 1).getObjectiveQuestion().getQuestionAnswer().getId() == questions.get(i-1).getSelectedOptionId()) {
                    r.setFill(Paint.valueOf("#12AF20"));
                    l.setTextFill(Paint.valueOf("#FFFFFF"));

                } else if (questions.get(i-1).getSelectedOptionId() != -1) {
                    r.setFill(Color.web("#FA0000", 0.5));
                    l.setTextFill(Color.WHITE);
                }

            }

            if (subjectQuestionsState.getSelectedQuestion() == i) {
                r.setStroke(Paint.valueOf("#FCB029"));
                r.setStrokeWidth(2.0);
            }

            StackPane s = new StackPane(r, l);
            s.setStyle("-fx-cursor: hand;");
            tilePane.getChildren().add(s);

            int finalI = i;
            s.setOnMouseClicked(event -> {
                subjectQuestionsState.setSelectedQuestion(finalI);
            });
        }
    }

    /**
     * Changes selected question tile at bottom of the screen
     * @param oldSelectedQuestion old selected question
     * @param newSelectedQuestion new question selected
     */
    private void changeSelectedTile(int oldSelectedQuestion, int newSelectedQuestion) {

        StackPane selectedQuestionPane = (StackPane) tilePane.getChildren().get(newSelectedQuestion - 1);
        StackPane oldQuestionPane = (StackPane) tilePane.getChildren().get(oldSelectedQuestion - 1);

        Rectangle selectedQuestionRect = (Rectangle) selectedQuestionPane.getChildren().get(0);
        Rectangle oldQuestionRect = (Rectangle) oldQuestionPane.getChildren().get(0);

        oldQuestionRect.setStroke(Paint.valueOf("#12AF20"));
        oldQuestionRect.setStrokeWidth(1.0);
        selectedQuestionRect.setStroke(Paint.valueOf("#FCB029"));
        selectedQuestionRect.setStrokeWidth(2);

    }

    private void showQuestionWithImage(String questionWithImageText, boolean isWebView) {
        questionWithImageText = Helper.loadPQImageUrl(getClass(), questionImage, questionWithImageWebView, questionWithImageText);
    }

    private void showObjectiveQuestion(String questionText, List<QuestionDescription> quesDescriptionList, boolean isWebView) {
        if (isWebView) {
            System.out.println(TAG + "Question is WebView");
            questionCenterVBox.getChildren().removeAll(questionScrollPane, questionLabelHBox, questionLabel);

            if (!quesDescriptionList.isEmpty()) {
                if (!questionCenterVBox.getChildren().contains(questionScrollPane)) {
                    questionCenterVBox.getChildren().add(questionScrollPane);
                    questionScrollPane.setMinHeight(180);
                    questionDescriptionLabel.setText(quesDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
                }
            }

            questionCenterVBox.getChildren().add(questionWebView);
            String content = Helper.loadLatex(getClass(), questionText);
            questionWebView.getEngine().loadContent(content);
            questionWebView.setMinHeight(200);

            if (!quesDescriptionList.isEmpty()) {
//                questionCenterVBox.getChildren().add(questionWebView);
                questionScrollPane.setMinHeight(110);
                questionWebView.setMinHeight(90);
            }
        } else {
            questionVBox.getChildren().removeAll(questionWebView);

            questionLabelHBox.setMinHeight(10);
            if (!quesDescriptionList.isEmpty()) {
                if (!questionCenterVBox.getChildren().contains(questionScrollPane)) {
                    questionCenterVBox.getChildren().add(questionScrollPane);
                    questionScrollPane.setMinHeight(180);
                    questionDescriptionLabel.setText(quesDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
                }
            } else {
                if (!questionCenterVBox.getChildren().contains(questionScrollPane)) {
                    questionLabelHBox.setMinHeight(200);
                }
            }

            if (!questionCenterVBox.getChildren().contains(questionLabelHBox)) {
                questionCenterVBox.getChildren().add(questionLabelHBox);
            }

//            if (!questionCenterVBox.getChildren().contains(questionLabel)) {
//                questionCenterVBox.getChildren().add(questionLabel);
//            }
            questionLabel.setText(questionText);
        }
    }

    private void showExplanation(String explanationText, boolean isWebView) {
        if (isWebView) {
            System.out.println(TAG + "Explanation is WebView");

            explanationWebView.setVisible(true);
            String content = Helper.loadLatex(getClass(), explanationText);
            explanationWebView.getEngine().loadContent(content);
            explanationLabel.setVisible(false);
            explanationWithImageVbox.setVisible(false);

        } else {
            explanationLabel.setVisible(true);
            explanationLabel.setText(explanationText);
            explanationWebView.setVisible(false);
            explanationWithImageVbox.setVisible(false);

        }
    }

    private void showExplanationWithImage(String explanationText, boolean isWebView) {
        String answer = extractAnswerFromExplanation(explanationText, isWebView);
        String imageUrl = extractImageUrlFromExplanation(explanationText);

        explanationWithImageVbox.setVisible(true);
        explanationWithImageWebView.getEngine().loadContent(answer);
        explanationImage.setImage(new Image(getClass().getResource(imageUrl).toString()));

        explanationWebView.setVisible(false);
        explanationLabel.setVisible(false);
    }

    private String extractAnswerFromExplanation(String text, boolean isWebView) {
        String imageQuestion = text.substring(text.lastIndexOf("100%'")+6);
        if (isWebView) {
            imageQuestion = Helper.loadLatex(getClass(), imageQuestion);
        }
        return imageQuestion;
    }

    private String extractImageUrlFromExplanation(String text) {
        int startIndexOfImg = text.indexOf("<img");
        int endIndexOfImg = text.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = text.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = text.indexOf("' width", startIndexOfImg);

        String imagePath = text.substring(startIndexOfImgPath, endIndexOfImgPath);

        return imagePath.replace("android_asset/images", "assets/images/pq");
    }

    private String formatExplanationTextWithImage(String explanationText) {
        int startIndexOfImg = explanationText.indexOf("<img");
        int endIndexOfImg = explanationText.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = explanationText.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = explanationText.indexOf("' width", startIndexOfImg);

        String imagePath = explanationText.substring(startIndexOfImgPath, endIndexOfImgPath);

        String newImagePath = imagePath.replace("android_asset/images", "assets/images/pq");

        explanationImage.setImage(new Image(getClass().getResource(newImagePath).toString()));

        String imageQuestion = explanationText.substring(explanationText.lastIndexOf("100%'")+6);
//        if (isWebView) {
//            imageQuestion = loadLatex(mClass, imageQuestion);
//        }
        explanationWithImageWebView.getEngine().loadContent(imageQuestion);

//        StringBuilder builder = new StringBuilder(explanationText);
//
//        URL url = getClass().getResource(newImagePath);
//        String img = "<img src='"+url+"' width='100%'>";
//
//        explanationWebView.getEngine().loadContent(img);



        return Helper.parsePQImageUrl(getClass(), explanationText);
    }

    public void onCalculatorClicked(MouseEvent mouseEvent) {
        Stage calculatorStage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layouts/CalculatorView.fxml"));
            Scene scene = new Scene(root);

            Image appIcon = new Image(getClass().getResource("/drawable/app_logo.png").toString());
            calculatorStage.getIcons().add(appIcon);

            calculatorStage.setTitle("Calculator");
            calculatorStage.setResizable(false);
            calculatorStage.setScene(scene);
            calculatorStage.initOwner(ViewSwitcher.getRootScene().getWindow());

            calculatorStage.setX(ViewSwitcher.getRootScene().getWidth() / 1.3);
            calculatorStage.setY(ViewSwitcher.getRootScene().getHeight() / 2.7);

            calculatorStage.showAndWait();

        } catch (Exception e) {
            System.out.println(TAG + "Cannot create scene because " + e.getMessage());
        }
    }

    private void setupVideoPlayer() {
        String explanationVideoUrl = getClass().getResource("/assets/coding.mp4").toExternalForm();

        Media media = new Media(explanationVideoUrl);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        media.setOnError(() -> {
            System.out.println("Media error -> " + media.getError());
        });
        mediaPlayer.setOnError(() -> {
            System.out.println("MediaPlayer error -> " + mediaPlayer.getError());
        });

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(280);
        mediaView.setFitHeight(300);
        mediaView.setSmooth(true);
        mediaView.setOnError(event -> {
            System.out.println("MediaView error -> " + event.getMediaError().toString());
        });

        Button playButton = new Button();
        ImageView playIcon = new ImageView(new Image(getClass().getResource("/drawable/video_type_play_icon.png").toString()));
        playIcon.setFitHeight(45);
        playIcon.setFitWidth(45);
        ImageView pauseIcon = new ImageView(new Image(getClass().getResource("/drawable/video_type_pause_icon_1x.png").toString()));
        pauseIcon.setFitHeight(45);
        pauseIcon.setFitWidth(45);
        playButton.setGraphic(playIcon);
        playButton.setBackground(Background.EMPTY);
        playButton.setOnAction(event -> {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED) {
                mediaPlayer.play();
                playButton.setGraphic(pauseIcon);
            } else {
                mediaPlayer.pause();
                playButton.setGraphic(playIcon);
            }
        });

        StackPane.setAlignment(playButton, Pos.CENTER);

//        explanationVideo.getChildren().addAll(mediaView, playButton);

    }

    public InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data -> " + data);
        return data;
    }

    public static class InitialData {
        private List<PQSubject> subjects;
        private List<QuestionDescription> questionDescriptions;
        private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;
        private HashMap<String, Year> selectedSubjectYear;
        private Type questionType;
        private Screens previousScreen;


        public InitialData(List<PQSubject> subjects, List<QuestionDescription> questionDescriptions, HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions, HashMap<String, Year> selectedSubjectYear, Type questionType, Screens previousScreen) {
            this.subjects = subjects;
            this.questionDescriptions = questionDescriptions;
            this.subjectsQuestions = subjectsQuestions;
            this.selectedSubjectYear = selectedSubjectYear;
            this.questionType = questionType;
            this.previousScreen = previousScreen;
        }

        public List<PQSubject> getSubjects() {
            return subjects;
        }

        public List<QuestionDescription> getQuestionDescriptions() {
            return questionDescriptions;
        }

        public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
            return subjectsQuestions;
        }

        public HashMap<String, Year> getSelectedSubjectYear() {
            return selectedSubjectYear;
        }

        public Type getQuestionType() {
            return questionType;
        }

        public Screens getPreviousScreen() {
            return previousScreen;
        }
    }
}
