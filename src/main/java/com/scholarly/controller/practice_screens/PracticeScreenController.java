package com.scholarly.controller.practice_screens;

import com.scholarly.controller.PQScreenController;
import com.scholarly.data.model.*;
import com.scholarly.data.model.newDb.ObjectiveQuestionDescription;
import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.data.model.newDb.TheoryQuestionDescription;
import com.scholarly.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.ui.utils.*;
import com.scholarly.util.Helper;
import com.scholarly.util.TextToSpeech;
import com.scholarly.viewmodels.practice_screens.PracticeScreenVM;
import com.scholarly.viewmodels.practice_screens.PracticeScreenVM.QuestionState;
import com.scholarly.viewmodels.practice_screens.PracticeScreenVM.SubjectQuestionsState;
import com.scholarly.viewmodels.SubjectListItemVM;
import com.scholarly.viewmodels.SubjectListItemVM.SubjectState;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@FxmlPath("/layouts/practice_screens/PracticeScreen.fxml")
public class PracticeScreenController implements FxmlView<PracticeScreenVM>, Initializable, SceneLifecycle {
    private static final String TAG = "PracticeScreenController: ";

    private final static double rectangleSelectedBorderWidth = 1.0;
    private final static Color rectangleBorderSelectedColor = Color.ORANGE;
    private final static Paint rectangleSelectedColor = Paint.valueOf("#9CFC9C");

    @InjectViewModel
    private PracticeScreenVM viewModel;

    @FXML
    private TilePane tilePane;
    @FXML
    private StackPane questionStackPane;
    @FXML
    private ScrollPane tileScrollPane, objScrollPane, theoryScrollPane;
    @FXML
    private ListView<PQSubject> subjectList;
    @FXML
    private RadioButton optionAButton, optionBButton, optionCButton, optionDButton;
    @FXML
    private Label questionOverviewLabel, timeLabel, scoreText, questionDescriptionLabel, readQuestionDesc, questionDescriptionText, questionLabel, appBarTitle, attemptedLabel, theoryQuestionLabel;
    @FXML
    private TextField enterCorrectAnswerField;
    @FXML
    private Line questionLine;
    @FXML
    private CheckBox questionErrorCheckBox, incorrectAnswerCheckBox, okayCheckBox;
    @FXML
    private VBox incorrectAnswerPane, reportDialog, testSummaryDialog, centerVBox, questionDescriptionDialog, objQuestionCenterVBox, questionVBox, questionWithImageVBox, theoryQuestionCenterVBox, calculatorButton;
    @FXML
    private HBox quesDescriptionHBox, questionWithImageHBox, questionDescriptionHBox, questionLabelHBox;
    @FXML
    private Button prevButton, nextButton, exitButton, submitButton, submitReport, homePageButton, resultAnalysisButton, viewImageButton;
    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, reportDialogCloseIcon, quesDescriptionCloseIcon, timeImage, summaryBookImage, testSummaryCloseIcon, questionImage, optionAImage;
    @FXML
    private Pane dialogDimmer, exitDialogDimmer, summaryDialogDimmer;
    @FXML
    private WebView questionWebView, optionAWebView, questionWithImageWebView;
    @FXML
    private BorderPane practiceContentPane;

    @FXML
    private ProgressIndicator progressBar;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    double totalScore = 0;
    double totalQuestions = 0;

    private final String optionWebViewBC = "#F3FBF4;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Integer value to track subjectList subject selection
        AtomicInteger subjectListSelectionAtomicIndex = new AtomicInteger(0);

        showProgressBar();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

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
                    subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super PQSubject>) change -> {
                        if (change.getList().size() == 1) {
                            PQSubject subject = change.getList().get(0);
                            System.out.println(TAG + "Content of change -> " + change);
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
                            updateBookmarkIcon();
                        }
                    });

                    viewModel.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
                        subjectQuestionsState.selectedQuestionProperty().addListener((observable, oldValue, newValue) -> {
                            if (viewModel.getSelectedSubject().getShortTitle().equalsIgnoreCase(s)) {
                                if (newValue.intValue() <= subjectQuestionsState.getQuestions().size()){
                                    changeSelectedTile(oldValue.intValue(), newValue.intValue());
                                    changeSelectedQuestion(newValue.intValue());
                                    updateBookmarkIcon();
                                }
                            }
                        });
                    });

                    subjectList.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<? super Integer>) observable -> {
                        if (observable.getList().size() == 1) {
                            subjectListSelectionAtomicIndex.set(observable.getList().get(0));
                        }
                    });


                    if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                        optionAButton.setOnAction(event -> {
                            int selectedQuestion = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getSelectedQuestion();



                            QuestionState questionState = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getQuestions()
                                    .get(selectedQuestion - 1);

                            ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

//                questionState.setSelectedOption(question.getOptionA());
                            questionState.setSelectedOptionId(question.getOptionA().getId());

                            if (questionState.getSelectedOptionId() == -1) {
                                onOptionSelected(selectedQuestion);
                            }

                        });
                        optionAButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                int selectedQuestion = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getSelectedQuestion();

                                QuestionState questionState = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getQuestions()
                                        .get(selectedQuestion - 1);

                                ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

                                if (questionState.getSelectedOptionId() == -1) {
                                    onOptionSelected(selectedQuestion);
                                }

//                    questionState.setSelectedOption(question.getOptionA());
                                questionState.setSelectedOptionId(question.getOptionA().getId());
                            }
                        });

                        optionBButton.setOnAction(event -> {
                            int selectedQuestion = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getSelectedQuestion();

                            QuestionState questionState = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getQuestions()
                                    .get(selectedQuestion - 1);

                            ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

                            if (questionState.getSelectedOptionId() == -1) {
                                onOptionSelected(selectedQuestion);
                            }

//                questionState.setSelectedOption(question.getOptionB());
                            questionState.setSelectedOptionId(question.getOptionB().getId());
                        });
                        optionBButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                int selectedQuestion = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getSelectedQuestion();

                                QuestionState questionState = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getQuestions()
                                        .get(selectedQuestion - 1);

                                ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

                                if (questionState.getSelectedOptionId() == -1) {
                                    onOptionSelected(selectedQuestion);
                                }

//                    questionState.setSelectedOption(question.getOptionB());
                                questionState.setSelectedOptionId(question.getOptionB().getId());
                            }
                        });

                        optionCButton.setOnAction(event -> {
                            int selectedQuestion = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getSelectedQuestion();

                            QuestionState questionState = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getQuestions()
                                    .get(selectedQuestion - 1);

                            ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

                            if (questionState.getSelectedOptionId() == -1) {
                                onOptionSelected(selectedQuestion);
                            }

//                questionState.setSelectedOption(question.getOptionC());
                            questionState.setSelectedOptionId(question.getOptionC().getId());
                        });
                        optionCButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                int selectedQuestion = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getSelectedQuestion();

                                QuestionState questionState = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getQuestions()
                                        .get(selectedQuestion - 1);

                                ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

                                if (questionState.getSelectedOptionId() == -1) {
                                    onOptionSelected(selectedQuestion);
                                }

//                    questionState.setSelectedOption(question.getOptionC());
                                questionState.setSelectedOptionId(question.getOptionC().getId());
                            }
                        });

                        optionDButton.setOnAction(event -> {
                            int selectedQuestion = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getSelectedQuestion();

                            QuestionState questionState = viewModel.getSubjectsQuestions()
                                    .get(viewModel.getSelectedSubject().getShortTitle())
                                    .getQuestions()
                                    .get(selectedQuestion - 1);

                            ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

                            if (questionState.getSelectedOptionId() == -1) {
                                onOptionSelected(selectedQuestion);
                            }

//                questionState.setSelectedOption(question.getOptionD());
                            questionState.setSelectedOptionId(question.getOptionD().getId());
                        });
                        optionDButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                int selectedQuestion = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getSelectedQuestion();

                                QuestionState questionState = viewModel.getSubjectsQuestions()
                                        .get(viewModel.getSelectedSubject().getShortTitle())
                                        .getQuestions()
                                        .get(selectedQuestion - 1);

                                ObjectiveQuestion question = (ObjectiveQuestion) questionState.getQuestion();

                                if (questionState.getSelectedOptionId() == -1) {
                                    onOptionSelected(selectedQuestion);
                                }

//                    questionState.setSelectedOption(question.getOptionD());
                                questionState.setSelectedOptionId(question.getOptionD().getId());
                            }
                        });

                    } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
                        centerVBox.getChildren().removeAll(optionAButton, optionBButton, optionCButton, optionDButton);
                    }

                    viewModel.timeProperty().addListener((observable, oldValue, newValue) -> {
                        String timeText = "";

                        long hours = newValue.longValue() / 3600;
                        long minutes = newValue.longValue() % 3600 / 60;
                        long secs = newValue.longValue() % 60;

                        if (hours > 0) {
                            timeText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                        } else {
                            timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                        }

                        timeLabel.setText(timeText);

                        if (newValue.intValue() == 0) {
                            showTimeUpDialog();
                        }

                        // TODO: Implement time elapsed here
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
            SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
            List<QuestionState> questions = subjectQuestionsState.getQuestions();

            int selectedQuestionIndex = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            if (selectedQuestionIndex == questions.size()){
                subjectList.getSelectionModel().select(subjectListSelectionAtomicIndex.incrementAndGet());
                prevButton.setDisable(false);

            } else {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestionIndex + 1);

                int newSelectedQuestionIndex = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .getSelectedQuestion();

                if (newSelectedQuestionIndex == questions.size() && subjectList.getItems().size()-1 == subjectListSelectionAtomicIndex.get()) {
                    nextButton.setDisable(true);
                }
            }

        });

        toggleGroup.getToggles().addAll(optionAButton, optionBButton, optionCButton, optionDButton);

        exitButton.setOnAction(event -> showExitDialog());

        submitButton.setOnAction(event -> {
            showSubmitDialog();

            viewModel.getResults().forEach(result -> {
                 totalScore += result.getCorrectAnswers();
                 totalQuestions += result.getTotalQuestions();
            });

            double newTotalScore = (totalScore/totalQuestions) * 400;
            scoreText.setText((int)newTotalScore + "/" + 400);
        });

//        testSummaryCloseIcon.setOnMouseClicked(event -> {
//            Animations.hideDialog(testSummaryDialog, summaryDialogDimmer);
//
//        });

        homePageButton.setOnAction(event -> {
            Animations.hideDialog(testSummaryDialog, summaryDialogDimmer);
            Helper.moveToPQScreen(Screens.PRACTICE_SCREEN, null);
        });

        resultAnalysisButton.setOnAction(event -> {
            ResultScreenController.InitialData initialData =
                    new ResultScreenController.InitialData(viewModel.getResults(), viewModel.getSubjects(), viewModel.getQuestionDescriptions(), viewModel.getSubjectsQuestions(), viewModel.getSelectedSubjectYear(), View.PQ_SCREEN);

            ViewSwitcher.passData(initialData);
            ViewSwitcher.showScreen(View.RESULT_SCREEN);
        });

//        bookmarkImage.setOnMouseClicked(mouseEvent -> {
//            viewModel.handleBookmarkClicked();
//            updateBookmarkIcon();
//
//        });

        /*viewImageButton.setOnAction(event -> {
            if (viewImageButton.getText().contains("View Question")) {
                questionImageBox.setVisible(false);
                viewImageButton.setText("View Image");
            } else {
                questionImageBox.setVisible(true);
                viewImageButton.setText("View Question");
            }
        });*/

        reportDialogCloseIcon.setOnMouseClicked(mouseEvent -> {
            Animations.hideDialog(reportDialog, dialogDimmer);
        });

//        flagImage.setOnMouseClicked(mouseEvent -> {
//            Animations.showDialog(reportDialog, dialogDimmer);
//        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestionNumber = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getQuestions()
                    .get(selectedQuestionNumber - 1);

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
               // System.out.println("Question: " + ((ObjectiveQuestion) questionState.getQuestion()).getQuestion());
                ObjectiveQuestion selectedQuestion = ((ObjectiveQuestion) questionState.getQuestion());

                StringBuilder textToRead = new StringBuilder(selectedQuestion.getQuestion());
                textToRead.append(". Option A, ").append(selectedQuestion.getOptionA().getText());
                textToRead.append(". Option B, ").append(selectedQuestion.getOptionB().getText());
                textToRead.append(". Option C, ").append(selectedQuestion.getOptionC().getText());
                textToRead.append(". Option D, ").append(selectedQuestion.getOptionD().getText());
                if (!Helper.isWebView(selectedQuestion.getQuestion())) {
                    TextToSpeech.play(textToRead.toString());
                }
            } else {
                TextToSpeech.play(((TheoryQuestion) questionState.getQuestion()).getQuestion());
            }
        });

//        readQuestionDesc.setOnMouseClicked(mouseEvent -> {
//            Animations.showDialog(questionDescriptionDialog, dialogDimmer);
//        });
//        readQuestionDesc.setOnMouseEntered(event -> {
//            readQuestionDesc.setUnderline(true);
//        });
//        readQuestionDesc.setOnMouseExited(event -> {
//            readQuestionDesc.setUnderline(false);
//        });

        quesDescriptionCloseIcon.setOnMouseClicked(mouseEvent -> {
            Animations.hideDialog(questionDescriptionDialog, dialogDimmer);
        });


        /******************** Report Question Section ************************/

        incorrectAnswerPane.getChildren().remove(enterCorrectAnswerField);

        questionErrorCheckBox.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    submitReport.setDisable(!newValue);

                    // Ensure okayCheckBox is not selected
                    if (okayCheckBox.isSelected()){
                        okayCheckBox.setSelected(false);
                        submitReport.setDisable(false);
                    }
                    if (incorrectAnswerCheckBox.isSelected()) {
                        submitReport.setDisable(false);
                    }

                });

        incorrectAnswerCheckBox.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    submitReport.setDisable(!newValue);

                    // Ensure okayCheckBox is not selected
                    if (okayCheckBox.isSelected()){
                        okayCheckBox.setSelected(false);
                        questionErrorCheckBox.setSelected(false);
                        submitReport.setDisable(false);
                    }
                    if (questionErrorCheckBox.isSelected()){
                        submitReport.setDisable(false);
                    }

                    if (newValue){
                        incorrectAnswerPane.getChildren().add(enterCorrectAnswerField);

                    }else {
                        incorrectAnswerPane.getChildren().remove(enterCorrectAnswerField);
                    }
                });

        okayCheckBox.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    submitReport.setDisable(!newValue);

                    // Ensure only okayCheckBox can be selected at a time
                    if (newValue) {
                        if (questionErrorCheckBox.isSelected() || incorrectAnswerCheckBox.isSelected()){
                            questionErrorCheckBox.setSelected(false);
                            incorrectAnswerCheckBox.setSelected(false);
                            okayCheckBox.setSelected(true);
                            submitReport.setDisable(false);
                        }
                    }
                });
    }

    private void initializeViews() {
        subjectList.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
//        tileScrollPane.setBackground(Background.EMPTY);
        homePageButton.setBackground(Background.EMPTY);

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
        reportDialogCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        quesDescriptionCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
//        flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));
        timeImage.setImage(new Image(getClass().getResource("/drawable/timer_clock.png").toString()));

        ImageView prevImage = new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/prev_btn_icon.png").toString()));
        prevButton.setGraphicTextGap(15);
        prevButton.setGraphic(prevImage);
        ImageView nextImage = new ImageView(getClass().getResource("/drawable/practice_screen_images/next_btn_icon.png").toString());
        nextButton.setContentDisplay(ContentDisplay.RIGHT);
        nextButton.setGraphicTextGap(15);
        nextButton.setGraphic(nextImage);

        summaryBookImage.setImage(new Image(getClass().getResource("/drawable/practice_screen_images/summary_book_image.jpg").toString()));
//        testSummaryCloseIcon.setImage(new Image(getClass().getResource("/drawable/practice_screen_images/summary_close_icon.png").toString()));
    }

    private void initializeFont() {
        exitButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        submitButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }

    private void onOptionSelected(int selectedQuestionIndex) {
        StackPane selectedQuestionPane = (StackPane) tilePane.getChildren().get(selectedQuestionIndex - 1);

        Rectangle selectedQuestionRectangle = (Rectangle) selectedQuestionPane.getChildren().get(0);
        Label selectedQuestionText = (Label) selectedQuestionPane.getChildren().get(1);

        selectedQuestionRectangle.setFill(rectangleSelectedColor);
//        selectedQuestionText.setTextFill(Color.WHITE);

        updateAttemptedQuestions(optionAButton, optionBButton, optionCButton, optionDButton);

        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        attemptedLabel.setText("Attempted " + subjectQuestionsState.getNumOfAttempts() + " of " + questions.size());

    }

    private void updateAttemptedQuestions(RadioButton optionAButton, RadioButton optionBButton, RadioButton optionCButton, RadioButton optionDButton) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        if (optionAButton.isSelected() || optionBButton.isSelected() || optionCButton.isSelected() || optionDButton.isSelected()) {
            subjectQuestionsState.setNumOfAttempts(subjectQuestionsState.getNumOfAttempts() + 1);
        }
    }

    private void setupQuestionView(PQSubject selectedSubject) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestionIndex = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + selectedQuestionIndex + " of " + questions.size());
        attemptedLabel.setText("Attempted " + subjectQuestionsState.getNumOfAttempts() + " of " + questions.size());

        prevButton.setDisable(subjectList.getSelectionModel().getSelectedIndex() == 0 && selectedQuestionIndex == 1);

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion question = (ObjectiveQuestion) questions.get(selectedQuestionIndex - 1).getQuestion();

            List<ObjectiveQuestionDescription> quesDescriptionInList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == question.getQuestionDescriptionId()).toList();

            String questionText = question.getQuestion().replaceAll("<br>", System.lineSeparator());
            questionLabel.setText(questionText);

            objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionLabelHBox);
            objQuestionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);

            if (questionText.contains("<img")) {
                objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWebView);
                if (!objQuestionCenterVBox.getChildren().contains(questionWithImageHBox))
                    objQuestionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(question, true));
            } else {
                loadObjectiveQuestion(questionText, quesDescriptionInList, Helper.isWebView(question, true));
            }

            Helper.loadOption(getClass(), optionAButton, question.getOptionA().getText(), " (A) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionBButton, question.getOptionB().getText(), " (B) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionCButton, question.getOptionC().getText(), " (C) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionDButton, question.getOptionD().getText(), " (D) ", optionWebViewBC);

            int selectedOptionId = questions.get(selectedQuestionIndex - 1).getSelectedOptionId();
            if (selectedOptionId != -1) {
                if (selectedOptionId == question.getOptionA().getId()) {
                    toggleGroup.selectToggle(optionAButton);
                } else if (selectedOptionId == question.getOptionB().getId()) {
                    toggleGroup.selectToggle(optionBButton);
                } else if (selectedOptionId == question.getOptionC().getId()) {
                    toggleGroup.selectToggle(optionCButton);
                } else if (selectedOptionId == question.getOptionD().getId()) {
                    toggleGroup.selectToggle(optionDButton);
                }
            } else {
                toggleGroup.selectToggle(null);
            }
        } else {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(selectedQuestionIndex - 1).getQuestion();
            questionOverviewLabel.setText("Question " + selectedQuestionIndex + " of " + questions.size());

            List<TheoryQuestionDescription> quesDescriptionList = viewModel.getTheoryQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

//            questionWebView.getEngine().loadContent(question.getQuestion());

            String questionText = currentQuestion.getQuestion();
            questionText = questionText.replaceAll("<br>", System.lineSeparator());

            objQuestionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);
            questionVBox.getChildren().removeAll(questionDescriptionHBox, questionLine);

            if (questionText.contains("<img")) {
//                questionStackPane.getChildren().add(questionWithImageHBox);
                objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWebView);
                if (!objQuestionCenterVBox.getChildren().contains(questionWithImageHBox))
                    objQuestionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(currentQuestion, true));
            } else {
                loadTheoryQuestion(questionText, Helper.isWebView(currentQuestion, true));
            }
        }
    }

    private void changeSelectedQuestion(int newValue) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        prevButton.setDisable(subjectList.getSelectionModel().getSelectedIndex() == 0 && newValue == 1);

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion question = (ObjectiveQuestion) questions.get(newValue - 1).getQuestion();
            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            List<ObjectiveQuestionDescription> quesDescriptionList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == question.getQuestionDescriptionId()).toList();

            String questionText = question.getQuestion().replaceAll("<br>", System.lineSeparator());

            objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWebView, questionWithImageVBox, questionLabelHBox);

            if (questionText.contains("<img")) {
                System.out.println(TAG + "Objective Question contains image");
                objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWebView, questionLabelHBox);
                if (!objQuestionCenterVBox.getChildren().contains(questionWithImageVBox))
                    objQuestionCenterVBox.getChildren().add(questionWithImageVBox);
                showQuestionWithImage(questionText, Helper.isWebView(question, true));
            } else {
                loadObjectiveQuestion(questionText, quesDescriptionList, Helper.isWebView(question, question.getIsQuestionWebView() != 1));
            }

            Helper.loadOption(getClass(), optionAButton, question.getOptionA().getText(), " (A) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionBButton, question.getOptionB().getText(), " (B) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionCButton, question.getOptionC().getText(), " (C) ", optionWebViewBC);
            Helper.loadOption(getClass(), optionDButton, question.getOptionD().getText(), " (D) ", optionWebViewBC);

            int selectedOptionId = questions.get(newValue - 1).getSelectedOptionId();
            if (selectedOptionId != -1) {
                if (selectedOptionId == question.getOptionA().getId()) {
                    toggleGroup.selectToggle(optionAButton);
                } else if (selectedOptionId == question.getOptionB().getId()) {
                    toggleGroup.selectToggle(optionBButton);
                } else if (selectedOptionId == question.getOptionC().getId()) {
                    toggleGroup.selectToggle(optionCButton);
                } else if (selectedOptionId == question.getOptionD().getId()) {
                    toggleGroup.selectToggle(optionDButton);
                }
            } else {
                toggleGroup.selectToggle(null);
            }
        } else {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(newValue - 1).getQuestion();
            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            List<TheoryQuestionDescription> quesDescriptionList = viewModel.getTheoryQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

            String questionText = currentQuestion.getQuestion();
            questionText = questionText.replaceAll("<br>", System.lineSeparator());

            objQuestionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);
            questionVBox.getChildren().removeAll(questionDescriptionHBox, questionLine);

            if (questionText.contains("<img")) {
                objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWebView);
                if (!objQuestionCenterVBox.getChildren().contains(questionWithImageHBox))
                    objQuestionCenterVBox.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText, Helper.isWebView(currentQuestion, currentQuestion.getIsQuestionWebView() != 1));
            } else {
                loadTheoryQuestion(questionText, Helper.isWebView(currentQuestion, currentQuestion.getIsQuestionWebView() != 1));
            }

            questionWebView.getEngine().loadContent(currentQuestion.getQuestion());

            if (questionText.contains("<img")) {
                showQuestionWithImage(questionText, Helper.isWebView(currentQuestion, true));
            } else {
                loadTheoryQuestion(questionText, Helper.isWebView(currentQuestion, true));
            }
        }
    }

    private void setupTilePane(PQSubject selectedSubject) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.setVgap(10);
        tilePane.setHgap(10);
        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(35, 35);
            r.setStroke(Color.GRAY);
            r.setFill(Color.web("#FFFFFF"));
            r.setStrokeType(StrokeType.OUTSIDE);

            Label l = new Label(Integer.toString(i));

            if (questions.get(i-1).getSelectedOptionId() != -1) {
                r.setFill(Paint.valueOf("#12AF20"));
                l.setTextFill(Color.WHITE);
            }

            if (subjectQuestionsState.getSelectedQuestion() == i) {
                r.setStroke(Color.ORANGE);
                r.setStrokeWidth(1.0);
            }
            StackPane s = new StackPane(r, l);
            s.setStyle("-fx-cursor: hand;");
            tilePane.getChildren().add(s);

            int finalI = i;
            s.setOnMouseClicked(event -> {
                if (finalI < subjectQuestionsState.getSelectedQuestion() && nextButton.isDisable()) {
                    nextButton.setDisable(false);
                }

                if (finalI == questions.size() && subjectList.getItems().size() - 1 == subjectList.getSelectionModel().getSelectedIndex()) {
                    nextButton.setDisable(true);
                }

                subjectQuestionsState.setSelectedQuestion(finalI);
            });
        }
    }

    private void changeSelectedTile(int oldSelectedQuestionIndex, int newSelectedQuestionIndex) {
        StackPane selectedQuestionPane = (StackPane) tilePane.getChildren().get(newSelectedQuestionIndex - 1);
        StackPane oldQuestionPane = (StackPane) tilePane.getChildren().get(oldSelectedQuestionIndex - 1);

        Rectangle selectedQuestionRectangle = (Rectangle) selectedQuestionPane.getChildren().get(0);
        Rectangle oldQuestionRectangle = (Rectangle) oldQuestionPane.getChildren().get(0);

        selectedQuestionRectangle.setStroke(rectangleBorderSelectedColor);
        selectedQuestionRectangle.setStrokeWidth(rectangleSelectedBorderWidth);

        oldQuestionRectangle.setStroke(Color.GRAY);
        oldQuestionRectangle.setStrokeWidth(1.0);

    }

    private void updateBookmarkIcon() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion selectedQuestion = questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getObjectiveQuestion();

//            List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks().get(viewModel.getSelectedSubject().getId());
//
//            bookmarks.forEach(bookmark -> {
//                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
////                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
//
//                }
//            });

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion selectedQuestion = questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getTheoryQuestion();

//            List<TheoryBookmark> bookmarks = viewModel.getTheoryBookmarks().get(viewModel.getSelectedSubject().getId());
//
//            bookmarks.forEach(bookmark -> {
//                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
////                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
//
//                }
//            });
        }
    }

    private void showQuestionWithImage(String questionWithImageText, boolean isWebView) {
        String imageQuestion = Helper.extractQuestionOrAnswerFromQuestionWithImage(getClass(), questionWithImageText);
        String imageUrl = Helper.extractImageUrlFromText(questionWithImageText);

        System.out.println(TAG + "Image Question -> " + imageQuestion);
        System.out.println(TAG + "Image Url -> " + imageUrl);

        questionWithImageWebView.getEngine().loadContent(imageQuestion);
        questionImage.setImage(new Image(Objects.requireNonNull(getClass().getResource(imageUrl)).toString()));
    }

    private void loadObjectiveQuestion(String questionText, List<ObjectiveQuestionDescription> quesDescriptionList, boolean isWebView) {
        if (isWebView) {
            System.out.println(TAG + "Question is WebView!");
            objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWithImageHBox);

            if (!quesDescriptionList.isEmpty()) {
                if (!objQuestionCenterVBox.getChildren().contains(objScrollPane)) {
                    objQuestionCenterVBox.getChildren().add(objScrollPane);
                    objScrollPane.setMinHeight(180);
                    questionDescriptionLabel.setText(quesDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
                }
            }

            if (!objQuestionCenterVBox.getChildren().contains(questionWebView))
                objQuestionCenterVBox.getChildren().add(questionWebView);
            String content = Helper.loadLatex(getClass(), questionText);
            questionWebView.getEngine().loadContent(content);
            questionWebView.setMinHeight(100);

            if (!quesDescriptionList.isEmpty()) {
                objScrollPane.setMinHeight(120);
                questionWebView.setMinHeight(100);
            }
        } else {
            System.out.println(TAG + "Question is Not WebView!");

            objQuestionCenterVBox.getChildren().removeAll(questionWebView, questionWithImageHBox);

            questionLabelHBox.setMinHeight(10);
            if (!quesDescriptionList.isEmpty()) {
                if (!objQuestionCenterVBox.getChildren().contains(objScrollPane)) {
                    objQuestionCenterVBox.getChildren().add(objScrollPane);
                    objScrollPane.setMinHeight(180);
                    questionDescriptionLabel.setText(quesDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
                }
            } else {
                if (!objQuestionCenterVBox.getChildren().contains(objScrollPane)) {
                    questionLabelHBox.setMinHeight(200);
                }
            }

            if (!objQuestionCenterVBox.getChildren().contains(questionLabelHBox)) {
                objQuestionCenterVBox.getChildren().add(questionLabelHBox);
            }
            questionLabel.setText(questionText);
        }
    }

    private void loadTheoryQuestion(String questionText, boolean isWebView) {
        if (isWebView) {
            objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWebView, questionWithImageHBox);
            objQuestionCenterVBox.getChildren().add(questionWebView);

            String content = Helper.loadLatex(getClass(), questionText);
            questionWebView.getEngine().loadContent(content);
            questionWebView.setMinHeight(200);
        } else {
            objQuestionCenterVBox.getChildren().removeAll(objScrollPane, questionWebView, questionWithImageHBox);
            objQuestionCenterVBox.getChildren().add(objScrollPane);
            questionLabel.setText(questionText);
        }
    }

    private void showSubmitDialog() {
        Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Submit", null, "Are you sure you want to submit?");

        exitDialogDimmer.setVisible(true);

        dialog.setResultConverter(buttonType -> {

            if (buttonType == ButtonType.YES){

                if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                    exitDialogDimmer.setVisible(false);
                    Animations.showDialog(testSummaryDialog, summaryDialogDimmer);

                } else {
                    ExplanationScreenController.InitialData data = new ExplanationScreenController.InitialData(viewModel.getSubjects(), viewModel.getQuestionDescriptions(), viewModel.getSubjectsQuestions(), viewModel.getSelectedSubjectYear(), viewModel.getQuestionType(), Screens.PRACTICE_SCREEN);
                    ViewSwitcher.passData(data);
                    ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);
                    //TODO: Implement Theory result screen
                }

            }
            exitDialogDimmer.setVisible(false);
            return buttonType;
        });

        dialog.show();
    }

    private void showExitDialog() {
        Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Exit", null, "Are you sure you want to quit?");

        exitDialogDimmer.setVisible(true);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.YES) {
                exitDialogDimmer.setVisible(false);
                Helper.moveToPQScreen(Screens.PRACTICE_SCREEN, null);
            }
            exitDialogDimmer.setVisible(false);
            return buttonType;
        });

        dialog.show();
    }
 
    private void showTimeUpDialog() {
        Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Time Up", null, "Time Up! Do you want to submit?");

        exitDialogDimmer.setVisible(true);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.YES) {
                exitDialogDimmer.setVisible(false);
                ResultScreenController.InitialData initialData =
                        new ResultScreenController.InitialData(viewModel.getResults(), viewModel.getSubjects(), viewModel.getQuestionDescriptions(), viewModel.getSubjectsQuestions(), viewModel.getSelectedSubjectYear(), View.PQ_SCREEN);
                ViewSwitcher.passData(initialData);
                ViewSwitcher.showScreen(View.RESULT_SCREEN);

            } else if (buttonType == ButtonType.NO) {
                exitDialogDimmer.setVisible(false);

                ViewSwitcher.passData(new PQScreenController.InitialData(Screens.PRACTICE_SCREEN, null));
                ViewSwitcher.showScreen(View.PQ_SCREEN);
            }
            return buttonType;
        });

        dialog.show();
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
//        System.out.println(TAG + "Got data -> " + data);
        return data;
    }

    @FXML
    public void onCalculatorClicked(MouseEvent mouseEvent) {
        Stage calculatorStage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layouts/CalculatorView.fxml"));
            Scene scene = new Scene(root);

            Image appIcon = new Image(getClass().getResource("/drawable/app_icon.png").toString());
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

    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println(TAG + "Key pressed -> " + keyEvent.getCode());

        if (keyEvent.getCode().toString().equalsIgnoreCase("A")) {
            toggleGroup.selectToggle(optionAButton);
        } else if (keyEvent.getCode().toString().equalsIgnoreCase("B")) {
            toggleGroup.selectToggle(optionBButton);
        } else if (keyEvent.getCode().toString().equalsIgnoreCase("C")) {
            toggleGroup.selectToggle(optionCButton);
        } else if (keyEvent.getCode().toString().equalsIgnoreCase("D")) {
            toggleGroup.selectToggle(optionDButton);
        } else if (keyEvent.getCode().toString().equalsIgnoreCase("N")) {
            nextButton.fire();
        } else if (keyEvent.getCode().toString().equalsIgnoreCase("P")) {
            prevButton.fire();
        } else if (keyEvent.getCode().toString().equalsIgnoreCase("S")) {
            submitButton.fire();
        }
    }

    @Override
    public void onViewAdded() {}

    @Override
    public void onViewRemoved() {
        TextToSpeech.dispose();
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        practiceContentPane.setVisible(true);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        practiceContentPane.setVisible(false);
    }

    public static class InitialData {
        public List<SubjectState> questionData;
//        public QuestionType questionType;
        public int hours;
        public int minutes;

        public InitialData(List<SubjectState> questionData, int hours, int minutes) {
            this.questionData = questionData;
            this.hours = hours;
            this.minutes = minutes;
        }
    }
}
