package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.*;
//import com.gtranslate.Audio;
//import com.gtranslate.Language;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.data.model.QuestionDescription;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import com.scholarly.utme.viewmodels.PracticeScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.PracticeScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.SceneLifecycle;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.scholarly.utme.util.Constants.PRACTICE_SCREEN;

@FxmlPath("/layouts/PracticeScreen.fxml")
public class PracticeScreenController implements FxmlView<PracticeScreenVM>, Initializable, SceneLifecycle {

    private static final String TAG = "PracticeScreenController: ";

    private final static double rectangleSelectedBorderWidth = 2.0;
    private final static Color rectangleBorderSelectedColor = Color.ORANGE;
    private final static Paint rectangleSelectedColor = Paint.valueOf("#12AF20");

    @InjectViewModel
    private PracticeScreenVM viewModel;

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane tileScrollPane;

    @FXML
    private ListView<PQSubject> subjectList;

    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private RadioButton optionAButton, optionBButton, optionCButton, optionDButton;

    @FXML
    private Label questionOverviewLabel, questionLabel, timeLabel, scoreText, questionDescriptionHeader, readQuestionDesc, questionDescriptionText;

    @FXML
    private TextField enterCorrectAnswerField;

    @FXML
    private CheckBox questionErrorCheckBox, incorrectAnswerCheckBox, okayCheckBox;

    @FXML
    private VBox incorrectAnswerPane, reportDialog, testSummaryDialog, centerVBox, questionDescriptionDialog;

    @FXML
    private Button prevButton, nextButton, exitButton, submitButton, submitReport, homePageButton, resultAnalysisButton;

    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, reportDialogCloseIcon, quesDescriptionCloseIcon, timeImage, summaryBookImage, testSummaryCloseIcon;

    @FXML
    private Pane dialogDimmer, exitDialogDimmer, summaryDialogDimmer;

    @FXML
    private WebView webView;

    int totalScore = 0;
    int totalQuestions = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewModel.processInitialData(getInitialData());
        
        initializeViews();

        initializeFont();

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupQuestionView();
                setupTilePane();
            }
        });

        /*viewModel.getSubjectBookmarks().forEach((s, bookmarks) -> {
            bookmarks.addListener((ListChangeListener<? super ObjectiveBookmark>) change -> {
                System.out.println(TAG + "Bookmark List changed ");
                if (viewModel.getSelectedSubject().getShortTitle()().equalsIgnoreCase(s)) {
                    updateBookmarkIcon();
                }
            });
        });*/

        subjectList.setCellFactory(new PracticeSubjectListCellFactory());
        subjectList.setItems(viewModel.getSubjects());
        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super PQSubject>) change -> {
            if (change.getList().size() == 1) {
                PQSubject subject = change.getList().get(0);
                System.out.println(TAG + "Content of change -> " + change);
                viewModel.setSelectedSubject(subject);
            } else {
                viewModel.setSelectedSubject(null);
            }
        });

        // Integer value to track subjectList subject selection
        AtomicInteger subjectListSelectionIndex = new AtomicInteger(0);

        subjectList.getSelectionModel().select(subjectListSelectionIndex.get());

        viewModel.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
            subjectQuestionsState.selectedQuestionProperty().addListener((observable, oldValue, newValue) -> {

                if (viewModel.getSelectedSubject().getShortTitle().equalsIgnoreCase(s)) {

                    if (newValue.intValue() <= subjectQuestionsState.getQuestions().size()){
                        changeSelectedTile(oldValue.intValue(), newValue.intValue());
                        changeSelectedQuestion(newValue.intValue());

                    }

                }
            });
        });



        prevButton.setOnAction(event -> {
            nextButton.setDisable(false);
            SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
            List<QuestionState> questions = subjectQuestionsState.getQuestions();

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

//            System.out.println("Selected question index -> " + selectedQuestion);
//            System.out.println("SubjectList selected item index -> " + subjectList.getSelectionModel().getSelectedIndex());
//            System.out.println("Subject List Selection Index -> " + subjectListSelectionIndex);


            if (subjectList.getSelectionModel().getSelectedIndex() == 0) {

                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestion - 1);

                if (selectedQuestion == 2) {
                    prevButton.setDisable(true);
                }


            }else {

                if (selectedQuestion == 1) {

                    subjectList.getSelectionModel().select(subjectListSelectionIndex.decrementAndGet());

                }else {
                    viewModel.getSubjectsQuestions()
                            .get(viewModel.getSelectedSubject().getShortTitle())
                            .setSelectedQuestion(selectedQuestion - 1);
                }

            }

        });

        nextButton.setOnAction(event -> {
            prevButton.setDisable(false);
            SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
            List<QuestionState> questions = subjectQuestionsState.getQuestions();

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();


            if (selectedQuestion == questions.size()){

                subjectList.getSelectionModel().select(subjectListSelectionIndex.incrementAndGet());

                System.out.println(TAG + "Selected question index -> " + selectedQuestion);
                System.out.println(TAG + "SubjectList selected item index -> " + subjectList.getSelectionModel().getSelectedIndex());
                System.out.println(TAG + "Subject List Selection Index -> " + subjectListSelectionIndex);

                if (subjectList.getItems().size() == subjectListSelectionIndex.get()){

                    nextButton.setDisable(true);

                }

            } else {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestion + 1);
            }

        });

        tilePane.setVgap(10);
        tilePane.setHgap(10);

        toggleGroup.getToggles().addAll(optionAButton, optionBButton, optionCButton, optionDButton);

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

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(question.getOptionA());

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

                    if (questionState.getSelectedOption() == null) {
                        onOptionSelected(selectedQuestion);
                    }


                    questionState.setSelectedOption(question.getOptionA());
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

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(question.getOptionB());
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

                    if (questionState.getSelectedOption() == null) {
                        onOptionSelected(selectedQuestion);
                    }

                    questionState.setSelectedOption(question.getOptionB());
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

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(question.getOptionC());
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

                    if (questionState.getSelectedOption() == null) {
                        onOptionSelected(selectedQuestion);
                    }

                    questionState.setSelectedOption(question.getOptionC());
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

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(question.getOptionD());
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

                    if (questionState.getSelectedOption() == null) {
                        onOptionSelected(selectedQuestion);
                    }

                    questionState.setSelectedOption(question.getOptionD());
                }
            });

        }
        else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {

            centerVBox.getChildren().removeAll(optionAButton, optionBButton, optionCButton, optionDButton);
            /*optionAButton.setVisible(false);
            optionBButton.setVisible(false);
            optionCButton.setVisible(false);
            optionDButton.setVisible(false);*/

        }

        viewModel.timeProperty().addListener((observable, oldValue, newValue) -> {
            String timeText = "";

            long hours = newValue.longValue() / 3600;
            long minutes = newValue.longValue() % 3600 / 60;
            long secs = newValue.longValue() % 60;

            if (hours > 0) {
                timeText = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
            } else {
                timeText = String.format(Locale.getDefault(), "%d:%02d", minutes, secs);
            }

            timeLabel.setText(timeText);

            if (newValue.intValue() == 0) {
                showTimeUpDialog();
            }

            // TODO: Implement time elapsed here
        });

        exitButton.setOnAction(event -> {
            showExitDialog();
        });

        submitButton.setOnAction(event -> {
            showSubmitDialog();

            viewModel.getResults().forEach(result -> {
                 totalScore += result.getCorrectAnswers();
                 totalQuestions += result.getTotalQuestions();
            });
            scoreText.setText(totalScore + "/" + totalQuestions);
        });

        testSummaryCloseIcon.setOnMouseClicked(event -> {
            Animations.hideDialog(testSummaryDialog, summaryDialogDimmer);

        });

        homePageButton.setOnAction(event -> {
            Animations.hideDialog(testSummaryDialog, summaryDialogDimmer);

        });

        resultAnalysisButton.setOnAction(event -> {
            ResultScreenController.InitialData initialData =
                    new ResultScreenController.InitialData(viewModel.getResults(), viewModel.getSubjects(), viewModel.getSubjectsQuestions(), View.HOME_SCREEN);

            ViewSwitcher.passData(initialData);
            ViewSwitcher.showScreen(View.RESULT_SCREEN);
        });

        bookmarkImage.setOnMouseClicked(mouseEvent -> {
            viewModel.handleBookmarkClicked();
            updateBookmarkIcon();

        });

        reportDialogCloseIcon.setOnMouseClicked(mouseEvent -> {
            Animations.hideDialog(reportDialog, dialogDimmer);
        });

        flagImage.setOnMouseClicked(mouseEvent -> {
            Animations.showDialog(reportDialog, dialogDimmer);
        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getQuestions()
                    .get(selectedQuestion - 1);

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
               // System.out.println("Question: " + ((ObjectiveQuestion) questionState.getQuestion()).getQuestion());
                TextToSpeech.play(((ObjectiveQuestion) questionState.getQuestion()).getQuestion());
            } else {
                TextToSpeech.play(((TheoryQuestion) questionState.getQuestion()).getQuestion());
            }
        });

        readQuestionDesc.setOnMouseClicked(mouseEvent -> {
            Animations.showDialog(questionDescriptionDialog, dialogDimmer);
        });

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
        tileScrollPane.setBackground(Background.EMPTY);
        homePageButton.setBackground(Background.EMPTY);

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
        reportDialogCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        quesDescriptionCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
        flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));
        timeImage.setImage(new Image(getClass().getResource("/drawable/practice_screen_images/time_image.jpg").toString()));

        ImageView prevImage = new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/prev_btn_icon.png").toString()));
        prevButton.setGraphicTextGap(15);
        prevButton.setGraphic(prevImage);
        ImageView nextImage = new ImageView(getClass().getResource("/drawable/practice_screen_images/next_btn_icon.png").toString());
        nextButton.setContentDisplay(ContentDisplay.RIGHT);
        nextButton.setGraphicTextGap(15);
        nextButton.setGraphic(nextImage);

        summaryBookImage.setImage(new Image(getClass().getResource("/drawable/practice_screen_images/summary_book_image.jpg").toString()));
        testSummaryCloseIcon.setImage(new Image(getClass().getResource("/drawable/practice_screen_images/summary_close_icon.png").toString()));
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
        selectedQuestionText.setTextFill(Color.WHITE);

    }

    private void updateBookmarkIcon() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestionNumber = subjectQuestionsState.getSelectedQuestion();
//        System.out.println(TAG + "updateBookmarkIcon selectedQuestionNumber -> " + selectedQuestionNumber);
//        List<ObjectiveBookmark> bookmarks = viewModel.getSubjectBookmarks().get(viewModel.getSelectedSubject().getShortTitle()());

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks();
//        System.out.println(TAG + "updateBookmarkIcon bookmarks -> " + bookmarks);

            bookmarks.forEach(bookmark -> {
//            System.out.println(TAG + "currentQuestionIndex -> " + ((ObjectiveQuestion) questions.get(selectedQuestionNumber - 1).getQuestion()).getId());
//            System.out.println(TAG + "Bookmark question id -> " + bookmark.getQuestionId());
                if (bookmark.getQuestionId() == ((ObjectiveQuestion) questions.get(selectedQuestionNumber - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            List<TheoryBookmark> bookmarks = viewModel.getTheoryBookmarks();
//        System.out.println(TAG + "updateBookmarkIcon bookmarks -> " + bookmarks);

            bookmarks.forEach(bookmark -> {
//            System.out.println(TAG + "currentQuestionIndex -> " + ((ObjectiveQuestion) questions.get(selectedQuestionNumber - 1).getQuestion()).getId());
//            System.out.println(TAG + "Bookmark question id -> " + bookmark.getQuestionId());
                if (bookmark.getQuestionId() == ((TheoryQuestion) questions.get(selectedQuestionNumber - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        }


        /*bookmarks.forEach(bookmark -> {
            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                if (bookmark.getQuestionId() == ((ObjectiveQuestion) questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                }
            } else {
                if (bookmark.getQuestionId() == ((TheoryQuestion) questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                }
            }
        });*/
    }

    private void setupQuestionView() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();


        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            List<ObjectiveBookmark> objectiveBookmarks = viewModel.getObjectiveBookmarks();

            objectiveBookmarks.forEach(objectiveBookmark -> {
                if (objectiveBookmark.getQuestionId() == ((ObjectiveQuestion) questions.get(selectedQuestion - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                }
            });

        } else {
            List<TheoryBookmark> theoryBookmarks = viewModel.getTheoryBookmarks();

            theoryBookmarks.forEach(theoryBookmark -> {
                if (theoryBookmark.getQuestionId() == ((TheoryQuestion) questions.get(selectedQuestion - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                }
            });
        }


        /*bookmarks.forEach(bookmark -> {
            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                if (bookmark.getQuestionId() == ((ObjectiveQuestion) questions.get(selectedQuestion - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                }
            } else {
                if (bookmark.getQuestionId() == ((TheoryQuestion) questions.get(selectedQuestion - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                }
            }
        });*/

        // prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        // nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        System.out.println(TAG + "Index of the selected subject in subjectList: " + subjectList.getSelectionModel().getSelectedIndex());
        if (subjectList.getSelectionModel().getSelectedIndex() + 1 == selectedQuestion - 1){
            prevButton.setDisable(true);
        }

        questionOverviewLabel.setText("Question " + selectedQuestion + " of " + questions.size());

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion question = (ObjectiveQuestion) questions.get(selectedQuestion - 1).getQuestion();
            questionOverviewLabel.setText("Question " + selectedQuestion + " of " + questions.size());

            List<QuestionDescription> quesDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == question.getQuestionDescriptionId()).collect(Collectors.toList());

            if (quesDescriptionInList.isEmpty()) {
                readQuestionDesc.setVisible(false);
                questionDescriptionHeader.setText("");
            } else {
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            questionLabel.setText(question.getQuestion());

            String questionText = question.getQuestion();

            if (questionText.contains("<img")) {
                int startIndexOfImg = questionText.indexOf("<img");
                int endIndexOfImg = questionText.indexOf("'100%'>", startIndexOfImg);

                int startIndexOfImgPath = questionText.indexOf("/android_asset", startIndexOfImg);
                int endIndexOfImgPath = questionText.indexOf("' width", startIndexOfImg);

                String imagePath = questionText.substring(startIndexOfImgPath, endIndexOfImgPath);

                StringBuilder builder = new StringBuilder(questionText);

                System.out.println(imagePath);

                URL url = getClass().getResource(imagePath);
                String img = "<img src='"+url+"' width='100%'>";

                builder.replace(startIndexOfImg, (endIndexOfImg + 7), img);

                questionText = builder.toString();
                System.out.println(questionText);
            }

            webView.getEngine().loadContent(questionText);

            optionAButton.setText(" (A) " + question.getOptionA());
            optionBButton.setText(" (B) " + question.getOptionB());
            optionCButton.setText(" (C) " + question.getOptionC());
            optionDButton.setText(" (D) " + question.getOptionD());

            String selectedOption = questions.get(selectedQuestion - 1).getSelectedOption();
            if (selectedOption != null) {
                if (selectedOption.equalsIgnoreCase(question.getOptionA())) {
                    toggleGroup.selectToggle(optionAButton);
                } else if (selectedOption.equalsIgnoreCase(question.getOptionB())) {
                    toggleGroup.selectToggle(optionBButton);
                } else if (selectedOption.equalsIgnoreCase(question.getOptionC())) {
                    toggleGroup.selectToggle(optionCButton);
                } else if (selectedOption.equalsIgnoreCase(question.getOptionD())) {
                    toggleGroup.selectToggle(optionDButton);
                }
            } else {
                toggleGroup.selectToggle(null);
            }
        } else {
            TheoryQuestion question = (TheoryQuestion) questions.get(selectedQuestion - 1).getQuestion();
            questionOverviewLabel.setText("Question " + selectedQuestion + " of " + questions.size());

            List<QuestionDescription> quesDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == question.getQuestionDescriptionId()).collect(Collectors.toList());

            if (quesDescriptionInList.isEmpty()) {
                readQuestionDesc.setVisible(false);
                questionDescriptionHeader.setText("");
            } else {
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            questionLabel.setText(question.getQuestion());
            webView.getEngine().loadContent(question.getQuestion());
        }
    }

    private void changeSelectedQuestion(int newValue) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks();
//        System.out.println(TAG + "changeSelectedQuestion bookmarks -> " + bookmarks);

            bookmarks.forEach(bookmark -> {
//            System.out.println(TAG + "currentQuestionIndex -> " + ((ObjectiveQuestion) questions.get(newValue - 1).getQuestion()).getId());
//            System.out.println(TAG + "Bookmark question id -> " + bookmark.getQuestionId());
                if (bookmark.getQuestionId() == ((ObjectiveQuestion) questions.get(newValue - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        } else {
            List<TheoryBookmark> bookmarks = viewModel.getTheoryBookmarks();
//        System.out.println(TAG + "changeSelectedQuestion bookmarks -> " + bookmarks);

            bookmarks.forEach(bookmark -> {
//            System.out.println(TAG + "currentQuestionIndex -> " + ((ObjectiveQuestion) questions.get(newValue - 1).getQuestion()).getId());
//            System.out.println(TAG + "Bookmark question id -> " + bookmark.getQuestionId());
                if (bookmark.getQuestionId() == ((TheoryQuestion) questions.get(newValue - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        }


        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion question = (ObjectiveQuestion) questions.get(newValue - 1).getQuestion();
            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            List<QuestionDescription> quesDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == question.getQuestionDescriptionId()).collect(Collectors.toList());

            if (quesDescriptionInList.isEmpty()) {
                readQuestionDesc.setVisible(false);
                questionDescriptionHeader.setText("");
            } else {
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            questionLabel.setText(question.getQuestion());
            String questionText = question.getQuestion();

            if (questionText.contains("<img")) {
                int startIndexOfImg = questionText.indexOf("<img");
                int endIndexOfImg = questionText.indexOf("'100%'>", startIndexOfImg);

                int startIndexOfImgPath = questionText.indexOf("/android_asset", startIndexOfImg);
                int endIndexOfImgPath = questionText.indexOf("' width", startIndexOfImg);

                String imagePath = questionText.substring(startIndexOfImgPath, endIndexOfImgPath);

                StringBuilder builder = new StringBuilder(questionText);

                System.out.println(imagePath);

                URL url = getClass().getResource(imagePath);
                String img = "<img src='"+url+"' width='100%'>";

                builder.replace(startIndexOfImg, (endIndexOfImg + 7), img);

                questionText = builder.toString();
                System.out.println(questionText);
            }

            webView.getEngine().loadContent(questionText);

            optionAButton.setText(" (A) " + question.getOptionA());
            optionBButton.setText(" (B) " + question.getOptionB());
            optionCButton.setText(" (C) " + question.getOptionC());
            optionDButton.setText(" (D) " + question.getOptionD());

            String selectedOption = questions.get(newValue - 1).getSelectedOption();
            if (selectedOption != null) {
                if (selectedOption.equalsIgnoreCase(question.getOptionA())) {
                    toggleGroup.selectToggle(optionAButton);
                } else if (selectedOption.equalsIgnoreCase(question.getOptionB())) {
                    toggleGroup.selectToggle(optionBButton);
                } else if (selectedOption.equalsIgnoreCase(question.getOptionC())) {
                    toggleGroup.selectToggle(optionCButton);
                } else if (selectedOption.equalsIgnoreCase(question.getOptionD())) {
                    toggleGroup.selectToggle(optionDButton);
                }
            } else {
                toggleGroup.selectToggle(null);
            }
        } else {
            TheoryQuestion question = (TheoryQuestion) questions.get(newValue - 1).getQuestion();
            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            List<QuestionDescription> quesDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == question.getQuestionDescriptionId()).collect(Collectors.toList());
            if (quesDescriptionInList.isEmpty()) {
                readQuestionDesc.setVisible(false);
                questionDescriptionHeader.setText("");
            } else {
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(quesDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            questionLabel.setText(question.getQuestion());
            webView.getEngine().loadContent(question.getQuestion());
        }
    }

    private void setupTilePane() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(35, 35);
            r.setFill(Color.web("#FFFFFF"));
            r.setStroke(Paint.valueOf("#12AF20"));
            r.setStrokeType(StrokeType.OUTSIDE);

            Label l = new Label(Integer.toString(i));

            if (questions.get(i-1).getSelectedOption() != null) {
                r.setFill(Paint.valueOf("#12AF20"));
                l.setTextFill(Color.WHITE);
            }

            if (subjectQuestionsState.getSelectedQuestion() == i) {
                r.setStroke(Color.ORANGE);
                r.setStrokeWidth(2);
            }
            StackPane s = new StackPane(r, l);
            tilePane.getChildren().add(s);

            int finalI = i;
            s.setOnMouseClicked(event -> {
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

        oldQuestionRectangle.setStroke(rectangleSelectedColor);
        oldQuestionRectangle.setStrokeWidth(1.0);

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
                    ExplanationScreenController.InitialData data = new ExplanationScreenController.InitialData(viewModel.getSubjects(), viewModel.getSubjectsQuestions(), viewModel.getQuestionType());
                    ViewSwitcher.passData(data);
                    ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);
                    //TODO: Implement Theory result screen
                }

            }else if (buttonType == ButtonType.NO){
                exitDialogDimmer.setVisible(false);
            }
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
                ViewSwitcher.passData(new HomeScreenController.InitialData(PRACTICE_SCREEN));
                ViewSwitcher.showScreen(View.HOME_SCREEN);
            } else if (buttonType == ButtonType.NO) {
                exitDialogDimmer.setVisible(false);
            }
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
                        new ResultScreenController.InitialData(viewModel.getResults(), viewModel.getSubjects(), viewModel.getSubjectsQuestions(), View.HOME_SCREEN);
                ViewSwitcher.passData(initialData);
                ViewSwitcher.showScreen(View.RESULT_SCREEN);

            } else if (buttonType == ButtonType.NO) {
                exitDialogDimmer.setVisible(false);

                ViewSwitcher.passData(new HomeScreenController.InitialData(Screens.PRACTICE_SCREEN));
                ViewSwitcher.showScreen(View.HOME_SCREEN);
            }
            return buttonType;
        });

        dialog.show();
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println(TAG + "Got data -> " + data);
        return data;
    }

    @FXML
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
        }
    }

    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {
        TextToSpeech.dispose();
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
