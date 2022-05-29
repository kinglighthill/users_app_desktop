package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.FontUtil.GilroyFontFamily;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.CBTGameScreenVM;
import com.scholarly.utme.viewmodels.CBTGameScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/CBTGameScreen.fxml")
public class CBTGameScreenController implements FxmlView<CBTGameScreenVM>, Initializable {

    @InjectViewModel
    private CBTGameScreenVM viewModel;

    @FXML
    private ImageView bookmarkImage, calculatorImage, speakerImage, reportImage, reportDialogCloseIcon;

    @FXML
    private Button backButton, fiftyFiftyButton, optionAButton, optionBButton, optionCButton, optionDButton, exitButton, showAnswersButton, playAgainButton, submitReport;

    @FXML
    private Label questionNumberLabel, questionLabel, pageTitle, fiftyFiftyCount, correctAnswers, incorrectAnswers, questionAttempts, correctAnswersLabel, incorrectAnswersLabel, resultLabel, questionAttemptsLabel;

    @FXML
    private TextField enterCorrectAnswerField;

    @FXML
    private CheckBox questionErrorCheckBox, incorrectAnswerCheckBox, okayCheckBox;
  
    @FXML
    private HBox questionLayout;

    @FXML
    private Pane resultDialogDimmer, reportDialogDimmer;

    @FXML
    private VBox resultDialog, reportDialog, incorrectAnswerPane;
  
    private Stage calculatorStage = new Stage();

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

        viewModel.fiftyFiftyCountProperty().addListener((observableValue, number, t1) -> {
            updateFiftyFiftyButton(t1.intValue());
        });
        updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());

        fiftyFiftyButton.setOnAction(event -> {
            viewModel.setFiftyFiftyCount(viewModel.getFiftyFiftyCount() - 1);
            fiftyFiftyButton.setDisable(true);
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
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
            } else {
                dispatchAnswerIncorrect();
                optionAButton.setDisable(true);
            }
        });
        optionBButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            questionState.getSelectedOptions().add(optionBButton.getText());

            if (questionState.getQuestion().getOptionAnswer().equalsIgnoreCase(optionBButton.getText())) {
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
            } else {
                dispatchAnswerIncorrect();
                optionBButton.setDisable(true);
            }
        });
        optionCButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            questionState.getSelectedOptions().add(optionCButton.getText());

            if (questionState.getQuestion().getOptionAnswer().equalsIgnoreCase(optionCButton.getText())) {
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
            } else {
                dispatchAnswerIncorrect();
                optionCButton.setDisable(true);
            }
        });
        optionDButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            questionState.getSelectedOptions().add(optionDButton.getText());

            if (questionState.getQuestion().getOptionAnswer().equalsIgnoreCase(optionDButton.getText())) {
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
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

        exitButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        String idleExitButtonStyle = exitButton.getStyle();
        String hoveredExitButtonStyle =
                "-fx-background-color:#F1F9F0;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: #1B9D01;" +
                        "-fx-border-width: 0.6;" +
                        "-fx-border-radius: 5";

        exitButton.setOnMouseEntered(e -> {
            exitButton.setStyle(hoveredExitButtonStyle);
        });
        exitButton.setOnMouseExited(e -> {
            exitButton.setStyle(idleExitButtonStyle);
        });

        playAgainButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        String idlePlayAgainButtonStyle = playAgainButton.getStyle();
        String hoveredPlayAgainButtonStyle =
                "-fx-background-color:#F1F9F0;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: #1B9D01;" +
                        "-fx-border-width: 0.6;" +
                        "-fx-border-radius: 5";

        playAgainButton.setOnMouseEntered(e -> {
            playAgainButton.setStyle(hoveredPlayAgainButtonStyle);
        });
        playAgainButton.setOnMouseExited(e -> {
            playAgainButton.setStyle(idlePlayAgainButtonStyle);
        });

        showAnswersButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        String idleShowAnswerButtonStyle = showAnswersButton.getStyle();
        String hoveredShowAnswerButtonStyle =
                "-fx-background-color: #157D01;" +
                        "-fx-background-radius: 5;";

        showAnswersButton.setOnMouseEntered(e -> {
            showAnswersButton.setStyle(hoveredShowAnswerButtonStyle);
        });
        showAnswersButton.setOnMouseExited(e -> {
            showAnswersButton.setStyle(idleShowAnswerButtonStyle);
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
        fiftyFiftyCount.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        pageTitle.setFont(FontUtil.getFont(GilroyFontFamily.BOLD, 24));
        questionNumberLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 20));

        resultLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        correctAnswersLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        correctAnswers.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        incorrectAnswersLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        incorrectAnswers.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        questionAttempts.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        questionAttemptsLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));

        questionLabel.setLineSpacing(15);

        Image bookmarkIcon = new Image(getClass().getResource("/drawable/bookmark_2.png").toString());
        bookmarkImage.setFitWidth(20);
        bookmarkImage.setPreserveRatio(true);
        bookmarkImage.setImage(bookmarkIcon);

        Image reportIcon = new Image(getClass().getResource("/drawable/flag2.png").toString());
        reportImage.setPreserveRatio(true);
        reportImage.setImage(reportIcon);

        reportImage.setOnMouseClicked(mouseEvent -> {
            onReportImageClicked();
        });

        Image speakerIcon = new Image(getClass().getResource("/drawable/speaker.png").toString());
        speakerImage.setPreserveRatio(true);
        speakerImage.setImage(speakerIcon);

        speakerImage.setOnMouseClicked(mouseEvent -> {
            onSpeakerImageClicked();
        });

        Image calculator = new Image(getClass().getResource("/drawable/calculator_2.png").toString());
        calculatorImage.setFitWidth(35);
        calculatorImage.setPreserveRatio(true);
        calculatorImage.setImage(calculator);

        calculatorImage.setOnMouseClicked(mouseEvent -> {
            onCalculatorClicked();
        });

        backButton.setOnAction(e -> {
            ViewSwitcher.passData("cbtGamePanel");
            ViewSwitcher.showScreen(View.SELECT_SUBJECT_SCREEN);
        });
        exitButton.setOnAction(e -> {
            ViewSwitcher.passData("cbtGamePanel");
            ViewSwitcher.showScreen(View.SELECT_SUBJECT_SCREEN);
        });
        playAgainButton.setOnAction(event -> {
            hideResult();

        });
        showAnswersButton.setOnAction(event -> {
            ExplanationScreen.InitialData data = new ExplanationScreen.InitialData(viewModel.getSubjectList(), viewModel.getSubjectsQuestions());
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);
        });

        reportDialogCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        reportDialogCloseIcon.setOnMouseClicked(mouseEvent -> {
            hideReportDialog();
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

    private void updateFiftyFiftyButton(int intValue) {

        fiftyFiftyCount.setText(Integer.toString(intValue));

        if (intValue < 1) {
            fiftyFiftyButton.setDisable(true);
        } else {
            fiftyFiftyButton.setDisable(false);
        }
    }

    public void onReportImageClicked() {
        showReportDialog();
    }

    public void onSpeakerImageClicked() {
        List<QuestionState> questions = viewModel.getQuestions();
        QuestionState selectedQuestion = questions.get(viewModel.getSelectedQuestion() - 1);
        String question = selectedQuestion.getQuestion().getQuestion();

        TextToSpeech.play(question);
    }

    public void onCalculatorClicked() {
        System.out.println("Calculator clicked");
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
                System.out.println(e.getMessage());
            }
        } else {
            calculatorStage.toFront();
        }
    }

    private void showReportDialog() {
        reportDialogDimmer.setVisible(true);
        reportDialog.setVisible(true);


        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.5);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(reportDialogDimmer);

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(0);
        scaleTransition.setToX(1);
        scaleTransition.setFromY(0);
        scaleTransition.setToY(1);
        scaleTransition.setNode(reportDialog);
        scaleTransition.setDuration(Duration.millis(300));


        scaleTransition.play();
        fadeTransition.play();
    }

    private void hideReportDialog() {


        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(0);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(reportDialogDimmer);

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(1);
        scaleTransition.setToX(0);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(0);
        scaleTransition.setNode(reportDialog);
        scaleTransition.setDuration(Duration.millis(300));


        scaleTransition.play();
        fadeTransition.play();

        scaleTransition.setOnFinished(event -> {
            reportDialog.setVisible(false);
        });

        fadeTransition.setOnFinished(event -> {
            reportDialogDimmer.setVisible(false);
        });
    }

    private void showResult() {
        resultDialogDimmer.setVisible(true);
        resultDialog.setVisible(true);

        viewModel.setCorrectAnswers(viewModel.getCorrectAnswers() + 1);
        viewModel.setQuestionAttempts(viewModel.getQuestionAttempts() + 1);

        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.5);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(resultDialogDimmer);

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(0);
        scaleTransition.setToX(1);
        scaleTransition.setFromY(0);
        scaleTransition.setToY(1);
        scaleTransition.setNode(resultDialog);
        scaleTransition.setDuration(Duration.millis(300));


        scaleTransition.play();
        fadeTransition.play();

        questionAttempts.setText(String.valueOf(viewModel.getQuestionAttempts()));
        correctAnswers.setText(String.valueOf(viewModel.getCorrectAnswers()));
        incorrectAnswers.setText(String.valueOf(viewModel.getIncorrectAnswers()));
    }

    private void hideResult() {
        viewModel.setQuestions(viewModel.getQuestions());
        viewModel.setSelectedQuestion(1);
        viewModel.setCorrectAnswers(0);
        viewModel.setQuestionAttempts(0);
        viewModel.setIncorrectAnswers(0);

        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(0);
        fadeTransition.setDuration(Duration.millis(700));
        fadeTransition.setNode(resultDialogDimmer);

        ScaleTransition scaleTransition = new ScaleTransition();

        scaleTransition.setFromX(1);
        scaleTransition.setToX(0);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(0);
        scaleTransition.setNode(resultDialog);
        scaleTransition.setDuration(Duration.millis(300));


        scaleTransition.play();
        fadeTransition.play();

        fadeTransition.setOnFinished(event -> {
            resultDialogDimmer.setVisible(false);
        });
        scaleTransition.setOnFinished(event -> {
            resultDialog.setVisible(false);
        });

    }

    private void dispatchAnswerCorrect() {
        Media sound = new Media(getClass().getResource("/sounds/correctAnswer.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setStopTime(Duration.millis(700));
        mediaPlayer.play();

        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(questionLayout);

        fadeTransition.setOnFinished(event -> {
            viewModel.setSelectedQuestion(viewModel.getSelectedQuestion() + 1);

            FadeTransition reverseTransition = new FadeTransition();

            reverseTransition.setFromValue(0);
            reverseTransition.setToValue(1);

            reverseTransition.setDuration(Duration.millis(500));

            reverseTransition.setNode(questionLayout);

            reverseTransition.play();
        });

        fadeTransition.play();

        viewModel.setCorrectAnswers(viewModel.getCorrectAnswers() + 1);
        viewModel.setQuestionAttempts(viewModel.getQuestionAttempts() + 1);
    }

    private void dispatchAnswerIncorrect() {
        Media sound = new Media(getClass().getResource("/sounds/wrongAnswer.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setStopTime(Duration.millis(500));
        mediaPlayer.play();

        viewModel.setIncorrectAnswers(viewModel.getIncorrectAnswers() + 1);
        viewModel.setQuestionAttempts(viewModel.getQuestionAttempts() + 1);
    }


    private void setupQuestionView() {
        List<QuestionState> questions = viewModel.getQuestions();
        QuestionState selectedQuestion = questions.get(viewModel.getSelectedQuestion() - 1);
        int selectedQuestionNumber = selectedQuestion.getQuestion().getQuestionNumber();

        questionNumberLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

        questionLabel.setText(selectedQuestion.getQuestion().getQuestion());

        optionAButton.setText(selectedQuestion.getQuestion().getOptionA());
        optionBButton.setText(selectedQuestion.getQuestion().getOptionB());
        optionCButton.setText(selectedQuestion.getQuestion().getOptionC());
        optionDButton.setText(selectedQuestion.getQuestion().getOptionD());

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);
    }

    private void changeSelectedQuestion(int newValue) {
        List<QuestionState> questions = viewModel.getQuestions();
        QuestionState selectedQuestion = questions.get(newValue - 1);

        questionNumberLabel.setText("Question " + newValue + " of " + questions.size());

        questionLabel.setText(selectedQuestion.getQuestion().getQuestion());

        optionAButton.setText(selectedQuestion.getQuestion().getOptionA());
        optionBButton.setText(selectedQuestion.getQuestion().getOptionB());
        optionCButton.setText(selectedQuestion.getQuestion().getOptionC());
        optionDButton.setText(selectedQuestion.getQuestion().getOptionD());

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);

        updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());
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
