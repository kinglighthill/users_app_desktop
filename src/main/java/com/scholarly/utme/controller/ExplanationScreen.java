package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.ExplanationScreenVM;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


@FxmlPath("/layouts/ExplanationScreen.fxml")
public class ExplanationScreen implements FxmlView<ExplanationScreenVM>, Initializable {

    @InjectViewModel
    private ExplanationScreenVM viewModel;

    @FXML
    private TilePane tilePane;

    @FXML
    private ListView<Subject> subjectList;

    @FXML
    private Label questionOverviewLabel, questionLabel, optionA, optionB, optionC, optionD, noOptionSelected, explanationLabel;

    @FXML
    private Button prevButton, nextButton, exitButton;

    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage;

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

        tilePane.setVgap(10);
        tilePane.setHgap(10);

        exitButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        try {
            bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
            calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
            speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
            flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }





    public void onCalculatorClicked(MouseEvent mouseEvent) {

    }

    private void changeSelectedQuestion(int newValue) {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

        questionLabel.setText(questions.get(newValue - 1).getQuestion().getQuestion());

        optionA.setText(" (A) " + questions.get(newValue - 1).getQuestion().getOptionA());
        optionB.setText(" (B) " + questions.get(newValue - 1).getQuestion().getOptionB());
        optionC.setText(" (C) " + questions.get(newValue - 1).getQuestion().getOptionC());
        optionD.setText(" (D) " + questions.get(newValue - 1).getQuestion().getOptionD());

        optionA.setTextFill(Color.DARKGRAY);
        optionB.setTextFill(Color.DARKGRAY);
        optionC.setTextFill(Color.DARKGRAY);
        optionD.setTextFill(Color.DARKGRAY);

        String selectedOption = questions.get(newValue - 1).getSelectedOption();
        String questionAnswer = questions.get(newValue - 1).getQuestion().getOptionAnswer();

        if (selectedOption != null) {
            if (selectedOption.equalsIgnoreCase(questionAnswer)) {
                if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                }
            } else {
                if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                    optionA.setTextFill(Color.RED);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                    optionB.setTextFill(Color.RED);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                    optionC.setTextFill(Color.RED);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                    optionD.setTextFill(Color.RED);
                }

                if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                }
            }
            noOptionSelected.setVisible(false);
        }
        else {
            if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                optionA.setTextFill(Color.GREEN);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                optionB.setTextFill(Color.GREEN);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                optionC.setTextFill(Color.GREEN);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                optionD.setTextFill(Color.GREEN);
            }

            noOptionSelected.setVisible(true);
        }

        explanationLabel.setText(questions.get(newValue - 1).getQuestion().getAnswerExplanation());
    }

    private void setupQuestionView() {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + selectedQuestion + " of " + questions.size());

        questionLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getQuestion());

        optionA.setText(" (A) " + questions.get(selectedQuestion - 1).getQuestion().getOptionA());
        optionB.setText(" (B) " + questions.get(selectedQuestion - 1).getQuestion().getOptionB());
        optionC.setText(" (C) " + questions.get(selectedQuestion - 1).getQuestion().getOptionC());
        optionD.setText(" (D) " + questions.get(selectedQuestion - 1).getQuestion().getOptionD());

        optionA.setTextFill(Color.DARKGRAY);
        optionB.setTextFill(Color.DARKGRAY);
        optionC.setTextFill(Color.DARKGRAY);
        optionD.setTextFill(Color.DARKGRAY);

        String selectedOption = questions.get(selectedQuestion - 1).getSelectedOption();
        String questionAnswer = questions.get(selectedQuestion - 1).getQuestion().getOptionAnswer();

        if (selectedOption != null) {
            if (selectedOption.equalsIgnoreCase(questionAnswer)) {
                if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                }
            } else {
                if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                    optionA.setTextFill(Color.RED);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                    optionB.setTextFill(Color.RED);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                    optionC.setTextFill(Color.RED);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                    optionD.setTextFill(Color.RED);
                }

                if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                }
            }
            noOptionSelected.setVisible(false);
        }
        else {
            if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                optionA.setTextFill(Color.GREEN);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                optionB.setTextFill(Color.GREEN);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                optionC.setTextFill(Color.GREEN);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                optionD.setTextFill(Color.GREEN);
            }

            noOptionSelected.setVisible(true);
        }

        explanationLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getAnswerExplanation());
    }

    private void setupTilePane() {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(30, 30);
            r.setFill(Color.web("#ededed"));

            if (questions.get(i - 1).getQuestion().getOptionAnswer().equalsIgnoreCase(questions.get(i-1).getSelectedOption())) {
                r.setStroke(Color.GREEN);
                r.setStrokeWidth(2);
            } else if (questions.get(i-1).getSelectedOption() != null) {
                r.setStroke(Color.RED);
                r.setStrokeWidth(1);
            } else {
                r.setStroke(null);
            }

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

    public InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data -> " + data);
        return data;
    }

    public static class InitialData {
        private List<Subject> subjects;
        private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;

        public InitialData(List<Subject> subjects, HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions) {
            this.subjects = subjects;
            this.subjectsQuestions = subjectsQuestions;
        }

        public List<Subject> getSubjects() {
            return subjects;
        }

        public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
            return subjectsQuestions;
        }
    }
}
