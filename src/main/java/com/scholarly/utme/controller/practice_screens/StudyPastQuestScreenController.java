package com.scholarly.utme.controller.practice_screens;

import com.scholarly.utme.controller.PQScreenController;
import com.scholarly.utme.data.model.*;
import com.scholarly.utme.data.model.newDb.ObjectiveQuestionDescription;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.data.model.newDb.TheoryQuestionDescription;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.practice_screens.PracticeScreenVM;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.SceneLifecycle;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@FxmlPath("/layouts/practice_screens/StudyPastQuestionsScreen.fxml")
public class StudyPastQuestScreenController implements FxmlView<StudyPastScreenVM>, Initializable, SceneLifecycle {
    private static final String TAG = "StudyPastQuestScreenController: ";

    @InjectViewModel
    private StudyPastScreenVM viewModel;

    @FXML
    private WebView questionWebView, explanationWebView, questionWithImageWebView;
    @FXML
    private HBox answerHeaderHBox, rightPane, quesDescriptionHBox, questionWithImageHBox, questionDescriptionHBox, questionLabelHBox;
    @FXML
    private VBox centerVBox, rightVBox, questionDescriptionDialog, explanationVBox, questionCenterVBox, questionVBox, optionsVBox;
    @FXML
    private TilePane tilePane;
    @FXML
    private ScrollPane tileScrollPane, explanationScrollPane, questionScrollPane;
    @FXML
    private StackPane answerPane, explanationPane, questionStackPane;
    @FXML
    private ListView<PQSubject> subjectList;
    @FXML
    private Line questionLine;
    @FXML
    private Label questionOverviewLabel, optionA, optionB, optionC, optionD, explanationLabel, explanationTitle, correctAnswerTitle, correctAnswerLabel, questionDescriptionLabel, appBarTitle, questionDescriptionText, questionLabel;
    @FXML
    private Button prevButton, nextButton, exitButton, hideAnswerButton, viewImageButton;
    @FXML
    private ToggleButton showAnswerButton, showExplanationButton;
    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, quesDescriptionCloseIcon, questionImage, explanationImage;
    @FXML
    private Pane dialogDimmer;
    @FXML
    private DialogPane exitDialogPane;

    @FXML
    private BorderPane practiceContentPane;
    @FXML
    private ProgressIndicator progressBar;

    private final String optionWebViewBC = "#FFFFFF;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Integer value to track subjectList subject selection
        AtomicInteger subjectListSelectionAtomicIndex = new AtomicInteger(0);

        showProgressBar();
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Task<Boolean> contentTask = new Task<>() {
            @Override
            protected Boolean call() {
                viewModel.processInitialData(getInitialData());
                return true;
            }
        };
        contentTask.setOnSucceeded(
                succeedEvent -> Platform.runLater(() -> {
                    subjectList.setCellFactory(new PracticeSubjectListCellFactory());
                    subjectList.setItems(viewModel.getSubjects());
                    subjectList.getSelectionModel().selectFirst();
                    subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super PQSubject>) c -> {
                        if (c.getList().size() == 1) {
                            PQSubject subject = c.getList().get(0);
                            viewModel.setSelectedSubject(subject);
                            appBarTitle.setText("STUDY QUESTIONS");
                            appBarTitle.setText(appBarTitle.getText() + "  " + viewModel.getSelectedSubjectYear().get(viewModel.getSelectedSubject().getTitle()).getYear());
                        } else {
                            viewModel.setSelectedSubject(null);
                        }
                    });

                    viewModel.setSelectedSubject(subjectList.getSelectionModel().getSelectedItem());
                    setupQuestionView(viewModel.getSelectedSubject());
                    setupTilePane(viewModel.getSelectedSubject());

                    appBarTitle.setText(appBarTitle.getText() + "  " + viewModel.getSelectedSubjectYear().get(viewModel.getSelectedSubject().getTitle()).getYear());

                    viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            setupQuestionView(newValue);
                            setupTilePane(newValue);
//                            updateBookmarkIcon();
                        }
                    });

                    viewModel.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
                        subjectQuestionsState.selectedQuestionProperty().addListener((observable, oldValue, newValue) -> {
                            if (viewModel.getSelectedSubject().getShortTitle().equalsIgnoreCase(s)) {
                                changeSelectedTile(oldValue.intValue(), newValue.intValue());
                                changeSelectedQuestion(newValue.intValue());
//                                updateBookmarkIcon();
                            }
                        });
                    });

                    subjectList.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<? super Integer>) observable -> {
                        if (observable.getList().size() == 1) {
                            subjectListSelectionAtomicIndex.set(observable.getList().get(0));
                        }
                    });

                    hideProgressBar();
                })
        );

        executorService.execute(contentTask);
        executorService.shutdown();

        initializeViews();
        initializeFont();

        prevButton.setOnAction(event -> {
            nextButton.setDisable(false);

            int selectedQuestionIndex = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            if (subjectList.getSelectionModel().getSelectedIndex() == 0) {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestionIndex - 1);

                if (selectedQuestionIndex == 2) {
                    prevButton.setDisable(true);
                }
            } else {
                if (selectedQuestionIndex == 1) {
                    subjectList.getSelectionModel().select(subjectListSelectionAtomicIndex.decrementAndGet());
                } else {
                    viewModel.getSubjectsQuestions()
                            .get(viewModel.getSelectedSubject().getShortTitle())
                            .setSelectedQuestion(selectedQuestionIndex - 1);
                }
            }

        });

        nextButton.setOnAction(event -> {
            prevButton.setDisable(false);
            StudyPastScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
            List<StudyPastScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

            int selectedQuestionIndex = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            if (selectedQuestionIndex == questions.size()) {
                subjectList.getSelectionModel().select(subjectListSelectionAtomicIndex.incrementAndGet());
                prevButton.setDisable(false);

            } else {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestionIndex + 1);

                int newSelectedQuestionIndex = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .getSelectedQuestion();

                if (newSelectedQuestionIndex == questions.size() && subjectList.getItems().size()-1 == subjectListSelectionAtomicIndex.get())
                    nextButton.setDisable(true);
            }
        });

        showAnswerButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();
            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {

                questionsState.getQuestions().get(selectedQuestion - 1).setShowAnswer(true);

            } else {

                questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(true);
            }
            updateExplanationView();

        });

        showExplanationButton.setOnAction(event -> {

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(true);

            updateExplanationView();
        });

        hideAnswerButton.setOnAction(event -> {

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(false);

            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationScrollPane.setVisible(false);
            explanationLabel.setVisible(false);
            explanationImage.setVisible(false);
            explanationWebView.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);
            hideAnswerButton.setVisible(false);

        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questionsState.getQuestions().get(selectedQuestion - 1).getQuestion();
                StringBuilder textToRead = new StringBuilder(currentQuestion.getQuestion());
                textToRead.append(". Option A, ").append(currentQuestion.getOptionA().getText());
                textToRead.append(". Option B, ").append(currentQuestion.getOptionB().getText());
                textToRead.append(". Option C, ").append(currentQuestion.getOptionC().getText());
                textToRead.append(". Option D, ").append(currentQuestion.getOptionD().getText());

                if (!Helper.isWebView(currentQuestion.getQuestion())) {
                    TextToSpeech.play(textToRead.toString());
                }

            } else {
                TheoryQuestion currentQuestion = (TheoryQuestion) questionsState.getQuestions().get(selectedQuestion - 1).getQuestion();
                String questionText = currentQuestion.getQuestion();
                TextToSpeech.play(questionText);
            }

        });

//        bookmarkImage.setOnMouseClicked(event -> {
//            viewModel.handleBookmarkClicked();
//            updateBookmarkIcon();
//        });


//        questionImageCloseIcon.setOnMouseClicked(event -> {
//            questionImageBox.setVisible(false);
//        });

        exitButton.setOnAction(event -> {
            showExitDialog();
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
//        tileScrollPane.setBackground(Background.EMPTY);
        showAnswerButton.setBackground(Background.EMPTY);
        showExplanationButton.setBackground(Background.EMPTY);

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
//        flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));
        quesDescriptionCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));

        ImageView prevImage = new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/prev_btn_icon.png").toString()));
        prevButton.setGraphic(prevImage);
        prevButton.setGraphicTextGap(15);
        ImageView nextImage = new ImageView(getClass().getResource("/drawable/practice_screen_images/next_btn_icon.png").toString());
        nextButton.setGraphic(nextImage);
        nextButton.setContentDisplay(ContentDisplay.RIGHT);
        nextButton.setGraphicTextGap(15);

    }

    private void initializeFont() {
        exitButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        hideAnswerButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }

    private void setupQuestionView(PQSubject selectedSubject) {
        System.out.println(TAG + "Setup QuestionView called!");
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestionNumber = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

//        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
//        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        prevButton.setDisable(subjectList.getSelectionModel().getSelectedIndex() == 0 && selectedQuestionNumber == 1);

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(selectedQuestionNumber - 1).getQuestion();

            List<ObjectiveQuestionDescription> quesDescriptionInList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

            String questionText = currentQuestion.getQuestion().replaceAll("<br>", System.lineSeparator());
            questionLabel.setText(questionText);

            questionCenterVBox.getChildren().removeAll(questionScrollPane, questionLabelHBox);
            questionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);

            if (questionText.contains("<img")) {
                questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView);
                if (!questionCenterVBox.getChildren().contains(questionWithImageHBox))
                    questionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(currentQuestion,true));
            } else {
                loadObjectiveQuestion(questionText, quesDescriptionInList, Helper.isWebView(currentQuestion, true));
            }

            Helper.loadOption(getClass(), optionA, currentQuestion.getOptionA().getText(), " (A) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionB, currentQuestion.getOptionB().getText(), " (B) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionC, currentQuestion.getOptionC().getText(), " (C) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionD, currentQuestion.getOptionD().getText(), " (D) ", optionWebViewBC);

            explanationTitle.setText("Explanation");
//            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());
            String correctAnswer = currentQuestion.getQuestionAnswer().getAnswer();
            showCorrectAnswer(correctAnswer, Helper.isWebView(correctAnswer));

            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();

            if (explanationText.contains("<img")) {
//                explanationVBox.getChildren().addAll(explanationTitle, explanationWebView);
//                String questionWithImageText = parseExplanationWithImageView(explanationText);
//                explanationWebView.getEngine().loadContent(questionWithImageText);
                loadExplanationWithImage(explanationText, Helper.isWebView(explanationText));
            } else {
//                explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));
                loadExplanation(explanationText, Helper.isWebView(explanationText));
            }

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(selectedQuestionNumber - 1).getQuestion();
            questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

            List<TheoryQuestionDescription> quesDescriptionList = viewModel.getTheoryQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

            String questionText = currentQuestion.getQuestion();
            questionText = questionText.replaceAll("<br>", System.lineSeparator());

            questionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);
            questionVBox.getChildren().removeAll(questionDescriptionHBox, questionLine);

            if (questionText.contains("<img")) {
                questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView);
                if (!questionCenterVBox.getChildren().contains(questionWithImageHBox))
                    questionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(currentQuestion, true));
            } else {
                loadTheoryQuestion(questionText, Helper.isWebView(currentQuestion, true));
            }

            centerVBox.getChildren().removeAll(optionsVBox);

            answerHeaderHBox.getChildren().remove(showExplanationButton);
            answerHeaderHBox.setAlignment(Pos.CENTER);

            rightVBox.getChildren().remove(answerPane);

            explanationTitle.setText("Answer");
            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));
            if (explanationText.contains("<img")) {
                loadExplanationWithImage(explanationText, Helper.isWebView(explanationText));
            } else {
                loadExplanation(explanationText, Helper.isWebView(explanationText));
            }

        }

        updateExplanationView();
    }

    private void changeSelectedQuestion(int newValue) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        prevButton.setDisable(subjectList.getSelectionModel().getSelectedIndex() == 0 && newValue == 1);

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(newValue - 1).getQuestion();
            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            List<ObjectiveQuestionDescription> quesDescriptionInList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

            String questionText = currentQuestion.getQuestion();

            questionLabel.setText(questionText);

            questionCenterVBox.getChildren().removeAll(questionScrollPane);
            questionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox, questionLabelHBox);

            if (questionText.contains("<img")) {
                questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView);
                if (!questionCenterVBox.getChildren().contains(questionWithImageHBox))
                    questionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(currentQuestion,true));
            } else {
                loadObjectiveQuestion(questionText, quesDescriptionInList, Helper.isWebView(currentQuestion, true));
            }

            Helper.loadOption(getClass(), optionA, currentQuestion.getOptionA().getText(), " (A) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionB, currentQuestion.getOptionB().getText(), " (B) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionC, currentQuestion.getOptionC().getText(), " (C) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionD, currentQuestion.getOptionD().getText(), " (D) ", optionWebViewBC);

            explanationTitle.setText("Explanation");

//            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());
            String correctAnswer = currentQuestion.getQuestionAnswer().getAnswer();
            showCorrectAnswer(correctAnswer, Helper.isWebView(correctAnswer));

            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
//            explanationVBox.getChildren().clear();
            if (explanationText.contains("<img")) {
//                explanationVBox.getChildren().addAll(explanationTitle, explanationWebView);
//                String explanationWithImageText = parseExplanationWithImageView(explanationText);
//                explanationWebView.getEngine().loadContent(explanationWithImageText);
                loadExplanationWithImage(explanationText, Helper.isWebView(explanationText));
            } else {
//                explanationVBox.getChildren().addAll(explanationTitle, explanationScrollPane);
//                explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));
                loadExplanation(explanationText, Helper.isWebView(explanationText));
            }

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(newValue - 1).getQuestion();
            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            List<TheoryQuestionDescription> quesDescriptionList = viewModel.getTheoryQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

            String questionText = currentQuestion.getQuestion();
            questionText = questionText.replaceAll("<br>", System.lineSeparator());

            questionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);
            questionVBox.getChildren().removeAll(questionDescriptionHBox, questionLine);

            if (questionText.contains("<img")) {
                questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView);
                if (!questionCenterVBox.getChildren().contains(questionWithImageHBox))
                    questionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(currentQuestion, currentQuestion.getIsQuestionWebView() != 1));
            } else {
                loadTheoryQuestion(questionText, Helper.isWebView(currentQuestion, currentQuestion.getIsQuestionWebView() != 1));
            }

            centerVBox.getChildren().removeAll(optionsVBox);

            rightVBox.getChildren().remove(answerPane);

            explanationTitle.setText("Answer");

            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));

            if (explanationText.contains("<img")) {
                loadExplanationWithImage(explanationText, Helper.isWebView(explanationText));
            } else {
                loadExplanation(explanationText, Helper.isWebView(explanationText));
            }

        }

//        updateExplanationView();

    }

    private void loadObjectiveQuestion(String questionText, List<ObjectiveQuestionDescription> quesDescriptionList, boolean isWebView) {
        if (isWebView) {
            System.out.println(TAG + "Question is WebView!");
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
                questionScrollPane.setMinHeight(120);
                questionWebView.setMinHeight(100);
            }
        } else {
            questionCenterVBox.getChildren().removeAll(questionWebView);

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
            questionLabel.setText(questionText);
        }
    }

    private void loadTheoryQuestion(String question, boolean webView) {
        if (webView) {
            questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView, questionWithImageHBox);
            questionCenterVBox.getChildren().add(questionWebView);

            String content = Helper.loadLatex(getClass(), question);
            questionWebView.getEngine().loadContent(content);
            questionWebView.setMinHeight(200);
        } else {
            questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWebView, questionWithImageHBox);
            questionCenterVBox.getChildren().add(questionScrollPane);

            questionLabel.setText(question);
        }
    }

    private void loadExplanation(String explanation, Boolean isWebView) {
        explanationVBox.getChildren().removeAll(explanationTitle, explanationScrollPane, explanationImage, explanationWebView);
        if (isWebView) {
            explanationVBox.getChildren().addAll(explanationTitle, explanationWebView);
            String content = Helper.loadLatex(getClass(), explanation);
            explanationWebView.getEngine().loadContent(content);
        } else {
            explanationVBox.getChildren().addAll(explanationTitle, explanationScrollPane);
            explanationLabel.setText(explanation.replaceAll("<br>", System.lineSeparator()));
        }
    }

    private void showCorrectAnswer(String answer, Boolean isWebView) {
        if (isWebView) {
            String content = Helper.loadLatex(getClass(), answer);
            WebView webView = new WebView();
            webView.getEngine().loadContent(content);
            correctAnswerLabel.setGraphic(webView);
            correctAnswerLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            correctAnswerLabel.setGraphic(null);
            correctAnswerLabel.setContentDisplay(ContentDisplay.TEXT_ONLY);
            correctAnswerLabel.setText(answer.replaceAll("<br>", System.lineSeparator()));
        }
    }

    private void loadExplanationWithImage(String explanation, Boolean isWebView) {
        explanationVBox.getChildren().removeAll(explanationTitle, explanationScrollPane, explanationImage, explanationWebView);

        explanationVBox.getChildren().addAll(explanationTitle, explanationImage, explanationWebView);

        String answer = extractAnswerFromExplanation(explanation, isWebView);
        String imageUrl = extractImageUrlFromExplanation(explanation);


        explanationWebView.setVisible(true);
        explanationWebView.getEngine().loadContent(answer);
        explanationImage.setImage(new Image(getClass().getResource(imageUrl).toString()));

//        explanationWebView.setVisible(false);
//        explanationLabel.setVisible(false);
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

    private void showQuestionWithImage(String questionWithImageText, boolean isWebView) {
//        System.out.println(TAG + "Question with image -> " + questionWithImageText);
        questionWithImageText = Helper.loadPQImageUrl(getClass(), questionImage, questionWithImageWebView, questionWithImageText, isWebView);
    }

    private String parseExplanationWithImageView(String explanationWithImageText) {
        return Helper.parsePQImageUrl(getClass(), explanationWithImageText);
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

    private void updateBookmarkIcon() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion selectedQuestion = (ObjectiveQuestion) questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion();

            List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks().get(viewModel.getSelectedSubject().getId());

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
//                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion selectedQuestion = (TheoryQuestion) questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion();

            List<TheoryBookmark> bookmarks = viewModel.getTheoryBookmarks().get(viewModel.getSelectedSubject().getId());

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
//                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });
        }

    }

    private void  updateExplanationView() {
        SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                .get(viewModel.getSelectedSubject().getShortTitle());

        QuestionState questionState = questionsState.getQuestions().get(questionsState.getSelectedQuestion() - 1);

        if (!questionState.isShowExplanation() && !questionState.isShowAnswer()) {
            System.out.println(TAG + "not showing answer or explanation view");

            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationScrollPane.setVisible(false);
            explanationLabel.setVisible(false);
            explanationImage.setVisible(false);
            explanationWebView.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);

            hideAnswerButton.setVisible(false);

        } else if (questionState.isShowExplanation()) {
            System.out.println(TAG + "showing explanation and answer view");

            explanationTitle.setVisible(true);
            explanationScrollPane.setVisible(true);
            explanationLabel.setVisible(true);
            explanationWebView.setVisible(true);
            explanationImage.setVisible(true);
            showExplanationButton.setDisable(true);

            correctAnswerLabel.setVisible(true);
            correctAnswerTitle.setVisible(true);
            showAnswerButton.setDisable(true);

            if (explanationTitle.getText().contains("Answer"))
                hideAnswerButton.setText("Hide Answer");
            else
                hideAnswerButton.setText("Hide Explanation");
            hideAnswerButton.setVisible(true);

        } else if (questionState.isShowAnswer()) {
            System.out.println(TAG + "showing show answer view alone");

            correctAnswerLabel.setVisible(true);
            correctAnswerTitle.setVisible(true);
            showAnswerButton.setDisable(true);
            showExplanationButton.setDisable(false);

            hideAnswerButton.setText("Hide Answer");
            hideAnswerButton.setVisible(true);
        }
    }

    private void setupTilePane(PQSubject selectedSubject) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.setVgap(10);
        tilePane.setHgap(10);
        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(30, 30);
//            r.setStroke(Paint.valueOf("#12AF20"));
            r.setStroke(Color.GRAY);
//            r.setFill(Color.web("#ededed"));
            r.setFill(Color.web("#FFFFFF"));
            r.setStrokeType(StrokeType.OUTSIDE);

            Label l = new Label(Integer.toString(i));
            if (subjectQuestionsState.getSelectedQuestion() == i) {
                r.setFill(Paint.valueOf("#12AF20"));
                l.setTextFill(Color.WHITE);
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

    private void changeSelectedTile(int oldSelectedQuestion, int newSelectedQuestion) {

        StackPane selectedQuestionPane = (StackPane) tilePane.getChildren().get(newSelectedQuestion - 1);
        StackPane oldQuestionPane = (StackPane) tilePane.getChildren().get(oldSelectedQuestion - 1);

        Rectangle oldQuestionRectangle = (Rectangle) oldQuestionPane.getChildren().get(0);
        Rectangle selectedQuestionRectangle = (Rectangle) selectedQuestionPane.getChildren().get(0);
        Label selectedQuestionText = (Label) selectedQuestionPane.getChildren().get(1);
        Label oldQuestionText = (Label) oldQuestionPane.getChildren().get(1);

        oldQuestionRectangle.setFill(Paint.valueOf("#EDEDED"));
        oldQuestionText.setTextFill(Color.BLACK);
        selectedQuestionRectangle.setFill(Paint.valueOf("#12AF20"));
        selectedQuestionText.setTextFill(Color.WHITE);

    }

    private void showExitDialog() {
        Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Exit", null, "Are you sure you want to quit?");

        dialogDimmer.setVisible(true);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.YES) {
                dialogDimmer.setVisible(false);
                Helper.moveToPQScreen(Screens.PAST_QUESTION_SCREEN, null);
//                ViewSwitcher.passData(new PQScreenController.InitialData(Screens.PAST_QUESTION_SCREEN, null));
//                ViewSwitcher.showScreen(View.PQ_SCREEN);
            }
            dialogDimmer.setVisible(false);
            return buttonType;
        });

        dialog.show();

    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        practiceContentPane.setVisible(true);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        practiceContentPane.setVisible(false);
    }


    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        return data;
    }

    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {
        TextToSpeech.dispose();
    }

    public static class InitialData {
        public List<SubjectListItemVM.SubjectState> questionData;

        public InitialData(List<SubjectListItemVM.SubjectState> questionData) {
            this.questionData = questionData;
        }
    }
}
