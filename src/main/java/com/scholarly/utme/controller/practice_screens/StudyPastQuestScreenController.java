package com.scholarly.utme.controller.practice_screens;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.*;
import com.scholarly.utme.data.model.newDb.ObjectiveQuestionDescription;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.TextToSpeech;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.practice_screens.StudyPastScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.SceneLifecycle;
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
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@FxmlPath("/layouts/practice_screens/StudyPastQuestionsScreen.fxml")
public class StudyPastQuestScreenController implements FxmlView<StudyPastScreenVM>, Initializable, SceneLifecycle {
    private static final String TAG = "StudyPastQuestScreenController: ";

    @InjectViewModel
    private StudyPastScreenVM viewModel;

    @FXML
    private WebView questionWebView, explanationWebView, questionWithImageWebView;
    @FXML
    private HBox answerHeaderHBox, rightPane, quesDescriptionHBox, questionWithImageHBox;
    @FXML
    private VBox centerVBox, rightVBox, questionDescriptionDialog, explanationVBox;
    @FXML
    private TilePane tilePane;
    @FXML
    private ScrollPane tileScrollPane, explanationScrollPane;
    @FXML
    private StackPane answerPane, explanationPane, questionStackPane;
    @FXML
    private ListView<PQSubject> subjectList;
    @FXML
    private Label questionOverviewLabel, optionA, optionB, optionC, optionD, explanationLabel, explanationTitle, correctAnswerTitle, correctAnswerLabel, questionDescriptionHeader, readQuestionDesc, questionDescriptionText;
    @FXML
    private Button prevButton, nextButton, exitButton, hideAnswerButton, viewImageButton;
    @FXML
    private ToggleButton showAnswerButton, showExplanationButton;
    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, quesDescriptionCloseIcon, questionImage;
    @FXML
    private Pane dialogDimmer;
    @FXML
    private DialogPane exitDialogPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel.processInitialData(getInitialData());

        initializeViews();
        initializeFont();

        subjectList.setCellFactory(new PracticeSubjectListCellFactory());
        subjectList.setItems(viewModel.getSubjects());
        subjectList.getSelectionModel().selectFirst();
        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super PQSubject>) c -> {
            if (c.getList().size() == 1) {
                PQSubject subject = c.getList().get(0);
                viewModel.setSelectedSubject(subject);
            } else {
                viewModel.setSelectedSubject(null);
            }
        });

        viewModel.setSelectedSubject(subjectList.getSelectionModel().getSelectedItem());
        setupQuestionView(viewModel.getSelectedSubject());
        setupTilePane(viewModel.getSelectedSubject());

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupQuestionView(newValue);
                setupTilePane(newValue);
                updateBookmarkIcon();
            }
        });

        viewModel.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
            subjectQuestionsState.selectedQuestionProperty().addListener((observable, oldValue, newValue) -> {
                if (viewModel.getSelectedSubject().getShortTitle().equalsIgnoreCase(s)) {
                    changeSelectedTile(oldValue.intValue(), newValue.intValue());
                    changeSelectedQuestion(newValue.intValue());
                    updateBookmarkIcon();
                }
            });
        });


        // Integer value to track subjectList subject selection
        AtomicInteger subjectListSelectionAtomicIndex = new AtomicInteger(0);

        subjectList.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<? super Integer>) observable -> {
            if (observable.getList().size() == 1) {
                subjectListSelectionAtomicIndex.set(observable.getList().get(0));
            }
        });

        prevButton.setOnAction(event -> {
            nextButton.setDisable(false);

            int selectedQuestionIndex = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            if (subjectList.getSelectionModel().getSelectedIndex() == 0) {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestionIndex - 1);

                if (selectedQuestionIndex == 2) {
                    prevButton.setDisable(true);
                }
            } else {
                if (selectedQuestionIndex == 1) {
                    subjectList.getSelectionModel().select(subjectListSelectionAtomicIndex.decrementAndGet());
                } else {
                    viewModel.getSubjectsQuestions()
                            .get(viewModel.getSelectedSubject().getShortTitle())
                            .setSelectedQuestion(selectedQuestionIndex - 1);
                }
            }

        });

        nextButton.setOnAction(event -> {
            prevButton.setDisable(false);
            StudyPastScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
            List<StudyPastScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

            int selectedQuestionIndex = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            if (selectedQuestionIndex == questions.size()) {
                subjectList.getSelectionModel().select(subjectListSelectionAtomicIndex.incrementAndGet());
                prevButton.setDisable(false);

            } else {
                viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .setSelectedQuestion(selectedQuestionIndex + 1);

                int newSelectedQuestionIndex = viewModel.getSubjectsQuestions()
                        .get(viewModel.getSelectedSubject().getShortTitle())
                        .getSelectedQuestion();

                if (newSelectedQuestionIndex == questions.size() && subjectList.getItems().size()-1 == subjectListSelectionAtomicIndex.get())
                    nextButton.setDisable(true);
            }
        });

        showAnswerButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();
            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {

                questionsState.getQuestions().get(selectedQuestion - 1).setShowAnswer(true);

            } else {

                questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(true);
            }
            updateExplanationView();

        });

        showExplanationButton.setOnAction(event -> {

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(true);

            updateExplanationView();
        });

        hideAnswerButton.setOnAction(event -> {

            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            questionsState.getQuestions().get(selectedQuestion - 1).setShowExplanation(false);

            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationScrollPane.setVisible(false);
            explanationLabel.setVisible(false);
            explanationWebView.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);
            hideAnswerButton.setVisible(false);

        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle());

            if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
                ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questionsState.getQuestions().get(selectedQuestion - 1).getQuestion();
                String questionText = currentQuestion.getQuestion();
                TextToSpeech.play(questionText);

            } else {
                TheoryQuestion currentQuestion = (TheoryQuestion) questionsState.getQuestions().get(selectedQuestion - 1).getQuestion();
                String questionText = currentQuestion.getQuestion();
                TextToSpeech.play(questionText);
            }

        });

//        bookmarkImage.setOnMouseClicked(event -> {
//            viewModel.handleBookmarkClicked();
//            updateBookmarkIcon();
//        });


//        questionImageCloseIcon.setOnMouseClicked(event -> {
//            questionImageBox.setVisible(false);
//        });

        exitButton.setOnAction(event -> {
            showExitDialog();
        });

        readQuestionDesc.setOnMouseClicked(mouseEvent -> {
            Animations.showDialog(questionDescriptionDialog, dialogDimmer);
        });
        readQuestionDesc.setOnMouseEntered(event -> readQuestionDesc.setUnderline(true));
        readQuestionDesc.setOnMouseExited(event -> readQuestionDesc.setUnderline(false));

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

    private void setupQuestionView(PQSubject selectedSubject) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestionNumber = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

//        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
//        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        prevButton.setDisable(subjectList.getSelectionModel().getSelectedIndex() == 0 && selectedQuestionNumber == 1);

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(selectedQuestionNumber - 1).getQuestion();

            List<ObjectiveQuestionDescription> questionDescriptionInList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

            quesDescriptionHBox.getChildren().removeAll(questionDescriptionHeader, readQuestionDesc);
            if (!questionDescriptionInList.isEmpty()) {
                quesDescriptionHBox.getChildren().addAll(questionDescriptionHeader, readQuestionDesc);
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            String questionText = currentQuestion.getQuestion();

            questionStackPane.getChildren().removeAll(questionWebView, questionWithImageHBox);

            if (questionText.contains("<img")) {
                questionStackPane.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText);

            } else {
                questionStackPane.getChildren().add(questionWebView);
                questionWebView.getEngine().loadContent(questionText);
            }

            optionA.setText(" (A) " + currentQuestion.getOptionA().getText());
            optionB.setText(" (B) " + currentQuestion.getOptionB().getText());
            optionC.setText(" (C) " + currentQuestion.getOptionC().getText());
            optionD.setText(" (D) " + currentQuestion.getOptionD().getText());

            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());
            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationVBox.getChildren().clear();
            if (explanationText.contains("<img")) {
                explanationVBox.getChildren().addAll(explanationTitle, explanationWebView);
                String questionWithImageText = parseExplanationWithImageView(explanationText);
                explanationWebView.getEngine().loadContent(questionWithImageText);
            } else {
                explanationVBox.getChildren().addAll(explanationTitle, explanationScrollPane);
                explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));
            }


        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(selectedQuestionNumber - 1).getQuestion();
            questionOverviewLabel.setText("Question " + selectedQuestionNumber + " of " + questions.size());

            String questionText = currentQuestion.getQuestion();
//            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

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
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

        prevButton.setDisable(subjectList.getSelectionModel().getSelectedIndex() == 0 && newValue == 1);

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(newValue - 1).getQuestion();
            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            List<ObjectiveQuestionDescription> questionDescriptionInList = viewModel.getObjectiveQuestionDescriptions().stream().filter(questionDescription ->
                    questionDescription.getId() == currentQuestion.getQuestionDescriptionId()).toList();

            quesDescriptionHBox.getChildren().removeAll(questionDescriptionHeader, readQuestionDesc);
            if (!questionDescriptionInList.isEmpty()) {
                quesDescriptionHBox.getChildren().addAll(questionDescriptionHeader, readQuestionDesc);
                readQuestionDesc.setVisible(true);
                questionDescriptionHeader.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", " "));
                questionDescriptionText.setText(questionDescriptionInList.get(0).getDescription().replaceAll("<br>", System.lineSeparator()));
            }

            String questionText = currentQuestion.getQuestion();

            questionStackPane.getChildren().removeAll(questionWebView, questionWithImageHBox);

            if (questionText.contains("<img")) {
                questionStackPane.getChildren().add(questionWithImageHBox);
                showQuestionWithImage(questionText);
                System.out.println(TAG + "questionWithImageHBox.heightProperty() -> " + questionWithImageHBox.heightProperty().get());
                questionWebView.setMinHeight(questionWithImageHBox.heightProperty().get());
            } else {
                questionStackPane.getChildren().add(questionWebView);
                questionWebView.getEngine().loadContent(questionText);
            }

            optionA.setText(" (A) " + currentQuestion.getOptionA().getText());
            optionB.setText(" (B) " + currentQuestion.getOptionB().getText());
            optionC.setText(" (C) " + currentQuestion.getOptionC().getText());
            optionD.setText(" (D) " + currentQuestion.getOptionD().getText());

            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());

            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationVBox.getChildren().clear();
            if (explanationText.contains("<img")) {
                explanationVBox.getChildren().addAll(explanationTitle, explanationWebView);
                String questionWithImageText = parseExplanationWithImageView(explanationText);
                explanationWebView.getEngine().loadContent(questionWithImageText);
            } else {
                explanationVBox.getChildren().addAll(explanationTitle, explanationScrollPane);
                explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));
            }

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(newValue - 1).getQuestion();

            questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

            String questionText = currentQuestion.getQuestion();
//            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            correctAnswerLabel.setText(currentQuestion.getQuestionAnswer().getAnswer());
            String explanationText = currentQuestion.getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));

        }

        updateExplanationView();

    }

    private void showQuestionWithImage(String questionWithImageText) {
        int startIndexOfImg = questionWithImageText.indexOf("<img");
        int endIndexOfImg = questionWithImageText.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = questionWithImageText.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = questionWithImageText.indexOf("' width", startIndexOfImg);

        String imagePath = questionWithImageText.substring(startIndexOfImgPath, endIndexOfImgPath);
        questionImage.setImage(new Image(getClass().getResource(imagePath).toString()));

        String imageQuestion = questionWithImageText.substring(questionWithImageText.lastIndexOf(">")+1);
        questionWithImageWebView.getEngine().loadContent(imageQuestion);

        StringBuilder builder = new StringBuilder(questionWithImageText);

        URL url = getClass().getResource(imagePath);
        String img = "<img src='"+url+"' width='100%'>";

        builder.replace(startIndexOfImg, (endIndexOfImg + 7), img);

        questionWithImageText = builder.toString();
    }

    private String parseExplanationWithImageView(String explanationWithImageText) {
        int startIndexOfImg = explanationWithImageText.indexOf("<img");
        int endIndexOfImg = explanationWithImageText.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = explanationWithImageText.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = explanationWithImageText.indexOf("' width", startIndexOfImg);

        String imagePath = explanationWithImageText.substring(startIndexOfImgPath, endIndexOfImgPath);

        URL url = getClass().getResource(imagePath);
        String img = "<img src='"+url+"' width='100%'>";

        StringBuilder builder = new StringBuilder(explanationWithImageText);
        builder.replace(startIndexOfImg, (endIndexOfImg + 7), img);

        explanationWithImageText = builder.toString();
        System.out.println(TAG + "Final ExplanationText -> " + explanationWithImageText);
        return explanationWithImageText;
    }

    private void updateBookmarkIcon() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        if (viewModel.getQuestionType() == SubjectListItemVM.Type.OBJECTIVE) {
            ObjectiveQuestion selectedQuestion = (ObjectiveQuestion) questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion();

            List<ObjectiveBookmark> bookmarks = viewModel.getObjectiveBookmarks().get(viewModel.getSelectedSubject().getId());

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
//                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });

        } else if (viewModel.getQuestionType() == SubjectListItemVM.Type.THEORY) {
            TheoryQuestion selectedQuestion = (TheoryQuestion) questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion();

            List<TheoryBookmark> bookmarks = viewModel.getTheoryBookmarks().get(viewModel.getSelectedSubject().getId());

            bookmarks.forEach(bookmark -> {
                if (bookmark.getQuestionId() == selectedQuestion.getId()) {
//                    bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
                    System.out.println(TAG + "Bookmark image changed for question with question_id -> " + bookmark.getQuestionId() + " and subject_id -> " + bookmark.getSubjectId());
                }
            });
        }

    }

    private void  updateExplanationView() {
        SubjectQuestionsState questionsState = viewModel.getSubjectsQuestions()
                .get(viewModel.getSelectedSubject().getShortTitle());

        QuestionState questionState = questionsState.getQuestions().get(questionsState.getSelectedQuestion() - 1);

        if (!questionState.isShowExplanation() && !questionState.isShowAnswer()) {
//            System.out.println(TAG + "showing default explanation view");
            showAnswerButton.setDisable(false);
            showExplanationButton.setDisable(false);
            explanationTitle.setVisible(false);
            explanationScrollPane.setVisible(false);
            explanationLabel.setVisible(false);
            explanationWebView.setVisible(false);
            correctAnswerLabel.setVisible(false);
            correctAnswerTitle.setVisible(false);

            hideAnswerButton.setVisible(false);

        } else if (questionState.isShowExplanation()) {

//            System.out.println(TAG + "showing explanation and answer view");
            explanationTitle.setVisible(true);
            explanationScrollPane.setVisible(true);
            explanationLabel.setVisible(true);
            explanationWebView.setVisible(true);
            showExplanationButton.setDisable(true);

            correctAnswerLabel.setVisible(true);
            correctAnswerTitle.setVisible(true);
            showAnswerButton.setDisable(true);

            hideAnswerButton.setText("Hide Explanation");
            hideAnswerButton.setVisible(true);


        } else if (questionState.isShowAnswer()) {

//            System.out.println(TAG + "showing show answer view alone");
            correctAnswerLabel.setVisible(true);
            correctAnswerTitle.setVisible(true);
            showAnswerButton.setDisable(true);
            showExplanationButton.setDisable(false);

            hideAnswerButton.setText("Hide Answer");
            hideAnswerButton.setVisible(true);
        }
    }

    private void setupTilePane(PQSubject selectedSubject) {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(selectedSubject.getShortTitle());
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
