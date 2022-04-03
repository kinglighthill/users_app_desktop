package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.StudyPastScreenVM;
import com.scholarly.utme.viewmodels.StudyPastScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.StudyPastScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.sun.speech.freetts.VoiceManager;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/StudyPastQuestionsScreen.fxml")
public class StudyPastQuestScreenController implements FxmlView<StudyPastScreenVM>, Initializable {

    @InjectViewModel
    private StudyPastScreenVM viewModel;

    @FXML
    private HBox rightPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private ListView<Subject> subjectList;

    @FXML
    private Label questionOverviewLabel, questionLabel, optionA, optionB, optionC, optionD, explanationLabel, explanationTitle, correctAnswerTitle, correctAnswerLabel;

    @FXML
    private Button prevButton, nextButton, exitButton, showCorrectAnswerButton, showExplanationButton, hideAnswerButton;

    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage;

    @FXML
    private Pane dialogDimmer;

    @FXML
    private DialogPane exitDialogPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel.processInitialData(getInitialData());

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupQuestionView();
                setupTilePane();
            }
        });

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

        showCorrectAnswerButton.setOnAction(event -> {
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

            showCorrectAnswerButton.setVisible(true);
            showExplanationButton.setVisible(true);
            explanationTitle.setVisible(false);
            explanationLabel.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);
            hideAnswerButton.setVisible(false);

        });

        tilePane.setVgap(10);
        tilePane.setHgap(10);

        exitButton.setOnAction(event -> {
            showExitDialog();
        });

        try {
            bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
            calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
            speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
            flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName());

            String currentQuestion = questionsState.getQuestions().get(selectedQuestion - 1).getQuestion().getQuestion();

            TextToSpeech.play(currentQuestion);
        });

    }

    public void onCalculatorClicked(MouseEvent mouseEvent) {

    }


    private void setupQuestionView() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + selectedQuestion + " of " + questions.size());

        questionLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getQuestion());

        optionA.setText(" (A) " + questions.get(selectedQuestion - 1).getQuestion().getOptionA());
        optionB.setText(" (B) " + questions.get(selectedQuestion - 1).getQuestion().getOptionB());
        optionC.setText(" (C) " + questions.get(selectedQuestion - 1).getQuestion().getOptionC());
        optionD.setText(" (D) " + questions.get(selectedQuestion - 1).getQuestion().getOptionD());


        explanationLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getAnswerExplanation());
        correctAnswerLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getOptionAnswer());

        updateExplanationView();
    }

    private void updateExplanationView() {
        SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                .get(viewModel.getSelectedSubject().getTableName());

        QuestionState questionState = questionsState.getQuestions().get(questionsState.getSelectedQuestion() - 1);

        if (!questionState.isShowExplanation() && !questionState.isShowAnswer()) {
            System.out.println("showing default explanation view");
            showCorrectAnswerButton.setVisible(true);
            showExplanationButton.setVisible(true);
            explanationTitle.setVisible(false);
            explanationLabel.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);

            hideAnswerButton.setVisible(false);

        } else if (questionState.isShowExplanation()) {

            System.out.println("showing explanation and answer view");
            explanationTitle.setVisible(true);
            explanationLabel.setVisible(true);
            showExplanationButton.setVisible(false);

            correctAnswerLabel.setVisible(true);
            correctAnswerTitle.setVisible(true);
            showCorrectAnswerButton.setVisible(false);

            hideAnswerButton.setText("Hide Explanation");
            hideAnswerButton.setVisible(true);


        } else if (questionState.isShowAnswer()) {

            System.out.println("showing show answer view alone");
            correctAnswerLabel.setVisible(true);
            correctAnswerTitle.setVisible(true);
            showCorrectAnswerButton.setVisible(false);
            showExplanationButton.setVisible(true);

            hideAnswerButton.setText("Hide Answer");
            hideAnswerButton.setVisible(true);
        }
    }

    private void setupTilePane() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(30, 30);
            r.setFill(Color.web("#ededed"));

            Label l = new Label(Integer.toString(i));
            if (subjectQuestionsState.getSelectedQuestion() == i) {
                l.setTextFill(Color.RED);
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

        Rectangle selectedQuestionRectangle = (Rectangle) selectedQuestionPane.getChildren().get(0);
        Label selectedQuestionText = (Label) selectedQuestionPane.getChildren().get(1);
        Label oldQuestionText = (Label) oldQuestionPane.getChildren().get(1);



        oldQuestionText.setTextFill(Color.BLACK);
        selectedQuestionText.setTextFill(Color.RED);

    }

    private void changeSelectedQuestion(int newValue) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

        questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

        questionLabel.setText(questions.get(newValue - 1).getQuestion().getQuestion());

        optionA.setText(" (A) " + questions.get(newValue - 1).getQuestion().getOptionA());
        optionB.setText(" (B) " + questions.get(newValue - 1).getQuestion().getOptionB());
        optionC.setText(" (C) " + questions.get(newValue - 1).getQuestion().getOptionC());
        optionD.setText(" (D) " + questions.get(newValue - 1).getQuestion().getOptionD());


        explanationLabel.setText(questions.get(newValue - 1).getQuestion().getAnswerExplanation());
        correctAnswerLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getOptionAnswer());

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
                ViewSwitcher.passData("pastQuestionsPanel");
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

    public static class InitialData {
        public List<SubjectListItemVM.SubjectState> questionData;

        public InitialData(List<SubjectListItemVM.SubjectState> questionData) {
            this.questionData = questionData;
        }
    }
}
