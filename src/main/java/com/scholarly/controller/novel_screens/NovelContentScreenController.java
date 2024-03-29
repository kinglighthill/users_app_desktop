package com.scholarly.controller.novel_screens;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.data.model.newDb.NovelLastSession;
import com.scholarly.data.model.novels.*;
import com.scholarly.ui.cellFactories.NovelChapterQuestionListCellFactory;
import com.scholarly.ui.cellFactories.NovelChapterListCellFactory;
import com.scholarly.ui.utils.*;
import com.scholarly.ui.utils.View;
import com.scholarly.viewmodels.novel_screens.NovelContentScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@FxmlPath("/layouts/novel_screens/NovelContentScreen.fxml")
public class NovelContentScreenController implements FxmlView<NovelContentScreenVM>, Initializable {
    private static final String TAG = "NovelContentScreenController: ";

    @FXML
    private StackPane contentPane;

    @FXML
    private ListView<NovelChapter> chaptersList;
    @FXML
    private ListView<NovelObjectiveQuestion> chapterQuestionsList;
    @FXML
    private Panel questionFooter, resultPane;
    @FXML
    private VBox dimmer, exitNovelDialog, exitDialogDimmer, quitDialogDimmer, quitQuizDialog, activateNowDialog;
    @FXML
    private Button backButton, prevButton, nextButton, takeQuizButton, quitQuizButton, fiftyFiftyButton, exitDialogCancelButton, exitDialogExitButton, quitDialogCancelButton, quitDialogQuitButton;
    @FXML
    private Button optionAButton, optionBButton, optionCButton, optionDButton, optionEButton, answerContinueButton, tryAgainButton, resultContinueButton, activateNowButton;
    @FXML
    private Label pageTitle, chapterTitle, chapterCount, questionNumberLabel, fiftyFiftyCount, questionLabel, answerLabel, explanationLabel;

    @FXML
    private Label numOfCorrectAnsLabel, numOfGuessesLabel, scorePercentageLabel, resultHeader, chapterQuizHeader, showAllAnswersLabel, activateHeaderText;
    @FXML
    private HBox chapterHeader;
    @FXML
    private VBox novelContentVBox, novelChaptersVBox, chaptersListPane, chapterQuizPane, answerPane, questionPane, chapterQuizQuestionPane;
    @FXML
    private ImageView bookmarkImage, reportImage, speakerImage, exitQuestionMarkIcon, quitQuestionMarkIcon, activateNowCloseIcon, activateNowPadlockIcon, greenTickIcon1, greenTickIcon2, greenTickIcon3, greenTickIcon4, greenTickIcon5;

    @FXML
    private ProgressIndicator progressBar;

    @InjectViewModel
    private NovelContentScreenVM viewModel;

    List<Button> options;

    String idleButtonStyle =
            "-fx-background-color: #FF8D19;" +
                    "-fx-background-radius: 5";

    String hoveredButtonStyle =
            "-fx-background-color: #FFA347;" +
                    "-fx-background-radius: 5";

    private SimpleObjectProperty<NovelChapter> previouslySelectedChapter = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showProgressBar();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Task<Boolean> contentTask = new Task<>() {
            @Override
            protected Boolean call() {
                viewModel.processInitialData(getInitialData());
                return true;
            }
        };
        contentTask.setOnSucceeded(
                event -> Platform.runLater(() -> {
                    chaptersList.setCellFactory(new NovelChapterListCellFactory());
                    chaptersList.setItems(viewModel.getChapters());
                    chaptersList.getSelectionModel().select(viewModel.getSelectedChapter());

                    chaptersList.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
                        viewModel.setSelectedChapter(newValue);
                    }));

                    pageTitle.setText(viewModel.getNovelModel().getNovel().getName());
                    chapterCount.setText(viewModel.getSelectedChapter().getOrder() + " of " + chaptersList.getItems().size());
                    chapterTitle.setText(viewModel.getSelectedChapter().getChapterHeading());
                    renderNovel(viewModel.getSelectedChapter());
                    hideProgressBar();
                })
        );

        executorService.execute(contentTask);
        executorService.shutdown();

        options = new ArrayList<>();
        options.add(optionAButton);
        options.add(optionBButton);
        options.add(optionCButton);
        options.add(optionDButton);
        options.add(optionEButton);

        initializeViews();
        initializeFont();
        initializeGestures();
//        setupQuizView();

        viewModel.selectedChapterProperty().addListener(((observableValue, oldValue, newValue) -> {
            previouslySelectedChapter.set(oldValue);
            if (!newValue.isFree()) {
                showActivateDialog();
            } else {
                renderNovel(newValue);
                chapterQuizHeader.setText("Chapter " + newValue.getPosition() + " Quiz");

                chapterCount.setText(newValue.getOrder() + " of " + chaptersList.getItems().size());
                chapterTitle.setText(newValue.getChapterHeading());
            }
//            System.out.println(TAG + "Selected Chapter Position -> " + newValue.getPosition());
//            System.out.println(TAG + "Selected Chapter Sections -> " + viewModel.getChapterSections().get(newValue.getId()).stream().collect(Collectors.toList()));
        }));

        prevButton.disableProperty().bind(Bindings.equal(0, chaptersList.getSelectionModel().selectedIndexProperty()).or(chapterQuizPane.visibleProperty()));
        nextButton.disableProperty().bind(Bindings.equal(chaptersList.getSelectionModel().selectedIndexProperty(), chaptersList.getItems().size()-1).or(chapterQuizPane.visibleProperty()));

        nextButton.setOnAction(event -> {
            int selectedIndex = chaptersList.getSelectionModel().getSelectedIndex();
            previouslySelectedChapter.set(chaptersList.getSelectionModel().getSelectedItem());
            chaptersList.getSelectionModel().select(selectedIndex + 1);
            viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());
            if (!viewModel.getSelectedChapter().isFree()) {
                showActivateDialog();
            }
        });

        prevButton.setOnAction(event -> {
            int selectedIndex = chaptersList.getSelectionModel().getSelectedIndex();
            chaptersList.getSelectionModel().select(selectedIndex - 1);
            viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());
        });;

        backButton.setOnAction(event -> {
            exitDialogDimmer.setVisible(true);

            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm", null, "Are you sure you want to stop reading?");
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    NovelLastSession novelLastSession = new NovelLastSession(
                            viewModel.getUser().getId().hashCode(),
                            viewModel.getSelectedChapter().getId(),
                            viewModel.getSelectedChapter().getChapterHeading(),
                            viewModel.getUser().getId()
                    );

                    viewModel.putLastSession(novelLastSession);

                    ViewSwitcher.passData(new NovelChapterListController.InitialData(viewModel.getNovelModel(), Screens.NOVELS_SCREEN));
                    ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
                } else {
                    exitDialogDimmer.setVisible(false);
                }

                return buttonType;
            });
            dialog.show();
        });

        /*backButton.setOnAction(event -> {
            exitDialogDimmer.setVisible(true);
            Animations.translateIn(exitNovelDialog, 300);
        });
        exitDialogExitButton.setOnAction(event -> {
            exitDialogDimmer.setVisible(false);
            NovelLastSession novelLastSession = new NovelLastSession(
                    viewModel.getUser().getId().hashCode(),
                    viewModel.getSelectedChapter().getId(),
                    viewModel.getSelectedChapter().getChapterHeading(),
                    viewModel.getUser().getId()
            );

            viewModel.putLastSession(novelLastSession);

            ViewSwitcher.passData(new NovelChapterListController.InitialData(viewModel.getNovelModel()));
            ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
        });
        exitDialogCancelButton.setOnAction(event -> {
            exitDialogDimmer.setVisible(false);
            Animations.translateOut(exitNovelDialog, 300);
        });*/

        activateNowCloseIcon.setOnMouseClicked(event -> {
            chaptersList.getSelectionModel().select(previouslySelectedChapter.get());
            viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());
            Animations.hideDialog(activateNowDialog, exitDialogDimmer);
        });

        activateNowButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });


        /* **************** Novel Quiz Section ************** */
        chapterQuizHeader.setText("Chapter " + viewModel.getSelectedChapter().getPosition() + " Quiz");
        chapterQuestionsList.setItems(viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId()));
        chapterQuestionsList.setCellFactory(new NovelChapterQuestionListCellFactory());

        viewModel.selectedQuestionIndexProperty().addListener(((observableValue, oldValue, newValue) -> {
            changeSelectedQuestion(newValue.intValue());
            updateBookmarkIcon();
        }));


        viewModel.fiftyFiftyCountProperty().addListener(((observableValue, oldValue, newValue) -> {
            updateFiftyFiftyButton(newValue.intValue());
        }));

        fiftyFiftyButton.setOnAction(event -> {
            List<NovelObjectiveQuestion> chapterQuestions = viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId());
            NovelObjectiveQuestion selectedQuestion = chapterQuestions.get(viewModel.getSelectedQuestionIndex() - 1);

            int enabledButtons = 0;

            for (Button option : options) {
                if (!option.isDisabled()) {
                    enabledButtons++;
                }
            }

            if (enabledButtons > 2) {
                options.stream()
                        .unordered()
                        .filter(button -> !((int) button.getUserData() == selectedQuestion.getQuestionAnswer().getId()) && !button.isDisabled())
                        .limit(enabledButtons - 2)
                        .forEach(button -> button.setDisable(true));

                viewModel.setFiftyFiftyCount(viewModel.getFiftyFiftyCount() - 1);
                fiftyFiftyButton.setDisable(true);
            }

        });

        optionAButton.setOnAction(event -> {
            fiftyFiftyButton.setDisable(false);

            List<NovelObjectiveQuestion> chapterQuestions = viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId());
            NovelObjectiveQuestion selectedQuestion = chapterQuestions.get(viewModel.getSelectedQuestionIndex() - 1);

            if (selectedQuestion.getQuestionAnswer().getId() == (int) optionAButton.getUserData()) {
                dispatchAnswerCorrect();
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

            List<NovelObjectiveQuestion> chapterQuestions = viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId());
            NovelObjectiveQuestion selectedQuestion = chapterQuestions.get(viewModel.getSelectedQuestionIndex() - 1);

            if (selectedQuestion.getQuestionAnswer().getId() == (int) optionBButton.getUserData()) {
                dispatchAnswerCorrect();
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

            List<NovelObjectiveQuestion> chapterQuestions = viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId());
            NovelObjectiveQuestion selectedQuestion = chapterQuestions.get(viewModel.getSelectedQuestionIndex() - 1);

            if (selectedQuestion.getQuestionAnswer().getId() == (int) optionCButton.getUserData()) {
                dispatchAnswerCorrect();
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

            List<NovelObjectiveQuestion> chapterQuestions = viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId());
            NovelObjectiveQuestion selectedQuestion = chapterQuestions.get(viewModel.getSelectedQuestionIndex() - 1);

            if (selectedQuestion.getQuestionAnswer().getId() == (int) optionDButton.getUserData()) {
                dispatchAnswerCorrect();
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

        optionEButton.setOnAction(event -> {
            fiftyFiftyButton.setDisable(false);

            List<NovelObjectiveQuestion> chapterQuestions = viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId());
            NovelObjectiveQuestion selectedQuestion = chapterQuestions.get(viewModel.getSelectedQuestionIndex() - 1);

            if (selectedQuestion.getQuestionAnswer().getId() == (int) optionEButton.getUserData()) {
                dispatchAnswerCorrect();
            } else {
                dispatchAnswerIncorrect();
                optionEButton.setDisable(true);
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

        answerContinueButton.setOnAction(event -> {
            Animations.translateOut(answerPane, 300);
            List<NovelObjectiveQuestion> chapterQuestions = viewModel.getChapterQuestions().get(viewModel.getSelectedChapter().getId());

            if (viewModel.getSelectedQuestionIndex() == chapterQuestions.size()) {
                showResult();
            } else {
                Animations.fadeIn(questionPane, 300);
                viewModel.setSelectedQuestionIndex(viewModel.getSelectedQuestionIndex() + 1);
                fiftyFiftyButton.setDisable(false);
            }

        });

        takeQuizButton.setOnAction(event -> {
            setupQuizView();
            updateBookmarkIcon();
            Animations.translateOut(chaptersListPane, 400);
            takeQuizButton.setDisable(true);
            Animations.fadeIn(questionPane, 300);
            Animations.slideIn(chapterQuizPane, 500f, 0f, 500);

        });

        quitQuizButton.setOnAction(event -> {
            quitDialogDimmer.setVisible(true);
            Animations.translateIn(quitQuizDialog, 300);

        });

        quitDialogQuitButton.setOnAction(event -> {
            quitDialogDimmer.setVisible(false);
            Animations.translateOut(quitQuizDialog, 300);
            Animations.translateIn(chaptersListPane, 400);
            Animations.slideOut(chapterQuizPane, 0f, 500f, 500);
            chapterQuestionsList.setVisible(false);
            chapterQuizQuestionPane.setVisible(true);
            quitQuiz();
        });

        quitDialogCancelButton.setOnAction(event -> {
            quitDialogDimmer.setVisible(false);
            Animations.translateOut(quitQuizDialog, 300);
        });

        tryAgainButton.setOnAction(event -> {
            Animations.fadeIn(questionPane, 300);
            Animations.translateOut(resultPane, 300);
            refreshQuiz();

        });

        resultContinueButton.setOnAction(event -> {
            Animations.translateOut(resultPane, 300);
            Animations.translateIn(chaptersListPane, 400);
            Animations.slideOut(chapterQuizPane, 0f, 500f, 500);
            quitQuiz();
        });

        showAllAnswersLabel.setOnMouseClicked(event -> {
            Animations.translateOut(resultPane, 300);
            Animations.translateIn(chapterQuestionsList, 400);
            chapterQuizQuestionPane.setVisible(false);
//            Animations.slideOut(chapterQuizQuestionPane, 0f, 500f, 500);
            refreshQuiz();
        });

        bookmarkImage.setOnMouseClicked(event -> {
            viewModel.handleBookmarkClicked();
            updateBookmarkIcon();
        });

        reportImage.setOnMouseClicked(event -> {
            System.out.println(TAG + "Novel Question Report Image clicked!");
        });

        speakerImage.setOnMouseClicked(event -> {
            System.out.println(TAG + "Novel Question Speaker Image clicked!");
        });
    }

    private void showActivateDialog() {
        Animations.showDialog(activateNowDialog, exitDialogDimmer);
    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString())));
        prevButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/novel_images/prev_icon.png").toString())));
        nextButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/novel_images/next_icon.png").toString())));

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_bookmark.png").toString()));
        reportImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_report.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_speaker.png").toString()));
        exitQuestionMarkIcon.setImage(new Image(getClass().getResource("/drawable/dialog_question_mark.png").toString()));
        quitQuestionMarkIcon.setImage(new Image(getClass().getResource("/drawable/dialog_question_mark.png").toString()));
        activateNowCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        activateNowPadlockIcon.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/padlock_icon.png").toString()));
        greenTickIcon1.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon2.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon3.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon4.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon5.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));

        chaptersList.setBackground(Background.EMPTY);
        tryAgainButton.setBackground(Background.EMPTY);
        chapterQuestionsList.setBackground(Background.EMPTY);
    }

    private void initializeFont() {
        chapterTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//        chapterContent.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        chapterCount.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        activateHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
    }

    private void initializeGestures() {
        String idleFiftyFiftyStyle = fiftyFiftyButton.getStyle();
        String hoveredFiftyFiftyStyle =
                "-fx-background-color: #73D25E;" +
                        "-fx-background-radius: 300;" +
                        "-fx-border-color: #1B9D01;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 300";

        fiftyFiftyButton.setOnMouseEntered(e -> {
            fiftyFiftyButton.setStyle(hoveredFiftyFiftyStyle);
            fiftyFiftyButton.setTextFill(Color.WHITE);
        });
        fiftyFiftyButton.setOnMouseExited(e -> {
            fiftyFiftyButton.setStyle(idleFiftyFiftyStyle);
            fiftyFiftyButton.setTextFill(Color.web("#1B9D01"));
        });

        options.forEach(button -> {
            button.setOnMouseEntered(e -> button.setStyle(hoveredButtonStyle));
            button.setOnMouseExited(e -> button.setStyle(idleButtonStyle));
        });

    }

    private void setupQuizView() {
        NovelChapter selectedChapter = viewModel.getSelectedChapter();

        List<NovelObjectiveQuestion> questions = viewModel.getChapterQuestions().get(selectedChapter.getId());

        NovelObjectiveQuestion selectedQuestion = questions.get(viewModel.getSelectedQuestionIndex() - 1);
        int selectedQuestionNumber = selectedQuestion.getQuestionNumber();

        questionNumberLabel.setText("Question " + selectedQuestionNumber);
        String questionText = selectedQuestion.getQuestion();
        questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));
        answerLabel.setText(selectedQuestion.getQuestionAnswer().getAnswer());
        explanationLabel.setText(selectedQuestion.getQuestionAnswer().getExplanation());

        optionAButton.setText(selectedQuestion.getOptionA().getText());
        optionAButton.setUserData(0);
        optionBButton.setText(selectedQuestion.getOptionB().getText());
        optionBButton.setUserData(1);
        optionCButton.setText(selectedQuestion.getOptionC().getText());
        optionCButton.setUserData(2);
        optionDButton.setText(selectedQuestion.getOptionD().getText());
        optionDButton.setUserData(3);
        optionEButton.setText(selectedQuestion.getOptionE().getText());
        optionEButton.setUserData(4);

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);
        optionEButton.setDisable(false);

        updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());
    }

    private void changeSelectedQuestion(int questionIndex) {
        NovelChapter selectedChapter = viewModel.getSelectedChapter();

        List<NovelObjectiveQuestion> questions = viewModel.getChapterQuestions().get(selectedChapter.getId());

        NovelObjectiveQuestion selectedQuestion = questions.get(questionIndex - 1);
        int selectedQuestionNumber = selectedQuestion.getQuestionNumber();

        questionNumberLabel.setText("Question " + selectedQuestionNumber);
        String questionText = selectedQuestion.getQuestion();
        questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

        answerLabel.setText(selectedQuestion.getQuestionAnswer().getAnswer());
        explanationLabel.setText(selectedQuestion.getQuestionAnswer().getExplanation());

        optionAButton.setText(selectedQuestion.getOptionA().getText());
        optionAButton.setUserData(0);
        optionBButton.setText(selectedQuestion.getOptionB().getText());
        optionBButton.setUserData(1);
        optionCButton.setText(selectedQuestion.getOptionC().getText());
        optionCButton.setUserData(2);
        optionDButton.setText(selectedQuestion.getOptionD().getText());
        optionDButton.setUserData(3);
        optionEButton.setText(selectedQuestion.getOptionE().getText());
        optionEButton.setUserData(4);

        optionAButton.setDisable(false);
        optionBButton.setDisable(false);
        optionCButton.setDisable(false);
        optionDButton.setDisable(false);
        optionEButton.setDisable(false);

        quitQuizButton.setDisable(false);

        updateFiftyFiftyButton(viewModel.getFiftyFiftyCount());

    }

    private void dispatchAnswerCorrect() {
        Media sound = new Media(getClass().getResource("/sounds/correctAnswer.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setStopTime(Duration.millis(400));
        mediaPlayer.play();

        Animations.translateIn(answerPane, 500);
        Animations.fadeOut(questionPane, 300);
        fiftyFiftyButton.setDisable(true);
        quitQuizButton.setDisable(true);

        viewModel.setCorrectAnswers(viewModel.getCorrectAnswers() + 1);
        viewModel.setTotalGuesses(viewModel.getTotalGuesses() + 1);
    }

    private void dispatchAnswerIncorrect() {
        Media sound = new Media(getClass().getResource("/sounds/wrongAnswer.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setStopTime(Duration.millis(400));
        mediaPlayer.play();

        viewModel.setTotalGuesses(viewModel.getTotalGuesses() + 1);
    }

    private void updateFiftyFiftyButton(int count) {
        fiftyFiftyCount.setText(Integer.toString(count));

        fiftyFiftyButton.setDisable(count < 1);
    }

    private void updateBookmarkIcon() {
        NovelChapter selectedChapter = viewModel.getSelectedChapter();

        List<NovelObjectiveQuestion> questions = viewModel.getChapterQuestions().get(selectedChapter.getId());

        NovelObjectiveQuestion selectedQuestion = questions.get(viewModel.getSelectedQuestionIndex() - 1);

//        ObservableList<NovelObjectiveBookmark> bookmarks = viewModel.getChapterBookmarks().get(selectedChapter.getId());

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_bookmark.png").toString()));

//        bookmarks.forEach(novelObjectiveBookmark -> {
//            if (novelObjectiveBookmark.getQuestionId() == selectedQuestion.getId()) {
//                bookmarkImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_bookmark_filled.png").toString()));
//            }
//        });
    }

    private void showResult() {
        resultHeader.setText("Chapter " + viewModel.getSelectedChapter().getPosition() + " Quiz Result");
        numOfCorrectAnsLabel.setText(String.valueOf((int) viewModel.getCorrectAnswers()));
        numOfGuessesLabel.setText(String.valueOf((int) viewModel.getTotalGuesses()));
        scorePercentageLabel.setText((int) viewModel.getScorePercentage() + "%");
        Animations.translateIn(resultPane, 300);
    }

    private void quitQuiz() {
        viewModel.setSelectedQuestionIndex(1);
        viewModel.setFiftyFiftyCount(5);
        takeQuizButton.setDisable(false);

        viewModel.setTotalGuesses(0);
        viewModel.setCorrectAnswers(0);
    }

    private void refreshQuiz() {
        viewModel.setSelectedQuestionIndex(1);
        viewModel.setFiftyFiftyCount(5);

        viewModel.setTotalGuesses(0);
        viewModel.setCorrectAnswers(0);
    }

    private void renderNovel(NovelChapter chapter) {
        viewModel.getChapterSections()
                .get(chapter.getId())
                .forEach(section -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Map<String,Object> map = mapper.readValue(section.getContent(), Map.class);
                        String content = map.get("text").toString();

                        WebView webView = new WebView();
                        webView.setPrefHeight(600);
                        WebEngine webEngine = webView.getEngine();
                        webEngine.loadContent(content);
                        webEngine.setUserStyleSheetLocation("data:,body { font-size: 18px; line-height: 1.8; }");

                        contentPane.getChildren().clear();
                        contentPane.getChildren().addAll(webView);
                    } catch (Exception e) {
                        System.out.println(TAG + e.getMessage());
                    }
                });
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        novelContentVBox.setVisible(true);
        novelChaptersVBox.setVisible(true);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        novelContentVBox.setVisible(false);
        novelChaptersVBox.setVisible(false);
    }

    private InitialData getInitialData() {
        return  (InitialData) ViewSwitcher.retrieveData();
    }

    public static class InitialData {
        private NovelModel novelModel;
        private ObservableList<NovelChapter> chapters;
        private NovelChapter selectedChapter;

        public InitialData(NovelModel novelModel, ObservableList<NovelChapter> novelChapters, NovelChapter selectedChapter) {
            this.novelModel = novelModel;
            this.chapters = novelChapters;
            this.selectedChapter = selectedChapter;
        }

        public NovelModel getNovelModel() {
            return novelModel;
        }

        public ObservableList<NovelChapter> getChapters() {
            return chapters;
        }

        public NovelChapter getSelectedChapter() {
            return selectedChapter;
        }

    }

}
