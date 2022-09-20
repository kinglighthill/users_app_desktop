package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.model.QuestionDescription;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.ui.utils.FontUtil.GilroyFontFamily;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.CBTGameScreenVM;
import com.scholarly.utme.viewmodels.CBTGameScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.SceneLifecycle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.stream.Collectors;

import static com.scholarly.utme.util.Constants.CBT_GAME_SCREEN;

@FxmlPath("/layouts/CBTGameScreen.fxml")
public class CBTGameScreenController implements FxmlView<CBTGameScreenVM>, Initializable, SceneLifecycle {

    public static final String TAG = "CBTGameScreenController: ";

    @InjectViewModel
    private CBTGameScreenVM viewModel;

    @FXML
    private ImageView bookmarkImage, calculatorImage, speakerImage, reportImage, reportDialogCloseIcon, quesDescriptionCloseIcon;

    @FXML
    private Button backButton, fiftyFiftyButton, optionAButton, optionBButton, optionCButton, optionDButton, exitButton, showAnswersButton, playAgainButton, submitReport;

    @FXML
    private Label questionNumberLabel, questionLabel, pageTitle, fiftyFiftyCount, correctAnswers, incorrectAnswers, questionAttempts, correctAnswersLabel, incorrectAnswersLabel, resultLabel, questionAttemptsLabel;

    @FXML
    private Label questionDescriptionHeader, readQuestionDesc, questionDescriptionText;

    @FXML
    private TextField enterCorrectAnswerField;

    @FXML
    private CheckBox questionErrorCheckBox, incorrectAnswerCheckBox, okayCheckBox;
  
    @FXML
    private HBox questionLayout;

    @FXML
    private Pane resultDialogDimmer, reportDialogDimmer;

    @FXML
    private VBox resultDialog, reportDialog, incorrectAnswerPane, questionDescriptionDialog;
  
    private Stage calculatorStage = new Stage();

    String idleButtonStyle =
            "-fx-background-color: #FF8D19;" +
                    "-fx-background-radius: 10";

    String hoveredButtonStyle =
            "-fx-background-color: #FFA347;" +
                    "-fx-background-radius: 10";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Button> options = new ArrayList<>();
        options.add(optionAButton);
        options.add(optionBButton);
        options.add(optionCButton);
        options.add(optionDButton);

        viewModel.processInitialData(getInitialData());

        initializeViews();
        initializeFonts();
        initializeGestures();
        setupQuestionView();
        updateBookmarkIcon();
        setupReportSection();


        viewModel.selectedQuestionProperty().addListener((observableValue, number, t1) -> {
            changeSelectedQuestion(t1.intValue());
            updateBookmarkIcon();
        });

        viewModel.fiftyFiftyCountProperty().addListener((observableValue, number, t1) -> {
            updateFiftyFiftyButton(t1.intValue());
        });
        updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());

        fiftyFiftyButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            int optionAnswerId = questionState.getQuestion().getQuestionAnswer().getId();

            int enabledButtons = 0;

            for (Button option : options) {
                if (!option.isDisabled()) {
                    enabledButtons++;
                }
            }

            if (enabledButtons > 2) {
                options.stream()
                        .unordered()
                        .filter(button -> !((int) button.getUserData() == optionAnswerId) && !button.isDisabled())
                        .limit(enabledButtons - 2)
                        .forEach(button -> button.setDisable(true));

                viewModel.setFiftyFiftyCount(viewModel.getFiftyFiftyCount() - 1);
                fiftyFiftyButton.setDisable(true);
            }
        });

        optionAButton.setOnAction(event -> {
            fiftyFiftyButton.setDisable(false);

            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

//            questionState.getSelectedOptions().add(optionAButton.getText());

            if (questionState.getQuestion().getQuestionAnswer().getId() == 0) {
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
            } else {
                dispatchAnswerIncorrect();
                optionAButton.setDisable(true);
            }

            int enabledButtons = 0;
            for (Button option : options) {
                if (!option.isDisabled()) {
                    enabledButtons++;
                }
            }
            if (enabledButtons <= 2) {
                fiftyFiftyButton.setDisable(true);
            }
        });

        optionBButton.setOnAction(event -> {
            fiftyFiftyButton.setDisable(false);

            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

//            questionState.getSelectedOptions().add(optionBButton.getText());

            if (questionState.getQuestion().getQuestionAnswer().getId() == 1) {
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
            } else {
                dispatchAnswerIncorrect();
                optionBButton.setDisable(true);
            }

            int enabledButtons = 0;
            for (Button option : options) {
                if (!option.isDisabled()) {
                    enabledButtons++;
                }
            }
            if (enabledButtons <= 2) {
                fiftyFiftyButton.setDisable(true);
            }
        });

        optionCButton.setOnAction(event -> {
            fiftyFiftyButton.setDisable(false);

            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

//            questionState.getSelectedOptions().add(optionCButton.getText());

            if (questionState.getQuestion().getQuestionAnswer().getId() == 2) {
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
            } else {
                dispatchAnswerIncorrect();
                optionCButton.setDisable(true);
            }

            int enabledButtons = 0;
            for (Button option : options) {
                if (!option.isDisabled()) {
                    enabledButtons++;
                }
            }
            if (enabledButtons <= 2) {
                fiftyFiftyButton.setDisable(true);
            }
        });

        optionDButton.setOnAction(event -> {
            fiftyFiftyButton.setDisable(false);

            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

//            questionState.getSelectedOptions().add(optionDButton.getText());

            if (questionState.getQuestion().getQuestionAnswer().getId() == 3) {
                if (viewModel.getSelectedQuestion() == viewModel.getQuestions().size()) {
                    showResult();
                } else {
                    dispatchAnswerCorrect();
                }
            } else {
                dispatchAnswerIncorrect();
                optionDButton.setDisable(true);
            }

            int enabledButtons = 0;
            for (Button option : options) {
                if (!option.isDisabled()) {
                    enabledButtons++;
                }
            }
            if (enabledButtons <= 2) {
                fiftyFiftyButton.setDisable(true);
            }
        });


        options.forEach(button -> {
            button.setStyle(idleButtonStyle);
            button.setOnMouseEntered(e -> button.setStyle(hoveredButtonStyle));
            button.setOnMouseExited(e -> button.setStyle(idleButtonStyle));
            button.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 16));
            button.setWrapText(true);
            button.setTextFill(Color.WHITE);
        });


        bookmarkImage.setOnMouseClicked(event -> {
            viewModel.handleBookmarkClicked();
            updateBookmarkIcon();
        });

        reportImage.setOnMouseClicked(mouseEvent -> {
            Animations.showDialog(reportDialog, reportDialogDimmer);
        });

        reportDialogCloseIcon.setOnMouseClicked(mouseEvent -> {
            questionErrorCheckBox.setSelected(false);
            incorrectAnswerCheckBox.setSelected(false);
            okayCheckBox.setSelected(false);
            Animations.hideDialog(reportDialog, reportDialogDimmer);
        });

        speakerImage.setOnMouseClicked(mouseEvent -> {
            onSpeakerImageClicked();
        });

        calculatorImage.setOnMouseClicked(mouseEvent -> {
            onCalculatorClicked();
        });

        playAgainButton.setOnAction(event -> {
            hideResult();
        });
        
        readQuestionDesc.setOnMouseClicked(event -> {
            Animations.showDialog(questionDescriptionDialog, reportDialogDimmer);
        });

        quesDescriptionCloseIcon.setOnMouseClicked(event -> {
            Animations.hideDialog(questionDescriptionDialog, reportDialogDimmer);
        });

        showAnswersButton.setOnAction(event -> {
            ExplanationScreenController.InitialData data = new ExplanationScreenController.InitialData(viewModel.getSubjectList(), viewModel.getSubjectsQuestions(), SubjectListItemVM.Type.OBJECTIVE);
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);
        });

        backButton.setOnAction(e -> {
            resultDialogDimmer.setVisible(true);
            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Exit", null, "Are you sure you want to Exit?");
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ViewSwitcher.passData(new HomeScreenController.InitialData(Screens.CBT_GAME_SCREEN));
                    ViewSwitcher.showScreen(View.HOME_SCREEN);
                    resultDialogDimmer.setVisible(false);
                }
                resultDialogDimmer.setVisible(false);
                return buttonType;
            });
            dialog.show();
        });

        exitButton.setOnAction(e -> {
            ViewSwitcher.passData(new HomeScreenController.InitialData(CBT_GAME_SCREEN));
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });


    }

    private void initializeViews() {
        ImageView view = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        backButton.setGraphic(view);

        reportImage.setImage(new Image(getClass().getResource("/drawable/cbt_game_flag.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/cbt_game_speaker.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/cbt_game_calculator.png").toString()));
        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_green.png").toString()));
        reportDialogCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        quesDescriptionCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        questionDescriptionHeader.setFont(FontUtil.getFont(GilroyFontFamily.MEDIUM_ITALIC, 16));
        readQuestionDesc.setFont(FontUtil.getFont(GilroyFontFamily.MEDIUM_ITALIC, 16));
        questionLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 22));
//        questionLabel.setLineSpacing(5);
        fiftyFiftyButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        fiftyFiftyCount.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 16));
        showAnswersButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        pageTitle.setFont(FontUtil.getFont(GilroyFontFamily.BOLD, 24));
        questionNumberLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 20));
        playAgainButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));

        resultLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        correctAnswersLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        correctAnswers.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        incorrectAnswersLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        incorrectAnswers.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        questionAttempts.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
        questionAttemptsLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));

        exitButton.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 18));
    }

    private void initializeGestures() {
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
    }

    private void updateBookmarkIcon() {
        List<QuestionState> questions = viewModel.getQuestions();
        QuestionState selectedQuestion = questions.get(viewModel.getSelectedQuestion() - 1);
        int selectedQuestionNumber = selectedQuestion.getQuestion().getQuestionNumber();

        List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks();

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_green.png").toString()));

        bookmarks.forEach(bookmark -> {
            if (bookmark.getQuestionId() == (questions.get(selectedQuestionNumber - 1).getQuestion()).getId()) {
                bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_green_filled.png").toString()));
//                System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
            }
        });
    }

    private void updateFiftyFiftyButton(int count) {
        fiftyFiftyCount.setText(Integer.toString(count));

        if (count < 1) {
            fiftyFiftyButton.setDisable(true);
        } else {
            fiftyFiftyButton.setDisable(false);
        }
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
                Parent root = FXMLLoader.load(getClass().getResource("/layouts/CalculatorView.fxml"));
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
        mediaPlayer.setStopTime(Duration.millis(500));
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
        
        List<QuestionDescription> questionDescriptionList = viewModel.getQuestionDescriptions().stream().filter(questionDescription -> 
                questionDescription.getId() == selectedQuestion.getQuestion().getQuestionDescriptionId()).collect(Collectors.toList());
        
        if (questionDescriptionList.isEmpty()) {
            readQuestionDesc.setVisible(false);
            questionDescriptionHeader.setText("");
        } else {
            questionDescriptionHeader.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", " "));
            questionDescriptionText.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
        }

        String questionText = selectedQuestion.getQuestion().getQuestion();
        questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

        optionAButton.setText(selectedQuestion.getQuestion().getOptionA().getText());
        optionAButton.setUserData(0);
        optionBButton.setText(selectedQuestion.getQuestion().getOptionB().getText());
        optionBButton.setUserData(1);
        optionCButton.setText(selectedQuestion.getQuestion().getOptionC().getText());
        optionCButton.setUserData(2);
        optionDButton.setText(selectedQuestion.getQuestion().getOptionD().getText());
        optionDButton.setUserData(3);

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);
    }

    private void changeSelectedQuestion(int newValue) {
        List<QuestionState> questions = viewModel.getQuestions();
        QuestionState selectedQuestion = questions.get(newValue - 1);

        questionNumberLabel.setText("Question " + newValue + " of " + questions.size());

        List<QuestionDescription> questionDescriptionList = viewModel.getQuestionDescriptions().stream().filter(questionDescription ->
                questionDescription.getId() == selectedQuestion.getQuestion().getQuestionDescriptionId()).collect(Collectors.toList());

        if (questionDescriptionList.isEmpty()) {
            readQuestionDesc.setVisible(false);
            questionDescriptionHeader.setText("");
        } else {
            questionDescriptionHeader.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", " "));
            questionDescriptionText.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
        }

        String questionText = selectedQuestion.getQuestion().getQuestion();
        questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

        optionAButton.setText(selectedQuestion.getQuestion().getOptionA().getText());
        optionAButton.setUserData(0);
        optionBButton.setText(selectedQuestion.getQuestion().getOptionB().getText());
        optionBButton.setUserData(1);
        optionCButton.setText(selectedQuestion.getQuestion().getOptionC().getText());
        optionCButton.setUserData(2);
        optionDButton.setText(selectedQuestion.getQuestion().getOptionD().getText());
        optionDButton.setUserData(3);

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);

        updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());
    }

    private void setupReportSection() {
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
