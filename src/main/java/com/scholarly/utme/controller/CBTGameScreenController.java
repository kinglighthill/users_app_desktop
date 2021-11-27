package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.FontUtil.GilroyFontFamily;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.CBTGameScreenVM;
import com.scholarly.utme.viewmodels.CBTGameScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/CBTGameScreen.fxml")
public class CBTGameScreenController implements FxmlView<CBTGameScreenVM>, Initializable {


    @InjectViewModel
    private CBTGameScreenVM viewModel;

    @FXML
    private ImageView bookmarkImage, calculatorImage;

    @FXML
    private Button backButton, fiftyFiftyButton, optionAButton, optionBButton, optionCButton, optionDButton;

    @FXML
    private Label questionNumberLabel, questionLabel, pageTitle;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Button> options = new ArrayList<>();
        options.add(optionBButton);
        options.add(optionCButton);
        options.add(optionAButton);
        options.add(optionDButton);



        viewModel.processInitialData(getInitialData());

        setupQuestionView();

        ImageView view = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        view.setFitHeight(30);
        view.setPreserveRatio(true);

        backButton.setGraphic(view);

        viewModel.selectedQuestionProperty().addListener((observableValue, number, t1) -> {
            changeSelectedQuestion(t1.intValue());
        });

        fiftyFiftyButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            String optionAnswer = questionState.getQuestion().getOptionAnswer();

            int enabled = 0;

            for (int i = 0; i < options.size(); i++) {
                if (!options.get(i).isDisabled()) {
                    enabled++;
                }
            }

            if (enabled > 1) {
                options.stream()
                        .unordered()
                        .filter(button -> !button.getText().equalsIgnoreCase(optionAnswer) && !button.isDisabled())
                        .limit(enabled - 2)
                        .forEach(button -> button.setDisable(true));
            }
        });

        optionAButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            questionState.getSelectedOptions().add(optionAButton.getText());

            if (questionState.getQuestion().getOptionAnswer().equalsIgnoreCase(optionAButton.getText())) {
                dispatchAnswerCorrect();
                viewModel.setSelectedQuestion(viewModel.getSelectedQuestion() + 1);
            } else {
                dispatchAnswerIncorrect();
                optionAButton.setDisable(true);
            }
        });

        optionBButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            questionState.getSelectedOptions().add(optionBButton.getText());

            if (questionState.getQuestion().getOptionAnswer().equalsIgnoreCase(optionBButton.getText())) {
                dispatchAnswerCorrect();
                viewModel.setSelectedQuestion(viewModel.getSelectedQuestion() + 1);
            } else {
                dispatchAnswerIncorrect();
                optionBButton.setDisable(true);
            }
        });

        optionCButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            questionState.getSelectedOptions().add(optionCButton.getText());

            if (questionState.getQuestion().getOptionAnswer().equalsIgnoreCase(optionCButton.getText())) {
                dispatchAnswerCorrect();
                viewModel.setSelectedQuestion(viewModel.getSelectedQuestion() + 1);
            } else {
                dispatchAnswerIncorrect();
                optionCButton.setDisable(true);
            }
        });

        optionDButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            questionState.getSelectedOptions().add(optionDButton.getText());

            if (questionState.getQuestion().getOptionAnswer().equalsIgnoreCase(optionDButton.getText())) {
                dispatchAnswerCorrect();
                viewModel.setSelectedQuestion(viewModel.getSelectedQuestion() + 1);
            } else {
                dispatchAnswerIncorrect();
                optionDButton.setDisable(true);
            }
        });

        String idleStyle =
                "-fx-background-color: #FFA347;" +
                "-fx-background-radius: 10";

        String hoveredStyle =
                "-fx-background-color: #FF8D19;" +
                        "-fx-background-radius: 10";


        fiftyFiftyButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        String idleFiftyFiftyStyle = fiftyFiftyButton.getStyle();
        String hoveredFiftyFiftyStyle =
                "-fx-background-color: #73D25E;" +
                "-fx-background-radius: 500;" +
                "-fx-border-color: #1B9D01;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 500";

        fiftyFiftyButton.setOnMouseEntered(e -> {
            fiftyFiftyButton.setStyle(hoveredFiftyFiftyStyle);
            fiftyFiftyButton.setTextFill(Color.WHITE);
        });
        fiftyFiftyButton.setOnMouseExited(e -> {
            fiftyFiftyButton.setStyle(idleFiftyFiftyStyle);
            fiftyFiftyButton.setTextFill(Color.web("#1B9D01"));
        });

        backButton.setBackground(Background.EMPTY);

        options.forEach(button -> {
            button.setStyle(idleStyle);
            button.setOnMouseEntered(e -> button.setStyle(hoveredStyle));
            button.setOnMouseExited(e -> button.setStyle(idleStyle));
            button.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 24));
            button.setTextFill(Color.WHITE);
        });

        questionLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 28));
        pageTitle.setFont(FontUtil.getFont(GilroyFontFamily.BOLD, 24));
        questionNumberLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 20));

        questionLabel.setLineSpacing(15);


        Image bookmark = new Image(getClass().getResource("/drawable/bookmark_2.png").toString());
        bookmarkImage.setFitWidth(20);
        bookmarkImage.setPreserveRatio(true);
        bookmarkImage.setImage(bookmark);

        Image calculator = new Image(getClass().getResource("/drawable/calculator_2.png").toString());
        calculatorImage.setFitWidth(35);
        calculatorImage.setPreserveRatio(true);
        calculatorImage.setImage(calculator);

        backButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });
    }


    private void dispatchAnswerCorrect() {

    }

    private void dispatchAnswerIncorrect() {

    }


    private void setupQuestionView() {
        System.out.println("Option answer is -> " + viewModel.getQuestions().get(viewModel.getSelectedQuestion()).getQuestion().getOptionAnswer());
        List<QuestionState> questions = viewModel.getQuestions();
        int selectedQuestion = viewModel.getSelectedQuestion();

        questionNumberLabel.setText("Question " + selectedQuestion + " of " + questions.size());

        questionLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getQuestion());

        optionAButton.setText(questions.get(selectedQuestion - 1).getQuestion().getOptionA());
        optionBButton.setText(questions.get(selectedQuestion - 1).getQuestion().getOptionB());
        optionCButton.setText(questions.get(selectedQuestion - 1).getQuestion().getOptionC());
        optionDButton.setText(questions.get(selectedQuestion - 1).getQuestion().getOptionD());

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);
    }

    private void changeSelectedQuestion(int newValue) {
        List<QuestionState> questions = viewModel.getQuestions();
        questionNumberLabel.setText("Question " + newValue + " of " + questions.size());

        questionLabel.setText(questions.get(newValue - 1).getQuestion().getQuestion());

        optionAButton.setText(questions.get(newValue - 1).getQuestion().getOptionA());
        optionBButton.setText(questions.get(newValue - 1).getQuestion().getOptionB());
        optionCButton.setText(questions.get(newValue - 1).getQuestion().getOptionC());
        optionDButton.setText(questions.get(newValue - 1).getQuestion().getOptionD());

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);
    }


    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        return data;
    }


    public static class InitialData {
        public List<SubjectState> questionData;
        public boolean shuffleQuestions;
        public boolean shuffleAnswers;

        public InitialData(List<SubjectState> questionData, boolean shuffleQuestions, boolean shuffleAnswers) {
            this.questionData = questionData;
            this.shuffleQuestions = shuffleQuestions;
            this.shuffleAnswers = shuffleAnswers;
        }
    }
}
