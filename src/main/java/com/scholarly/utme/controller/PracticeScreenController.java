package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Bookmark;
//import com.gtranslate.Audio;
//import com.gtranslate.Language;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import com.scholarly.utme.viewmodels.PracticeScreenVM.QuestionState;
import com.scholarly.utme.viewmodels.PracticeScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, reportDialogCloseIcon;

    private Stage calculatorStage = new Stage();

    @FXML
    private Pane reportDialogDimmer;

    @FXML
    private VBox reportDialog;

    @FXML
    private WebView webView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewModel.processInitialData(getInitialData());


        try {
            bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
            calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
            reportDialogCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
            speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
            flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupQuestionView();
                setupTilePane();
            }
        });

        viewModel.getSubjectBookmarks().forEach((s, bookmarks) -> {
            bookmarks.addListener((ListChangeListener<? super Bookmark>) change -> {

            });
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
        viewModel.getSubjectBookmarks().forEach((s, bookmarks) -> {
            bookmarks.addListener((ListChangeListener<? super Bookmark>) observable -> {
                if (viewModel.getSelectedSubject().getTableName().equalsIgnoreCase(s)) {
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

        bookmarkImage.setOnMouseClicked(mouseEvent -> {
            viewModel.handleBookmarkClicked();
        });

        reportDialogCloseIcon.setOnMouseClicked(mouseEvent -> {
            hideReportDialog();
        });

        flagImage.setOnMouseClicked(mouseEvent -> {
            showReportDialog();
        });

        speakerImage.setOnMouseClicked(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getSelectedQuestion();

            QuestionState questionState = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getTableName())
                    .getQuestions()
                    .get(selectedQuestion - 1);

            textToSpeech(questionState.getQuestion().getQuestion());
        });

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

    private void updateBookmarkIcon() {
        SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        List<Bookmark> bookmarks = viewModel.getSubjectBookmarks().get(viewModel.getSelectedSubject().getTableName());

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));

        bookmarks.forEach(bookmark -> {
            if (bookmark.getQuestionId() == questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion().getId()) {
                bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
            }
        });
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
        List<Bookmark> bookmarks = viewModel.getSubjectBookmarks().get(viewModel.getSelectedSubject().getTableName());

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        bookmarks.forEach(bookmark -> {
            if (bookmark.getQuestionId() == questions.get(newValue - 1).getQuestion().getId()) {
                bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
            }
        });

//        boolean questionIsBookmarked = false;
//        for (int i = 0; i < bookmarks.size(); i++) {
//            if (bookmarks.get(i).getQuestionId() == questions.get(newValue - 1).getQuestion().getId()) {
//                questionIsBookmarked = true;
//            }
//        }
//
//        if (questionIsBookmarked) {
//            bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
//        } else {
//            bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
//        }

        questionOverviewLabel.setText("Question " + newValue + " of " + questions.size());

        questionLabel.setText(questions.get(newValue - 1).getQuestion().getQuestion());
        webView.getEngine().loadContent(questions.get(newValue - 1).getQuestion().getQuestion());

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

        List<Bookmark> bookmarks = viewModel.getSubjectBookmarks().get(viewModel.getSelectedSubject().getTableName());

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        bookmarks.forEach(bookmark -> {
            if (bookmark.getQuestionId() == questions.get(subjectQuestionsState.getSelectedQuestion() - 1).getQuestion().getId()) {
                bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark_filled.png").toString()));
            }
        });

        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + selectedQuestion + " of " + questions.size());

        questionLabel.setText(questions.get(selectedQuestion - 1).getQuestion().getQuestion());
        webView.getEngine().loadContent(questions.get(selectedQuestion - 1).getQuestion().getQuestion());

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

    private void textToSpeech(String text) {
//        Audio audio = Audio.getInstance();
//        InputStream sound = null;
//        try {
//            sound = audio.getAudio(text, Language.ENGLISH);
//        } catch (IOException ex) {
//            System.out.println("error converting text to audio");
//        }
//        try {
//            audio.play(sound);
//        } catch (Exception ex) {
//            System.out.println("error converting text to audio");
//        }

        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice("kevin16");

        voice.allocate();

        voice.speak(text);
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
