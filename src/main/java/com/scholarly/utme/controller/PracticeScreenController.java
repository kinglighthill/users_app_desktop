package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import com.scholarly.utme.viewmodels.PracticeScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.PracticeScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@FxmlPath("/layouts/PracticeScreen.fxml")
public class PracticeScreenController implements FxmlView<PracticeScreenVM>, Initializable {

    private final static double rectangleBorderWidth = 2;
    private final static Color rectangleBorderSelectedColor = Color.ORANGE;

    @InjectViewModel
    private PracticeScreenVM viewModel;

    @FXML
    private TilePane tilePane;

    @FXML
    private ListView<Subject> subjectList;

    private ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    private RadioButton optionAButton, optionBButton, optionCButton, optionDButton;

    @FXML
    private Label questionOverviewLabel, questionLabel, timeLabel;

    @FXML
    private Button prevButton, nextButton, exitButton, submitButton;

    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage;

    private Stage calculatorStage = new Stage();


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

        toggleGroup.getToggles().addAll(optionAButton, optionBButton, optionCButton, optionDButton);

        optionAButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getQuestions()
                    .get(selectedQuestion - 1);

            if (questionState.getSelectedOption() == null) {
                onOptionSelected(selectedQuestion);
            }

            questionState.setSelectedOption(questionState.getQuestion().getOptionA());

        });
        optionAButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                int selectedQuestion = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getSelectedQuestion();

                QuestionState questionState = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getQuestions()
                        .get(selectedQuestion - 1);

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(questionState.getQuestion().getOptionA());
            }
        });

        optionBButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getQuestions()
                    .get(selectedQuestion - 1);

            if (questionState.getSelectedOption() == null) {
                onOptionSelected(selectedQuestion);
            }

            questionState.setSelectedOption(questionState.getQuestion().getOptionB());
        });
        optionBButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                int selectedQuestion = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getSelectedQuestion();

                QuestionState questionState = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getQuestions()
                        .get(selectedQuestion - 1);

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(questionState.getQuestion().getOptionB());
            }
        });

        optionCButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getQuestions()
                    .get(selectedQuestion - 1);

            if (questionState.getSelectedOption() == null) {
                onOptionSelected(selectedQuestion);
            }

            questionState.setSelectedOption(questionState.getQuestion().getOptionC());
        });
        optionCButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                int selectedQuestion = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getSelectedQuestion();

                QuestionState questionState = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getQuestions()
                        .get(selectedQuestion - 1);

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(questionState.getQuestion().getOptionC());
            }
        });

        optionDButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getQuestions()
                    .get(selectedQuestion - 1);

            if (questionState.getSelectedOption() == null) {
                onOptionSelected(selectedQuestion);
            }

            questionState.setSelectedOption(questionState.getQuestion().getOptionD());
        });
        optionDButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                int selectedQuestion = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getSelectedQuestion();

                QuestionState questionState = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getTableName())
                        .getQuestions()
                        .get(selectedQuestion - 1);

                if (questionState.getSelectedOption() == null) {
                    onOptionSelected(selectedQuestion);
                }

                questionState.setSelectedOption(questionState.getQuestion().getOptionD());
            }
        });

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
        });

        exitButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        submitButton.setOnAction(event -> {

            ResultScreenController.InitialData initialData =  new ResultScreenController.InitialData(viewModel.getResults(), viewModel.getSubjects(), viewModel.getSubjectsQuestions());

            ViewSwitcher.passData(initialData);
            ViewSwitcher.showScreen(View.RESULT_SCREEN);
        });

        try {
            bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
            calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
            speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
            flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        nextButton.setFocusTraversable(false);
//        prevButton.setFocusTraversable(false);
//        submitButton.setFocusTraversable(false);
//        exitButton.setFocusTraversable(false);
//        subjectList.setFocusTraversable(false);
//        optionAButton.setFocusTraversable(false);
//        optionBButton.setFocusTraversable(false);
//        optionCButton.setFocusTraversable(false);
//        optionDButton.setFocusTraversable(false);
    }

    private void onOptionSelected(int selectedQuestion) {
        StackPane selectedQuestionPane = (StackPane) tilePane.getChildren().get(selectedQuestion - 1);

        Rectangle selectedQuestionRectangle = (Rectangle) selectedQuestionPane.getChildren().get(0);

        selectedQuestionRectangle.setStrokeWidth(rectangleBorderWidth);
        selectedQuestionRectangle.setStroke(rectangleBorderSelectedColor);
    }

    private void changeSelectedQuestion(int newValue) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

        questionLabel.setText(questions.get(newValue - 1).getQuestion().getQuestion());

        optionAButton.setText(" (A) " + questions.get(newValue - 1).getQuestion().getOptionA());
        optionBButton.setText(" (B) " + questions.get(newValue - 1).getQuestion().getOptionB());
        optionCButton.setText(" (C) " + questions.get(newValue - 1).getQuestion().getOptionC());
        optionDButton.setText(" (D) " + questions.get(newValue - 1).getQuestion().getOptionD());

        String selectedOption = questions.get(newValue - 1).getSelectedOption();
        if (selectedOption != null) {
            if (selectedOption.equalsIgnoreCase(questions.get(newValue - 1).getQuestion().getOptionA())) {
                toggleGroup.selectToggle(optionAButton);
            } else if (selectedOption.equalsIgnoreCase(questions.get(newValue - 1).getQuestion().getOptionB())) {
                toggleGroup.selectToggle(optionBButton);
            } else if (selectedOption.equalsIgnoreCase(questions.get(newValue - 1).getQuestion().getOptionC())) {
                toggleGroup.selectToggle(optionCButton);
            } else if (selectedOption.equalsIgnoreCase(questions.get(newValue - 1).getQuestion().getOptionD())) {
                toggleGroup.selectToggle(optionDButton);
            }
        } else {
            toggleGroup.selectToggle(null);
        }
    }

    private void setupQuestionView() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + selectedQuestion + " of " + questions.size());

        questionLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getQuestion());

        optionAButton.setText(" (A) " + questions.get(selectedQuestion - 1).getQuestion().getOptionA());
        optionBButton.setText(" (B) " + questions.get(selectedQuestion - 1).getQuestion().getOptionB());
        optionCButton.setText(" (C) " + questions.get(selectedQuestion - 1).getQuestion().getOptionC());
        optionDButton.setText(" (D) " + questions.get(selectedQuestion - 1).getQuestion().getOptionD());

        String selectedOption = questions.get(selectedQuestion - 1).getSelectedOption();
        if (selectedOption != null) {
            if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionA())) {
                toggleGroup.selectToggle(optionAButton);
            } else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionB())) {
                toggleGroup.selectToggle(optionBButton);
            } else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionC())) {
                toggleGroup.selectToggle(optionCButton);
            } else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getQuestion().getOptionD())) {
                toggleGroup.selectToggle(optionDButton);
            }
        } else {
            toggleGroup.selectToggle(null);
        }
    }

    private void setupTilePane() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(30, 30);
            r.setFill(Color.web("#ededed"));

            if (questions.get(i-1).getSelectedOption() != null) {
                r.setStroke(Color.ORANGE);
                r.setStrokeWidth(2);
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


    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data -> " + data);
        return data;
    }

    @FXML
    public void onCalculatorClicked(MouseEvent mouseEvent) {
        if (!calculatorStage.isShowing()) {
            calculatorStage.initModality(Modality.WINDOW_MODAL);
            calculatorStage.setTitle("Calculator");
            calculatorStage.setResizable(false);

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/layouts/Calculator.fxml"));
                Scene scene = new Scene(root);

                calculatorStage.setScene(scene);
                calculatorStage.showAndWait();

            } catch (Exception e) {

            }
        } else {
            calculatorStage.toFront();
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println("Key pressed -> " + keyEvent.getCode());

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
