package com.scholarly.utme.controller.practice_screens;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.*;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM2;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM2.QuestionState;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM2.SubjectQuestionsState;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.SceneLifecycle;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/practice_screens/StudyPastQuestionsScreen.fxml")
public class StudyPastQuestScreenController2 implements FxmlView<StudyPastScreenVM2>, Initializable, SceneLifecycle {
    private static final String TAG = "StudyPastQuestScreenController: ";

    @InjectViewModel
    private StudyPastScreenVM2 viewModel;

    @FXML
    private HBox answerHeaderHBox, rightPane;

    @FXML
    private VBox centerVBox, rightVBox, questionDescriptionDialog;

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane tileScrollPane, explanationScrollPane;

    @FXML
    private StackPane answerPane, explanationPane;

    @FXML
    private ListView<PQSubject> subjectList;

    @FXML
    private Label questionOverviewLabel, questionLabel, optionA, optionB, optionC, optionD, explanationLabel, explanationTitle, correctAnswerTitle, correctAnswerLabel, questionDescriptionHeader, readQuestionDesc, questionDescriptionText;

    @FXML
    private Button prevButton, nextButton, exitButton, hideAnswerButton;

    @FXML
    private ToggleButton showAnswerButton, showExplanationButton;

    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, quesDescriptionCloseIcon;

    @FXML
    private Pane dialogDimmer;

    @FXML
    private DialogPane exitDialogPane;

    private boolean initialized = false;

    private boolean onPrev = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel.processInitialData(getInitialData());

        initializeViews();
        initializeFont();

        subjectList.setCellFactory(new PracticeSubjectListCellFactory());
        subjectList.setItems(viewModel.getSubjects());

        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super PQSubject>) c -> {
            if (c.getList().size() == 1) {
                PQSubject subject = c.getList().get(0);
                viewModel.setSelectedSubject(subject);

                if (!onPrev) {
                    int startIndex = viewModel.getSubjectsQuestions().getSubjectStartIndex(subject.getShortTitle());
                    viewModel.getSubjectsQuestions().setSelectedQuestion(startIndex);
                }

                onPrev = false;

                if (!initialized) {
                    setupQuestionView();
                    initialized = true;
                }

                setupTilePane();
                updateBookmarkIcon();
            } else {
                viewModel.setSelectedSubject(null);
            }
        });

        subjectList.getSelectionModel().select(0);

        viewModel.getSubjectsQuestions().selectedQuestionProperty()
                .addListener((observable, oldValue, newValue) -> {
                    boolean subjectChanged = false;

                    if (newValue.intValue() - oldValue.intValue() == 1) {
                        String subjectTitle = viewModel.getSelectedSubject().getShortTitle();
                        boolean moveToNextSubject = viewModel.getSubjectsQuestions().moveToNextSubject(subjectTitle, newValue.intValue());

                        if (moveToNextSubject) {
                            subjectList.getSelectionModel().selectNext();
                            subjectChanged = true;
                        }
                    }

                    if (oldValue.intValue() - newValue.intValue() == 1) {
                        String subjectTitle = viewModel.getSelectedSubject().getShortTitle();
                        boolean moveToPrevSubject = viewModel.getSubjectsQuestions().moveToPrevSubject(subjectTitle, newValue.intValue());

                        if (moveToPrevSubject) {
                            subjectList.getSelectionModel().selectPrevious();
                            subjectChanged = true;
                        }
                    }

                    if (Math.abs(newValue.intValue() - oldValue.intValue()) == 1 && !subjectChanged) {
                        changeSelectedTile(oldValue.intValue(), newValue.intValue());
                    }

                    changeSelectedQuestion(newValue.intValue());
//                    updateBookmarkIcon();
                });

        prevButton.setOnAction(event -> {
            onPrev = true;

            int selectedQuestion = viewModel.getSubjectsQuestions().getSelectedQuestion();
            viewModel.getSubjectsQuestions().setSelectedQuestion(selectedQuestion - 1);
        });

        nextButton.setOnAction(event -> {
            onPrev = false;

            int selectedQuestion = viewModel.getSubjectsQuestions().getSelectedQuestion();
            viewModel.getSubjectsQuestions().setSelectedQuestion(selectedQuestion + 1);
        });

        showAnswerButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions().getSelectedQuestion();
            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions();

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                questionsState.getQuestions().get(selectedQuestion).setShowAnswer(true);

            } else {
                questionsState.getQuestions().get(selectedQuestion).setShowExplanation(true);
            }
            updateExplanationView();
        });

        showExplanationButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions().getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions();
            questionsState.getQuestions().get(selectedQuestion).setShowExplanation(true);

            updateExplanationView();
        });

        hideAnswerButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions().getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions();

            questionsState.getQuestions().get(selectedQuestion).setShowExplanation(false);

            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationScrollPane.setVisible(false);
            explanationLabel.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);
            hideAnswerButton.setVisible(false);

        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions().getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions();

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questionsState.getQuestions().get(selectedQuestion).getQuestion();
                String questionText = currentQuestion.getQuestion();
                if (!Helper.isWebView(questionText)) {
                    TextToSpeech.play(questionText);
                }
            } else {
                TheoryQuestion currentQuestion = (TheoryQuestion) questionsState.getQuestions().get(selectedQuestion).getQuestion();
                String questionText = currentQuestion.getQuestion();
                TextToSpeech.play(questionText);
            }

        });

//        bookmarkImage.setOnMouseClicked(event -> {
//            viewModel.handleBookmarkClicked();
//            updateBookmarkIcon();
//        });


        exitButton.setOnAction(event -> {
            showExitDialog();
        });

        readQuestionDesc.setOnMouseClicked(mouseEvent -> {
            Animations.showDialog(questionDescriptionDialog, dialogDimmer);
        });

        quesDescriptionCloseIcon.setOnMouseClicked(mouseEvent -> {
            Animations.hideDialog(questionDescriptionDialog, dialogDimmer);
        });

    }

    private void initializeViews() {
        subjectList.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
        tileScrollPane.setBackground(Background.EMPTY);
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

    public void onCalculatorClicked(MouseEvent mouseEvent) {

    }

    private void updateBookmarkIcon() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions();
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion selectedQuestion = (ObjectiveQuestion) questions.get(subjectQuestionsState.getSelectedQuestion()).getQuestion();

            List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks().get(viewModel.getSelectedSubject().getId());

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
//                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion selectedQuestion = (TheoryQuestion) questions.get(subjectQuestionsState.getSelectedQuestion()).getQuestion();

            List<TheoryBookmark> bookmarks = viewModel.getTheoryBookmarks().get(viewModel.getSelectedSubject().getId());

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
//                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });
        }

    }

    private void setupQuestionView() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions();
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestionNumber = subjectQuestionsState.getSelectedQuestion();
        Pair<Integer, Integer> questionNumberAndCount = subjectQuestionsState.getQuestionNumberAndCount(viewModel.getSelectedSubject().getShortTitle(), selectedQuestionNumber);

        prevButton.disableProperty().bind(Bindings.greaterThan(1, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size() - 1, subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + questionNumberAndCount.getKey() + " of " + questionNumberAndCount.getValue());

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(selectedQuestionNumber).getQuestion();

            List<QuestionDescription> questionDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).collect(Collectors.toList());

            System.out.println(TAG + "setupQuestionView: questionDescriptionInList -> " + questionDescriptionInList);

            if (questionDescriptionInList.isEmpty()) {
                readQuestionDesc.setVisible(false);
                questionDescriptionHeader.setText("");
            } else {
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            String questionText = currentQuestion.getQuestion();
            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            optionA.setText(" (A) " + currentQuestion.getOptionA().getText());
            optionB.setText(" (B) " + currentQuestion.getOptionB().getText());
            optionC.setText(" (C) " + currentQuestion.getOptionC().getText());
            optionD.setText(" (D) " + currentQuestion.getOptionD().getText());

            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());
            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));
        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(selectedQuestionNumber).getQuestion();

            String questionText = currentQuestion.getQuestion();
            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            centerVBox.getChildren().removeAll(optionA, optionB, optionC, optionD);

            rightVBox.getChildren().remove(answerPane);

//            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());
            answerHeaderHBox.getChildren().remove(showExplanationButton);
            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));

        }

        updateExplanationView();
    }

    private void changeSelectedQuestion(int newValue) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions();
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        Pair<Integer, Integer> questionNumberAndCount = subjectQuestionsState.getQuestionNumberAndCount(viewModel.getSelectedSubject().getShortTitle(), newValue);
        questionOverviewLabel.setText("Question " + questionNumberAndCount.getKey() + " of " + questionNumberAndCount.getValue());

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(newValue).getQuestion();

            List<QuestionDescription> questionDescriptionInList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).collect(Collectors.toList());

            if (questionDescriptionInList.isEmpty()) {
                readQuestionDesc.setVisible(false);
                questionDescriptionHeader.setText("");
            } else {
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            String questionText = currentQuestion.getQuestion();
            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            optionA.setText(" (A) " + currentQuestion.getOptionA().getText());
            optionB.setText(" (B) " + currentQuestion.getOptionB().getText());
            optionC.setText(" (C) " + currentQuestion.getOptionC().getText());
            optionD.setText(" (D) " + currentQuestion.getOptionD().getText());


            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());

            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(newValue).getQuestion();

            String questionText = currentQuestion.getQuestion();
            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());
            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));

        }

        updateExplanationView();
    }

    private void  updateExplanationView() {
        SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions();

        QuestionState questionState = questionsState.getQuestions().get(questionsState.getSelectedQuestion());

        if (!questionState.isShowExplanation() && !questionState.isShowAnswer()) {
            System.out.println(TAG + "showing default explanation view");
            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationScrollPane.setVisible(false);
            explanationLabel.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);

            hideAnswerButton.setVisible(false);

        } else if (questionState.isShowExplanation()) {

            System.out.println(TAG + "showing explanation and answer view");
            explanationTitle.setVisible(true);
            explanationScrollPane.setVisible(true);
            explanationLabel.setVisible(true);
            showExplanationButton.setDisable(true);

            correctAnswerLabel.setVisible(true);
            correctAnswerTitle.setVisible(true);
            showAnswerButton.setDisable(true);

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

    private void setupTilePane() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions();

        int startIndex = viewModel.getSubjectsQuestions().getSubjectStartIndex(viewModel.getSelectedSubject().getShortTitle());
        int questionCount = viewModel.getSubjectsQuestions().getSubjectQuestionCount(viewModel.getSelectedSubject().getShortTitle());

        tilePane.setVgap(10);
        tilePane.setHgap(10);
        tilePane.getChildren().clear();

        for (int i = 0; i < questionCount; i++) {
            Rectangle r = new Rectangle(30, 30);
            r.setStroke(Paint.valueOf("#12AF20"));
            r.setFill(Color.web("#ededed"));

            Label l = new Label(Integer.toString(i+1));
            if (subjectQuestionsState.getSelectedQuestion() == startIndex + i) {
                r.setFill(Paint.valueOf("#12AF20"));
                l.setTextFill(Color.WHITE);
            }
            StackPane s = new StackPane(r, l);
            tilePane.getChildren().add(s);

            int finalI = startIndex + i;
            s.setOnMouseClicked(event -> {
                subjectQuestionsState.setSelectedQuestion(finalI);
            });
        }
    }

    private void changeSelectedTile(int oldSelectedQuestion, int newSelectedQuestion) {
        int startIndex = viewModel.getSubjectsQuestions().getSubjectStartIndex(viewModel.getSelectedSubject().getShortTitle());

        StackPane selectedQuestionPane = (StackPane) tilePane.getChildren().get(newSelectedQuestion - startIndex);
        StackPane oldQuestionPane = (StackPane) tilePane.getChildren().get(oldSelectedQuestion - startIndex);

        Rectangle oldQuestionRectangle = (Rectangle) oldQuestionPane.getChildren().get(0);
        Rectangle selectedQuestionRectangle = (Rectangle) selectedQuestionPane.getChildren().get(0);
        Label selectedQuestionText = (Label) selectedQuestionPane.getChildren().get(1);
        Label oldQuestionText = (Label) oldQuestionPane.getChildren().get(1);

        oldQuestionText.setTextFill(Color.BLACK);
        oldQuestionRectangle.setFill(Paint.valueOf("#ededed"));
        selectedQuestionText.setTextFill(Color.WHITE);
        selectedQuestionRectangle.setFill(Paint.valueOf("#12AF20"));
    }

    private void showExitDialog() {
        Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Exit", null, "Are you sure you want to quit?");

        dialogDimmer.setVisible(true);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.YES) {
                dialogDimmer.setVisible(false);
                ViewSwitcher.passData(new HomeScreenController.InitialData(Screens.PAST_QUESTION_SCREEN, null));
                ViewSwitcher.showScreen(View.HOME_SCREEN);
            }
            dialogDimmer.setVisible(false);
            return buttonType;
        });

        dialog.show();

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
