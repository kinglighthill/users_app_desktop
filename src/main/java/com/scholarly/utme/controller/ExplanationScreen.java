package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.ExplanationScreenVM;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;


@FxmlPath("/layouts/ExplanationScreen.fxml")
public class ExplanationScreen implements FxmlView<ExplanationScreenVM>, Initializable {

    @InjectViewModel
    private ExplanationScreenVM viewModel;

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane tileScrollPane;

    @FXML
    private StackPane explanationPane, explanationVideo;

    @FXML
    private ListView<Subject> subjectList;

    @FXML
    private Label questionOverviewLabel, questionLabel, optionA, optionB, optionC, optionD, noOptionSelected, explanationLabel;

    @FXML
    private Button prevButton, nextButton, exitButton;

    @FXML
    private RadioButton optionAButton, optionBButton, optionCButton, optionDButton;

    @FXML
    private ToggleButton textExplanation, videoExplanation;

    @FXML
    private ImageView bookmarkImage, flagImage, speakerImage, calculatorImage, optionAIcon, optionBIcon, optionCIcon, optionDIcon;

    private Image correctImage;

    private Image incorrectImage;

    private String explanationVideoUrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewModel.processInitialData(getInitialData());

        initializeViews();

        initializeFont();

        setupVideoPlayer();

        viewModel.selectedSubjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupQuestionView();
                setupTilePane();
            }
        });

        ObservableList<Subject> items = FXCollections.observableArrayList();

        Subject all = new Subject();
        all.setSubjectName("All");
        items.add(all);
        items.addAll(viewModel.getSubjects());

        subjectList.setCellFactory(new PracticeSubjectListCellFactory());
        subjectList.setItems(items);
        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Subject>) c -> {
            if (c.getList().size() == 1) {
                Subject subject = c.getList().get(0);
                viewModel.setSelectedSubject(subject);
            } else {
                viewModel.setSelectedSubject(null);
            }
        });

        subjectList.getSelectionModel().select(1);

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

        ToggleGroup explanationGroup = new ToggleGroup();
        explanationGroup.getToggles().addAll(textExplanation, videoExplanation);
        explanationGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == textExplanation) {
                textExplanation.setStyle("-fx-background-color: #12AF20; -fx-border-color: #12AF20;");
                textExplanation.setTextFill(Paint.valueOf("#FFFFFF"));
                videoExplanation.setStyle("-fx-border-color: #12AF20;");
                videoExplanation.setTextFill(Paint.valueOf("#12AF20"));

                explanationPane.getChildren().remove(explanationVideo);
                if (!explanationPane.getChildren().contains(explanationLabel)) {
                    explanationPane.getChildren().add(explanationLabel);
                }

            }else if (newValue == videoExplanation) {
                explanationVideo.setVisible(true);
                videoExplanation.setStyle("-fx-background-color: #12AF20; -fx-border-color: #12AF20;");
                videoExplanation.setTextFill(Paint.valueOf("#FFFFFF"));
                textExplanation.setStyle("-fx-border-color: #12AF20;");
                textExplanation.setTextFill(Paint.valueOf("#12AF20"));

                explanationPane.getChildren().remove(explanationLabel);
                if (!explanationPane.getChildren().contains(explanationVideo)) {
                    explanationPane.getChildren().add(explanationVideo);
                }

            }
        }));

        exitButton.setOnAction(event -> {
            ViewSwitcher.passData("practicePanel");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

    }

    private void initializeViews() {
        subjectList.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
        prevButton.setBackground(Background.EMPTY);
        tileScrollPane.setBackground(Background.EMPTY);
        textExplanation.setBackground(Background.EMPTY);
        videoExplanation.setBackground(Background.EMPTY);

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
        calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
        flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));

        correctImage = new Image(getClass().getResource("/drawable/practice_screen_images/correct_icon.png").toString());
        incorrectImage = new Image(getClass().getResource("/drawable/practice_screen_images/incorrect_icon.png").toString());

        ImageView prevImage = new ImageView(new Image(getClass().getResource("/drawable/practice_screen_images/prev_btn_icon.png").toString()));
        prevButton.setGraphicTextGap(15);
        prevButton.setGraphic(prevImage);
        ImageView nextImage = new ImageView(getClass().getResource("/drawable/practice_screen_images/next_btn_icon.png").toString());
        nextButton.setContentDisplay(ContentDisplay.RIGHT);
        nextButton.setGraphicTextGap(15);
        nextButton.setGraphic(nextImage);
    }

    private void initializeFont() {
        exitButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }

    private void setupVideoPlayer() {
        explanationVideoUrl = getClass().getResource("/assets/coding.mp4").toExternalForm();

        Media media = new Media(explanationVideoUrl);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        media.setOnError(() -> {
            System.out.println("Media error -> " + media.getError());
        });
        mediaPlayer.setOnError(() -> {
            System.out.println("MediaPlayer error -> " + mediaPlayer.getError());
        });

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(280);
        mediaView.setFitHeight(300);
        mediaView.setSmooth(true);
        mediaView.setOnError(event -> {
            System.out.println("MediaView error -> " + event.getMediaError().toString());
        });

        Button playButton = new Button();
        ImageView playIcon = new ImageView(new Image(getClass().getResource("/drawable/video_type_play_icon.png").toString()));
        playIcon.setFitHeight(45);
        playIcon.setFitWidth(45);
        ImageView pauseIcon = new ImageView(new Image(getClass().getResource("/drawable/video_type_pause_icon_1x.png").toString()));
        pauseIcon.setFitHeight(45);
        pauseIcon.setFitWidth(45);
        playButton.setGraphic(playIcon);
        playButton.setBackground(Background.EMPTY);
        playButton.setOnAction(event -> {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED) {
                mediaPlayer.play();
                playButton.setGraphic(pauseIcon);
            } else {
                mediaPlayer.pause();
                playButton.setGraphic(playIcon);
            }
        });

        StackPane.setAlignment(playButton, Pos.CENTER);

        explanationVideo.getChildren().addAll(mediaView, playButton);

    }

    public void onCalculatorClicked(MouseEvent mouseEvent) {
        // TODO
    }

    /**
     * Initializes question layout
     */
    private void setupQuestionView() {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();
        PracticeScreenVM.QuestionState question = questions.get(subjectQuestionsState.getSelectedQuestion() - 1);

        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + subjectQuestionsState.getSelectedQuestion() + " of " + questions.size());

        questionLabel.setText(question.getObjectiveQuestion().getQuestion());

        optionA.setText(" (A) " + question.getObjectiveQuestion().getOptionA());
        optionB.setText(" (B) " + question.getObjectiveQuestion().getOptionB());
        optionC.setText(" (C) " + question.getObjectiveQuestion().getOptionC());
        optionD.setText(" (D) " + question.getObjectiveQuestion().getOptionD());

        optionA.setTextFill(Color.BLACK);
        optionB.setTextFill(Color.BLACK);
        optionC.setTextFill(Color.BLACK);
        optionD.setTextFill(Color.BLACK);

        String selectedOption = question.getSelectedOption();
        String questionAnswer = question.getObjectiveQuestion().getOptionAnswer();

        if (selectedOption != null) {
            if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionA())) {
                optionAButton.setSelected(true);
            } else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionB())) {
                optionBButton.setSelected(true);
            } else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionC())) {
                optionCButton.setSelected(true);
            } else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionD())) {
                optionDButton.setSelected(true);
            }

            if (selectedOption.equalsIgnoreCase(questionAnswer)) {
                if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);
                }
                else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);
                }
                else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);
                }
                else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);
                }
            } else {
                if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionA())) {
                    optionA.setTextFill(Color.RED);
                    optionAIcon.setImage(incorrectImage);
                }
                else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionB())) {
                    optionB.setTextFill(Color.RED);
                    optionBIcon.setImage(incorrectImage);
                }
                else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionC())) {
                    optionC.setTextFill(Color.RED);
                    optionCIcon.setImage(incorrectImage);
                }
                else if (selectedOption.equalsIgnoreCase(question.getObjectiveQuestion().getOptionD())) {
                    optionD.setTextFill(Color.RED);
                    optionDIcon.setImage(incorrectImage);
                }


                if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);
                }
                else if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);
                }
                else if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);
                }
                else if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);
                }
            }
            noOptionSelected.setVisible(false);
        }
        else {
            if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionA())) {
                optionA.setTextFill(Color.GREEN);
                optionAIcon.setImage(correctImage);
            }
            else if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionB())) {
                optionB.setTextFill(Color.GREEN);
                optionBIcon.setImage(correctImage);
            }
            else if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionC())) {
                optionC.setTextFill(Color.GREEN);
                optionCIcon.setImage(correctImage);
            }
            else if (questionAnswer.equalsIgnoreCase(question.getObjectiveQuestion().getOptionD())) {
                optionD.setTextFill(Color.GREEN);
                optionDIcon.setImage(correctImage);
            }

            noOptionSelected.setVisible(true);
        }

        explanationLabel.setText(question.getObjectiveQuestion().getAnswerExplanation());
    }

    /**
     * Changes selected question of the explanation screen and re-renders views
     * @param questionNumber number of the selected question
     */
    private void changeSelectedQuestion(int questionNumber) {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + questionNumber + " of " + questions.size());

        questionLabel.setText(questions.get(questionNumber - 1).getObjectiveQuestion().getQuestion());

        optionA.setText(" (A) " + questions.get(questionNumber - 1).getObjectiveQuestion().getOptionA());
        optionB.setText(" (B) " + questions.get(questionNumber - 1).getObjectiveQuestion().getOptionB());
        optionC.setText(" (C) " + questions.get(questionNumber - 1).getObjectiveQuestion().getOptionC());
        optionD.setText(" (D) " + questions.get(questionNumber - 1).getObjectiveQuestion().getOptionD());

        optionA.setTextFill(Color.BLACK);
        optionB.setTextFill(Color.BLACK);
        optionC.setTextFill(Color.BLACK);
        optionD.setTextFill(Color.BLACK);

        optionAIcon.setImage(null);
        optionBIcon.setImage(null);
        optionCIcon.setImage(null);
        optionDIcon.setImage(null);

        optionAButton.setSelected(false);
        optionBButton.setSelected(false);
        optionCButton.setSelected(false);
        optionDButton.setSelected(false);

        String selectedOption = questions.get(questionNumber - 1).getSelectedOption();
        String questionAnswer = questions.get(questionNumber - 1).getObjectiveQuestion().getOptionAnswer();

        if (selectedOption != null) {

            if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionA())) {
                optionAButton.setSelected(true);
            } else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionB())) {
                optionBButton.setSelected(true);
            } else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionC())) {
                optionCButton.setSelected(true);
            } else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionD())) {
                optionDButton.setSelected(true);
            }

            if (selectedOption.equalsIgnoreCase(questionAnswer)) {
                if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);
                }
            } else {
                if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionA())) {
                    optionA.setTextFill(Color.RED);
                    optionAIcon.setImage(incorrectImage);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionB())) {
                    optionB.setTextFill(Color.RED);
                    optionBIcon.setImage(incorrectImage);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionC())) {
                    optionC.setTextFill(Color.RED);
                    optionCIcon.setImage(incorrectImage);
                }
                else if (selectedOption.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionD())) {
                    optionD.setTextFill(Color.RED);
                    optionDIcon.setImage(incorrectImage);
                }


                if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionA())) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionB())) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionC())) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);
                }
                else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionD())) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);
                }
            }
            noOptionSelected.setVisible(false);
        }
        else {
            if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionA())) {
                optionA.setTextFill(Color.GREEN);
                optionAIcon.setImage(correctImage);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionB())) {
                optionB.setTextFill(Color.GREEN);
                optionBIcon.setImage(correctImage);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionC())) {
                optionC.setTextFill(Color.GREEN);
                optionCIcon.setImage(correctImage);
            }
            else if (questionAnswer.equalsIgnoreCase(questions.get(selectedQuestion - 1).getObjectiveQuestion().getOptionD())) {
                optionD.setTextFill(Color.GREEN);
                optionDIcon.setImage(correctImage);
            }

            noOptionSelected.setVisible(true);
        }

        explanationLabel.setText(questions.get(questionNumber - 1).getObjectiveQuestion().getAnswerExplanation());
    }

    /**
     * Initializes question tiles at bottom of the screen
     */
    private void setupTilePane() {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getTableName());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(35, 35);
            r.setFill(Color.web("#FFFFFF"));
            r.setStroke(Paint.valueOf("#12AF20"));
            r.setStrokeType(StrokeType.OUTSIDE);

            Label l = new Label(Integer.toString(i));

            if (questions.get(i - 1).getObjectiveQuestion().getOptionAnswer().equalsIgnoreCase(questions.get(i-1).getSelectedOption())) {
                r.setFill(Color.GREEN);
                r.setStrokeWidth(1);
                l.setTextFill(Paint.valueOf("#FFFFFF"));
            } else if (questions.get(i-1).getSelectedOption() != null) {
                r.setStroke(null);
                r.setFill(Color.web("#FA0000", 0.7));
            } else {
                r.setStroke(Paint.valueOf("#12AF20"));
                r.setStrokeWidth(1.5);
            }


            if (subjectQuestionsState.getSelectedQuestion() == i) {
                r.setStroke(Paint.valueOf("#FCB029"));
                r.setStrokeWidth(2);
            }

            StackPane s = new StackPane(r, l);
            tilePane.getChildren().add(s);

            int finalI = i;
            s.setOnMouseClicked(event -> {
                subjectQuestionsState.setSelectedQuestion(finalI);
            });
        }
    }

    /**
     * Changes selected question tile at bottom of the screen
     * @param oldSelectedQuestion old selected question
     * @param newSelectedQuestion new question selected
     */
    private void changeSelectedTile(int oldSelectedQuestion, int newSelectedQuestion) {

        StackPane selectedQuestionPane = (StackPane) tilePane.getChildren().get(newSelectedQuestion - 1);
        StackPane oldQuestionPane = (StackPane) tilePane.getChildren().get(oldSelectedQuestion - 1);


        Rectangle selectedQuestionRect = (Rectangle) selectedQuestionPane.getChildren().get(0);
        Rectangle oldQuestionRect = (Rectangle) oldQuestionPane.getChildren().get(0);

        Label selectedQuestionText = (Label) selectedQuestionPane.getChildren().get(1);
        Label oldQuestionText = (Label) oldQuestionPane.getChildren().get(1);


//        oldQuestionText.setTextFill(Color.BLACK);
        oldQuestionRect.setStrokeWidth(1);
        selectedQuestionRect.setStroke(Paint.valueOf("#FCB029"));
        selectedQuestionRect.setStrokeWidth(2);
//        selectedQuestionText.setTextFill(Color.WHITE);

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
