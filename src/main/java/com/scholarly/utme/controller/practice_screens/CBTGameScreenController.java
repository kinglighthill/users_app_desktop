package com.scholarly.utme.controller.practice_screens;

import com.scholarly.utme.controller.PQScreenController;
import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.newDb.ObjectiveQuestionDescription;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.ui.utils.FontUtil.GilroyFontFamily;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.practice_screens.CBTGameScreenVM;
import com.scholarly.utme.viewmodels.practice_screens.CBTGameScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import com.scholarly.utme.viewmodels.practice_screens.PracticeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.SceneLifecycle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@FxmlPath("/layouts/practice_screens/CBTGameScreen.fxml")
public class CBTGameScreenController implements FxmlView<CBTGameScreenVM>, Initializable, SceneLifecycle {
    private static final String TAG = "CBTGameScreenController: ";


    @InjectViewModel
    private CBTGameScreenVM viewModel;

    @FXML
    private WebView questionWebView, questionWithImageWebView;
    @FXML
    private ScrollPane questionScrollPane;
    @FXML
    private ImageView bookmarkImage, calculatorImage, speakerImage, reportImage, reportDialogCloseIcon, quesDescriptionCloseIcon, questionImage;
    @FXML
    private Button backButton, fiftyFiftyButton, optionAButton, optionBButton, optionCButton, optionDButton, exitButton, showAnswersButton, playAgainButton, submitReport;
    @FXML
    private Label questionNumberLabel, pageTitle, fiftyFiftyCount, questionLabel, correctAnswers, incorrectAnswers, questionAttempts, correctAnswersLabel, incorrectAnswersLabel, resultLabel, questionAttemptsLabel;
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
    private VBox resultDialog, reportDialog, incorrectAnswerPane, questionDescriptionDialog, questionCenterVBox, questionVBox, questionWithImageVBox, questionLayoutLeft, questionLayoutRight;

    @FXML
    private BorderPane practiceContentPane;
    @FXML
    private ProgressIndicator progressBar;

    private final Stage calculatorStage = new Stage();

    String idleButtonStyle =
            "-fx-background-color: #FF8D19;" +
                    "-fx-background-radius: 10";

    String hoveredButtonStyle =
            "-fx-background-color: #FFA347;" +
                    "-fx-background-radius: 10";


    private final String optionWebViewBC = "#FF8D19;";
    private final String hoverOptionWebViewBC = "#FFA347;";

    private QuestionState selectedQuestion;
    private Integer enteredBtn = null;

    private Media correctSound;
    private Media wrongSound;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProgressBar();
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Task<Boolean> contentTask = new Task<>() {
            @Override
            protected Boolean call() {
                viewModel.processInitialData(getInitialData());
                return true;
            }
        };
        contentTask.setOnSucceeded(
                succeedEvent -> Platform.runLater(() -> {
                    viewModel.selectedQuestionProperty().addListener((observableValue, number, t1) -> {
                        changeSelectedQuestion(t1.intValue());
//                        updateBookmarkIcon();
                    });

                    viewModel.fiftyFiftyCountProperty().addListener((observableValue, number, t1) -> {
                        updateFiftyFiftyButton(t1.intValue());
                    });
                    updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());
                    hideProgressBar();
                })
        );

        executorService.execute(contentTask);
        executorService.shutdown();

        correctSound = new Media(getClass().getResource("/sounds/correctAnswer.mp3").toExternalForm());
        wrongSound = new Media(getClass().getResource("/sounds/wrongAnswer.mp3").toExternalForm());

        questionLayout.widthProperty().addListener((observable, oldValue, newValue) -> {
            questionLayoutLeft.setMaxWidth(newValue.doubleValue()/2);
            questionLayoutRight.setMaxWidth(newValue.doubleValue()/2);
        });

        List<Button> options = new ArrayList<>();
        options.add(optionAButton);
        options.add(optionBButton);
        options.add(optionCButton);
        options.add(optionDButton);

        initializeViews();
        initializeFonts();
        initializeGestures();
        setupQuestionView();
//        updateBookmarkIcon();
        setupReportSection();

        fiftyFiftyButton.setOnAction(event -> {
            QuestionState questionState = viewModel.getQuestions().get(viewModel.getSelectedQuestion() - 1);

            int optionAnswerId = questionState.question().getQuestionAnswer().getId();

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

            if (questionState.question().getQuestionAnswer().getId() == 0) {
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

            if (questionState.question().getQuestionAnswer().getId() == 1) {
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

            if (questionState.question().getQuestionAnswer().getId() == 2) {
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

            if (questionState.question().getQuestionAnswer().getId() == 3) {
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
            button.setOnMouseEntered(e -> {
                button.setStyle(hoveredButtonStyle);
                refreshOption(button, true);
            });
            button.setOnMouseExited(e -> {
                button.setStyle(idleButtonStyle);
                refreshOption(button, false);
            });
            button.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 16));
            button.setWrapText(true);
            button.setTextFill(Color.WHITE);
        });

        /*bookmarkImage.setOnMouseClicked(event -> {
            viewModel.handleBookmarkClicked();
            updateBookmarkIcon();
        });*/

        /*reportImage.setOnMouseClicked(mouseEvent -> {
            Animations.showDialog(reportDialog, reportDialogDimmer);
        });*/

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

        readQuestionDesc.setOnMouseEntered(event -> readQuestionDesc.setUnderline(true));
        readQuestionDesc.setOnMouseExited(event -> readQuestionDesc.setUnderline(false));

        quesDescriptionCloseIcon.setOnMouseClicked(event -> {
            Animations.hideDialog(questionDescriptionDialog, reportDialogDimmer);
        });

        showAnswersButton.setOnAction(event -> {
            ExplanationScreenController.InitialData data = new ExplanationScreenController.InitialData(viewModel.getSubjectList(), viewModel.getQuestionDescriptions(), viewModel.getSubjectsQuestions(), viewModel.getSelectedSubjectYear(), SubjectListItemVM.Type.OBJECTIVE, Screens.CBT_GAME_SCREEN);
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);
        });

        backButton.setOnAction(e -> {
            resultDialogDimmer.setVisible(true);
            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Exit", null, "Are you sure you want to quit?");
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    Helper.moveToPQScreen(Screens.CBT_GAME_SCREEN, null);
                    resultDialogDimmer.setVisible(false);
                }
                resultDialogDimmer.setVisible(false);
                return buttonType;
            });
            dialog.show();
        });

        exitButton.setOnAction(e -> {
            ViewSwitcher.passData(new PQScreenController.InitialData(Screens.CBT_GAME_SCREEN, null));
            ViewSwitcher.showScreen(View.PQ_SCREEN);
        });
    }

    private void initializeViews() {
        ImageView view = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        backButton.setGraphic(view);

//        reportImage.setImage(new Image(getClass().getResource("/drawable/cbt_game_flag.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/cbt_game_speaker.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/cbt_game_calculator.png").toString()));
//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_green.png").toString()));
        reportDialogCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        quesDescriptionCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        questionDescriptionHeader.setFont(FontUtil.getFont(GilroyFontFamily.MEDIUM_ITALIC, 16));
        readQuestionDesc.setFont(FontUtil.getFont(GilroyFontFamily.MEDIUM_ITALIC, 16));
        questionLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 26));
//        questionWithImageLabel.setFont(FontUtil.getFont(GilroyFontFamily.SEMI_BOLD, 20));
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
                "-fx-background-color:#F1F9F0; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: #1B9D01; " +
                        "-fx-border-width: 0.6; " +
                        "-fx-border-radius: 5; " +
                        "-fx-cursor: hand;";

        exitButton.setOnMouseEntered(e -> {
            exitButton.setStyle(hoveredExitButtonStyle);
        });
        exitButton.setOnMouseExited(e -> {
            exitButton.setStyle(idleExitButtonStyle);
        });


        String idlePlayAgainButtonStyle = playAgainButton.getStyle();
        String hoveredPlayAgainButtonStyle =
                "-fx-background-color:#F1F9F0; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: #1B9D01; " +
                        "-fx-border-width: 0.6; " +
                        "-fx-border-radius: 5; " +
                        "-fx-cursor: hand;";

        playAgainButton.setOnMouseEntered(e -> {
            playAgainButton.setStyle(hoveredPlayAgainButtonStyle);
        });
        playAgainButton.setOnMouseExited(e -> {
            playAgainButton.setStyle(idlePlayAgainButtonStyle);
        });


        String idleShowAnswerButtonStyle = showAnswersButton.getStyle();
        String hoveredShowAnswerButtonStyle =
                "-fx-background-color: #157D01; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;";

        showAnswersButton.setOnMouseEntered(e -> {
            showAnswersButton.setStyle(hoveredShowAnswerButtonStyle);
        });
        showAnswersButton.setOnMouseExited(e -> {
            showAnswersButton.setStyle(idleShowAnswerButtonStyle);
        });
    }

    private void setupQuestionView() {
        List<QuestionState> questions = viewModel.getQuestions();
        selectedQuestion = questions.get(viewModel.getSelectedQuestion() - 1);
        int selectedQuestionNumber = selectedQuestion.question().getQuestionNumber();

        pageTitle.setText("CBT Game");
        pageTitle.setText(pageTitle.getText() + "  " + viewModel.getSelectedSubjectYear().get(selectedQuestion.subject()).getYear());
        questionNumberLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

        List<ObjectiveQuestionDescription> questionDescriptionList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                questionDescription.getId() == selectedQuestion.question().getQuestionDescriptionId()).collect(Collectors.toList());

        if (questionDescriptionList.isEmpty()) {
            readQuestionDesc.setVisible(false);
            questionDescriptionHeader.setText("");
        } else {
            questionDescriptionHeader.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", " "));
            questionDescriptionText.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
        }

        String questionText = selectedQuestion.question().getQuestion();

        questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWithImageVBox);

        if (questionText.contains("<img")) {
            showQuestionWithImage(questionText, Helper.isWebView(questionText));
        } else {
            showQuestion(questionText, Helper.isWebView(questionText));
        }

        optionAButton.setUserData(0);
        optionBButton.setUserData(1);
        optionCButton.setUserData(2);
        optionDButton.setUserData(3);

        loadOption(optionAButton, selectedQuestion.question().getOptionA().getText(), " (A) ");
        loadOption(optionBButton, selectedQuestion.question().getOptionB().getText(), " (B) ");
        loadOption(optionCButton, selectedQuestion.question().getOptionC().getText(), " (C) ");
        loadOption(optionDButton, selectedQuestion.question().getOptionD().getText(), " (D) ");

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);
    }

    private void changeSelectedQuestion(int newValue) {
        List<QuestionState> questions = viewModel.getQuestions();
        selectedQuestion = questions.get(newValue - 1);

        pageTitle.setText("CBT Game");
        pageTitle.setText(pageTitle.getText() + "  " + viewModel.getSelectedSubjectYear().get(selectedQuestion.subject()).getYear());

        questionNumberLabel.setText("Question " + newValue + " of " + questions.size());

        List<ObjectiveQuestionDescription> questionDescriptionList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                questionDescription.getId() == selectedQuestion.question().getQuestionDescriptionId()).toList();

        if (questionDescriptionList.isEmpty()) {
            readQuestionDesc.setVisible(false);
            questionDescriptionHeader.setText("");
        } else {
            questionDescriptionHeader.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", " "));
            questionDescriptionText.setText(questionDescriptionList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
        }

        String questionText = selectedQuestion.question().getQuestion();
        questionCenterVBox.getChildren().removeAll(questionScrollPane, questionWithImageVBox);

        if (questionText.contains("<img")) {
            showQuestionWithImage(questionText, Helper.isWebView(questionText));
        } else {
            showQuestion(questionText, Helper.isWebView(questionText));
        }

        optionAButton.setUserData(0);
        optionBButton.setUserData(1);
        optionCButton.setUserData(2);
        optionDButton.setUserData(3);

        loadOption(optionAButton, selectedQuestion.question().getOptionA().getText(), " (A) ");
        loadOption(optionBButton, selectedQuestion.question().getOptionB().getText(), " (B) ");
        loadOption(optionCButton, selectedQuestion.question().getOptionC().getText(), " (C) ");
        loadOption(optionDButton, selectedQuestion.question().getOptionD().getText(), " (D) ");

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);

        updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());
    }

    private void showQuestion(String questionText, boolean isWebView) {
        questionCenterVBox.getChildren().remove(questionWithImageVBox);
        if (!questionCenterVBox.getChildren().contains(questionScrollPane)) {
            questionCenterVBox.getChildren().add(questionScrollPane);
        }
        questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

        if (isWebView) {
            questionVBox.getChildren().remove(questionLabel);
            if (!questionVBox.getChildren().contains(questionWebView)) {
                questionVBox.getChildren().add(questionWebView);
            }
            System.out.println(TAG + "Question is WebView!");

            String content = Helper.loadLatexForCBT(getClass(), questionText, "28px", "#12AF20");
            questionWebView.setMaxHeight(500);
            questionWebView.getEngine().loadContent(content);

        } else {
            questionVBox.getChildren().remove(questionWebView);
            if (!questionVBox.getChildren().contains(questionLabel)) {
                questionVBox.getChildren().add(questionLabel);
                questionLabel.setText(questionText);
            }
        }
    }

    private void showQuestionWithImage(String question, boolean isWebView) {
        questionCenterVBox.getChildren().remove(questionScrollPane);
        if (!questionCenterVBox.getChildren().contains(questionWithImageVBox)) {
            questionCenterVBox.getChildren().add(questionWithImageVBox);
        }

        String extractedQuestion = extractQuestion(question, isWebView);

        String imageUrl = extractImageUrl(question);

        questionWithImageWebView.getEngine().loadContent(extractedQuestion);
        questionImage.setImage(new Image(getClass().getResource(imageUrl).toString()));

    }

    private String extractQuestion(String text, boolean isWebView) {
        String imageQuestion = text.substring(text.lastIndexOf("100%'")+6);
        if (isWebView) {
            imageQuestion = Helper.loadLatexForCBT(getClass(), imageQuestion, "28px", "#12AF20");
        }
        return imageQuestion;
    }

    private String extractImageUrl(String text) {
        int startIndexOfImg = text.indexOf("<img");
        int endIndexOfImg = text.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = text.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = text.indexOf("' width", startIndexOfImg);

        String imagePath = text.substring(startIndexOfImgPath, endIndexOfImgPath);

        return imagePath.replace("android_asset/images", "assets/images/pq");
    }

    private void updateBookmarkIcon() {
        List<QuestionState> questions = viewModel.getQuestions();
        ObjectiveQuestion selectedQuestion = questions.get(viewModel.getSelectedQuestion() - 1).question();

        List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks();

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_green.png").toString()));

        bookmarks.forEach(bookmark -> {
            if (bookmark.getQuestionId() == selectedQuestion.getId()) {
//                bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_green_filled.png").toString()));
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
        String question = selectedQuestion.question().getQuestion();

        StringBuilder textToRead = new StringBuilder(question);
        textToRead.append(". Option A, ").append(selectedQuestion.question().getOptionA().getText());
        textToRead.append(". Option B, ").append(selectedQuestion.question().getOptionB().getText());
        textToRead.append(". Option C, ").append(selectedQuestion.question().getOptionC().getText());
        textToRead.append(". Option D, ").append(selectedQuestion.question().getOptionD().getText());

        if (!Helper.isWebView(question)) {
            TextToSpeech.play(textToRead.toString());
        }
    }

    public void onCalculatorClicked() {
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
        if (viewModel.getSoundPreference()) {
            MediaPlayer correctMediaPlayer = new MediaPlayer(correctSound);
            correctMediaPlayer.setStopTime(Duration.millis(500));
            correctMediaPlayer.play();
            TextToSpeech.play("CORRECT!");
        }

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
        if (viewModel.getSoundPreference()) {
            MediaPlayer wrongMediaPlayer = new MediaPlayer(wrongSound);
            wrongMediaPlayer.setStopTime(Duration.millis(500));
            wrongMediaPlayer.play();
            TextToSpeech.play("WRONG!");
        }

        viewModel.setIncorrectAnswers(viewModel.getIncorrectAnswers() + 1);
        viewModel.setQuestionAttempts(viewModel.getQuestionAttempts() + 1);
    }

    public void loadOption(Button optionButton, String option, String optionLabel) {
        if (option.contains("<img")) {
            optionButton.setText(optionLabel);
            ImageView imageView = Helper.getImageFromText(getClass(), option);
            optionButton.setGraphic(imageView);
            optionButton.setContentDisplay(ContentDisplay.RIGHT);
        } else {
            String backgroundColor =
                    enteredBtn != null && enteredBtn == (int) optionButton.getUserData()
                            ? hoverOptionWebViewBC
                            : optionWebViewBC;

            if (Helper.isWebView(option)) {
                WebView webView = new WebView();
                String content = "<html><body style='background-color: " + backgroundColor +" color: #FFFFFF;'>"
                        + option + "</body></html>";
                webView.getEngine().loadContent(content);
                webView.setPrefWidth(512.0);
                webView.setPrefHeight(44.0);
                webView.setContextMenuEnabled(false);
                webView.setOnMouseClicked(e -> optionButton.fire());

                optionButton.setText(optionLabel);
                optionButton.setGraphic(webView);
                optionButton.setContentDisplay(ContentDisplay.RIGHT);
            } else {
                optionButton.setGraphic(null);
                optionButton.setText(optionLabel + option);
            }
        }
    }
    public void refreshOption(Button button, boolean entered) {
        String option;
        String optionLabel;

        switch ((int) button.getUserData()) {
            case 0 -> {
                option = selectedQuestion.question().getOptionA().getText();
                optionLabel = " (A) ";
                enteredBtn = entered ? 0 : null;
            }
            case 1 -> {
                option = selectedQuestion.question().getOptionB().getText();
                optionLabel = " (B) ";
                enteredBtn = entered ? 1 : null;
            }
            case 2 -> {
                option = selectedQuestion.question().getOptionC().getText();
                optionLabel = " (C) ";
                enteredBtn = entered ? 2 : null;
            }
            case 3 -> {
                option = selectedQuestion.question().getOptionD().getText();
                optionLabel = " (D) ";
                enteredBtn = entered ? 3 : null;
            }
            default -> {
                option = "";
                optionLabel = "";
            }
        }
        loadOption(button, option, optionLabel);
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

    private String parseQuestionWithImageView(String questionWithImageText) {
        return Helper.loadPQImageUrl(getClass(), questionImage, questionWithImageWebView, questionWithImageText);
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        practiceContentPane.setVisible(true);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        practiceContentPane.setVisible(false);
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
