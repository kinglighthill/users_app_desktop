package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.*;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.StudyPastScreenVM;
import com.scholarly.utme.viewmodels.StudyPastScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.StudyPastScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.scholarly.utme.util.Constants.PAST_QUESTION_SCREEN;

@FxmlPath("/layouts/StudyPastQuestionsScreen.fxml")
public class StudyPastQuestScreenController implements FxmlView<StudyPastScreenVM>, Initializable, SceneLifecycle {
    public static final String TAG = "StudyPastQuestScreenController: ";

    @InjectViewModel
    private StudyPastScreenVM viewModel;

    @FXML
    private HBox rightPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane tileScrollPane;

    @FXML
    private ListView<Subject> subjectList;

    @FXML
    private Label questionOverviewLabel, questionLabel, optionA, optionB, optionC, optionD, explanationLabel, explanationTitle, correctAnswerTitle, correctAnswerLabel;

    @FXML
    private Button prevButton, nextButton, exitButton, hideAnswerButton;

    @FXML
    private ToggleButton showAnswerButton, showExplanationButton;

    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage;

    @FXML
    private Pane dialogDimmer;

    @FXML
    private DialogPane exitDialogPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel.processInitialData(getInitialData());

        initializeViews();
        initializeFont();

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupQuestionView();
                setupTilePane();
                updateBookmarkIcon();
            }
        });

        subjectList.setCellFactory(new PracticeSubjectListCellFactory());
        subjectList.setItems(viewModel.getSubjects());
        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Subject>) c -> {
            if (c.getList().size() == 1) {
                Subject subject = c.getList().get(0);
                viewModel.setSelectedSubject(subject);
            } else {
                viewModel.setSelectedSubject(null);
            }
        });

        subjectList.getSelectionModel().select(0);

        viewModel.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
            subjectQuestionsState.selectedQuestionProperty().addListener((observable, oldValue, newValue) -> {
                if (viewModel.getSelectedSubject().getTableName().equalsIgnoreCase(s)) {
                    changeSelectedTile(oldValue.intValue(), newValue.intValue());
                    changeSelectedQuestion(newValue.intValue());
                    updateBookmarkIcon();
                }
            });
        });

        prevButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .setSelectedQuestion(selectedQuestion - 1);
        });

        nextButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .setSelectedQuestion(selectedQuestion + 1);
        });

        showAnswerButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName());

            questionsState.getQuestions().get(selectedQuestion - 1).setShowAnswer(true);
            updateExplanationView();
        });

        showExplanationButton.setOnAction(event -> {

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName());

            questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(true);

            updateExplanationView();
        });

        hideAnswerButton.setOnAction(event -> {

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName());

            questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(false);

            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationLabel.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);
            hideAnswerButton.setVisible(false);

        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName());

//            String currentQuestion = questionsState.getQuestions().get(selectedQuestion - 1).getQuestion().getQuestion();

//            TextToSpeech.play(currentQuestion);
        });

        bookmarkImage.setOnMouseClicked(event -> {
            viewModel.handleBookmarkClicked();
            updateBookmarkIcon();
        });


        exitButton.setOnAction(event -> {
            showExitDialog();
        });

    }

    private void initializeViews() {
        subjectList.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
        tileScrollPane.setBackground(Background.EMPTY);
        showAnswerButton.setBackground(Background.EMPTY);
        showExplanationButton.setBackground(Background.EMPTY);

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
        flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));

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
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {

            List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks();

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == ((ObjectiveQuestion) questions.get(selectedQuestion - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {

            List<TheoryBookmark> bookmarks = viewModel.getTheoryBookmarks();

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == ((TheoryQuestion) questions.get(selectedQuestion - 1).getQuestion()).getId()) {
                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });
        }

    }

    private void setupQuestionView() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestionNumber = subjectQuestionsState.getSelectedQuestion();

        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(selectedQuestionNumber - 1).getQuestion();

            questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

            questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

            questionLabel.setText(currentQuestion.getQuestion());

            optionA.setText(" (A) " + currentQuestion.getOptionA());
            optionB.setText(" (B) " + currentQuestion.getOptionB());
            optionC.setText(" (C) " + currentQuestion.getOptionC());
            optionD.setText(" (D) " + currentQuestion.getOptionD());


            explanationLabel.setText(currentQuestion.getAnswerExplanation());
            correctAnswerLabel.setText(currentQuestion.getOptionAnswer());

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(selectedQuestionNumber - 1).getQuestion();

            questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

            questionLabel.setText(currentQuestion.getQuestion());

            optionA.setVisible(false);
            optionB.setVisible(false);
            optionC.setVisible(false);
            optionD.setVisible(false);

            explanationLabel.setText(currentQuestion.getAnswerExplanation());
            correctAnswerLabel.setText(currentQuestion.getOptionAnswer());
        }

        updateExplanationView();
    }

    private void  updateExplanationView() {
        SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                .get(viewModel.getSelectedSubject().getTableName());

        QuestionState questionState = questionsState.getQuestions().get(questionsState.getSelectedQuestion() - 1);

        if (!questionState.isShowExplanation() && !questionState.isShowAnswer()) {
            System.out.println(TAG + "showing default explanation view");
            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationLabel.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);

            hideAnswerButton.setVisible(false);

        } else if (questionState.isShowExplanation()) {

            System.out.println(TAG + "showing explanation and answer view");
            explanationTitle.setVisible(true);
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
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.setVgap(10);
        tilePane.setHgap(10);
        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(30, 30);
            r.setStroke(Paint.valueOf("#12AF20"));
            r.setFill(Color.web("#ededed"));

            Label l = new Label(Integer.toString(i));
            if (subjectQuestionsState.getSelectedQuestion() == i) {
                r.setFill(Paint.valueOf("#12AF20"));
                l.setTextFill(Color.WHITE);
            }
            StackPane s = new StackPane(r, l);
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

        oldQuestionText.setTextFill(Color.BLACK);
        oldQuestionRectangle.setFill(Paint.valueOf("#ededed"));
        selectedQuestionText.setTextFill(Color.WHITE);
        selectedQuestionRectangle.setFill(Paint.valueOf("#12AF20"));

    }

    private void changeSelectedQuestion(int newValue) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(newValue - 1).getQuestion();

            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            questionLabel.setText(currentQuestion.getQuestion());

            optionA.setText(" (A) " + currentQuestion.getOptionA());
            optionB.setText(" (B) " + currentQuestion.getOptionB());
            optionC.setText(" (C) " + currentQuestion.getOptionC());
            optionD.setText(" (D) " + currentQuestion.getOptionD());


            explanationLabel.setText(currentQuestion.getAnswerExplanation());
            correctAnswerLabel.setText(currentQuestion.getOptionAnswer());

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(newValue - 1).getQuestion();

            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            questionLabel.setText(currentQuestion.getQuestion());

            explanationLabel.setText(currentQuestion.getAnswerExplanation());
            correctAnswerLabel.setText(currentQuestion.getOptionAnswer());
        }

        updateExplanationView();
    }

    private void showExitDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.initModality(Modality.APPLICATION_MODAL);
        // Change dialog icon
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/drawable/app_logo.png").toString()));
        dialog.setTitle("Confirm Exit");

        exitDialogPane.setVisible(true);
        dialogDimmer.setVisible(true);

        dialog.getDialogPane().setContent(exitDialogPane);

        dialog.getDialogPane().setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        dialog.getDialogPane().setMinSize(350, 80);

        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.YES){
                dialogDimmer.setVisible(false);
                ViewSwitcher.passData(new HomeScreenController.InitialData(PAST_QUESTION_SCREEN));
                ViewSwitcher.showScreen(View.HOME_SCREEN);
            }else if (buttonType == ButtonType.NO){;
                dialogDimmer.setVisible(false);
            }
            return null;
        });

        dialog.showAndWait();
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
