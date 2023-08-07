package com.scholarly.utme.controller.practice_screens;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.viewmodels.practice_screens.ExplanationScreenVM;
import com.scholarly.utme.viewmodels.practice_screens.PracticeScreenVM;
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
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static com.scholarly.utme.ui.utils.Screen.PRACTICE_SCREEN;
import static com.scholarly.utme.viewmodels.SubjectListItemVM.*;


@FxmlPath("/layouts/practice_screens/ExplanationScreen.fxml")
public class ExplanationScreenController implements FxmlView<ExplanationScreenVM>, Initializable {
    private static final String TAG = "ExplanationScreenController: ";

    @InjectViewModel
    private ExplanationScreenVM viewModel;

    @FXML
    private TilePane tilePane;
    @FXML
    private ScrollPane tileScrollPane;
    @FXML
    private StackPane explanationPane, explanationVideo;
    @FXML
    private AnchorPane explanationAnchor;
    @FXML
    private Pane dialogDimmer;
    @FXML
    private VBox centerVBox;
    @FXML
    private HBox toggleBox;
    @FXML
    private ListView<PQSubject> subjectList;
    @FXML
    private Panel optionAPanel, optionBPanel, optionCPanel, optionDPanel;
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

    private Image correctImage, incorrectImage;


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

        ObservableList<PQSubject> items = FXCollections.observableArrayList();

        PQSubject all = new PQSubject();
        all.setTitle("All");
        items.add(all);
        items.addAll(viewModel.getSubjects());

        subjectList.setCellFactory(new PracticeSubjectListCellFactory());
        subjectList.setItems(items);
        subjectList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super PQSubject>) c -> {
            if (c.getList().size() == 1) {
                PQSubject subject = c.getList().get(0);
                viewModel.setSelectedSubject(subject);
            } else {
                viewModel.setSelectedSubject(null);
            }
        });

        subjectList.getSelectionModel().select(1);

        viewModel.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
            subjectQuestionsState.selectedQuestionProperty().addListener((observable, oldValue, newValue) -> {
                if (viewModel.getSelectedSubject().getShortTitle().equalsIgnoreCase(s)) {
                    changeSelectedTile(oldValue.intValue(), newValue.intValue());
                    changeSelectedQuestion(newValue.intValue());
                }
            });
        });

        prevButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .setSelectedQuestion(selectedQuestion - 1);
        });

        nextButton.setOnAction(event -> {
            int selectedQuestion = viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .getSelectedQuestion();

            viewModel.getSubjectsQuestions()
                    .get(viewModel.getSelectedSubject().getShortTitle())
                    .setSelectedQuestion(selectedQuestion + 1);
        });


        textExplanation.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                textExplanation.setText("Hide Explanation");
                explanationLabel.setVisible(true);
            } else {
                textExplanation.setText("Show Explanation");
                explanationLabel.setVisible(false);
            }
        }));

//        toggleBox.getChildren().remove(videoExplanation);
        ToggleGroup explanationGroup = new ToggleGroup();
        explanationGroup.getToggles().addAll(textExplanation);
        /*explanationGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == textExplanation) {
                textExplanation.setStyle("-fx-background-color: #12AF20; -fx-border-color: #12AF20;");
                textExplanation.setTextFill(Paint.valueOf("#FFFFFF"));
//                videoExplanation.setStyle("-fx-border-color: #12AF20;");
//                videoExplanation.setTextFill(Paint.valueOf("#12AF20"));

                explanationPane.getChildren().remove(explanationVideo);
                if (!explanationPane.getChildren().contains(explanationLabel)) {
                    explanationPane.getChildren().add(explanationLabel);
                }

            } else if (newValue == videoExplanation) {
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
        }));*/

        exitButton.setOnAction(event -> {
            dialogDimmer.setVisible(true);
            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Exit", null, "Are you sure you want to exit?");
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    dialogDimmer.setVisible(false);
                    ViewSwitcher.passData(new HomeScreenController.InitialData(Screen.PRACTICE_SCREEN, null));
                    ViewSwitcher.showScreen(View.HOME_SCREEN);
                } else {
                    dialogDimmer.setVisible(false);
                }
                return buttonType;
            });
            dialog.show();
        });

    }

    private void initializeViews() {
        subjectList.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
        prevButton.setBackground(Background.EMPTY);
        tileScrollPane.setBackground(Background.EMPTY);
        textExplanation.setBackground(Background.EMPTY);
//        videoExplanation.setBackground(Background.EMPTY);

//        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/bookmark2.png").toString()));
//        calculatorImage.setImage(new Image(getClass().getResource("/drawable/calculator.png").toString()));
//        speakerImage.setImage(new Image(getClass().getResource("/drawable/speaker.png").toString()));
//        flagImage.setImage(new Image(getClass().getResource("/drawable/flag2.png").toString()));

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
        String explanationVideoUrl = getClass().getResource("/assets/coding.mp4").toExternalForm();

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
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();
        PracticeScreenVM.QuestionState question = questions.get(subjectQuestionsState.getSelectedQuestion() - 1);

        prevButton.disableProperty().bind(Bindings.greaterThan(2, subjectQuestionsState.selectedQuestionProperty()));
        nextButton.disableProperty().bind(Bindings.equal(questions.size(), subjectQuestionsState.selectedQuestionProperty()));

        questionOverviewLabel.setText("Question " + subjectQuestionsState.getSelectedQuestion() + " of " + questions.size());

        if (viewModel.getQuestionType() == Type.OBJECTIVE) {

            String questionText = question.getObjectiveQuestion().getQuestion();
            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            optionA.setText(" (A) " + question.getObjectiveQuestion().getOptionA().getText());
            optionB.setText(" (B) " + question.getObjectiveQuestion().getOptionB().getText());
            optionC.setText(" (C) " + question.getObjectiveQuestion().getOptionC().getText());
            optionD.setText(" (D) " + question.getObjectiveQuestion().getOptionD().getText());

            optionA.setTextFill(Color.BLACK);
            optionB.setTextFill(Color.BLACK);
            optionC.setTextFill(Color.BLACK);
            optionD.setTextFill(Color.BLACK);

//            String selectedOption = question.getSelectedOption();
//            String questionAnswer = question.getObjectiveQuestion().getQuestionAnswer().getAnswer();

            int selectedOptionId = question.getSelectedOptionId();
            int questionAnswerId = question.getObjectiveQuestion().getQuestionAnswer().getId();

            int optionAId = question.getObjectiveQuestion().getOptionA().getId();
            int optionBId = question.getObjectiveQuestion().getOptionB().getId();
            int optionCId = question.getObjectiveQuestion().getOptionC().getId();
            int optionDId = question.getObjectiveQuestion().getOptionD().getId();

            if (selectedOptionId != -1) { // an option was selected

                if (selectedOptionId == optionAId) {
                    optionAButton.setSelected(true);
                } else if (selectedOptionId == optionBId) {
                    optionBButton.setSelected(true);
                } else if (selectedOptionId == optionCId) {
                    optionCButton.setSelected(true);
                } else if (selectedOptionId  == optionDId) {
                    optionDButton.setSelected(true);
                }

                if (questionAnswerId == optionAId) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);
                }


                if (selectedOptionId != questionAnswerId) { // wrong option was selected

                    if (selectedOptionId == optionAId) {
                        optionA.setTextFill(Color.RED);
                        optionAIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionBId) {
                        optionB.setTextFill(Color.RED);
                        optionBIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionCId) {
                        optionC.setTextFill(Color.RED);
                        optionCIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionDId) {
                        optionD.setTextFill(Color.RED);
                        optionDIcon.setImage(incorrectImage);

                    }

                }

                noOptionSelected.setVisible(false);

            } else { // no option was selected

                if (questionAnswerId == optionAId) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);

                }

                noOptionSelected.setVisible(true);
            }

            explanationLabel.setText(question.getObjectiveQuestion().getQuestionAnswer().getExplanation().replaceAll("<br>", System.lineSeparator()));

        } else if (viewModel.getQuestionType() == Type.THEORY){

            String questionText = question.getTheoryQuestion().getQuestion();
            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            String explanationText = question.getTheoryQuestion().getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));

            centerVBox.getChildren().removeAll(optionAPanel, optionBPanel, optionCPanel, optionDPanel, noOptionSelected);
        }

    }

    /**
     * Changes selected question of the explanation screen and re-renders views
     * @param questionNumber number of the selected question
     */
    private void changeSelectedQuestion(int questionNumber) {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        questionOverviewLabel.setText("Question " + questionNumber + " of " + questions.size());

        if (viewModel.getQuestionType() == Type.OBJECTIVE) {

            ObjectiveQuestion objectiveQuestion = questions.get(questionNumber - 1).getObjectiveQuestion();

            questionLabel.setText(objectiveQuestion.getQuestion().replaceAll("<br>", System.lineSeparator()));

            optionA.setText(" (A) " + objectiveQuestion.getOptionA().getText());
            optionB.setText(" (B) " + objectiveQuestion.getOptionB().getText());
            optionC.setText(" (C) " + objectiveQuestion.getOptionC().getText());
            optionD.setText(" (D) " + objectiveQuestion.getOptionD().getText());

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

//            String selectedOption = questions.get(questionNumber - 1).getSelectedOption();
//            String questionAnswer = questions.get(questionNumber - 1).getObjectiveQuestion().getQuestionAnswer().getAnswer();

            int selectedOptionId = questions.get(questionNumber - 1).getSelectedOptionId();
            int questionAnswerId = questions.get(questionNumber - 1).getObjectiveQuestion().getQuestionAnswer().getId();

            int optionAId = objectiveQuestion.getOptionA().getId();
            int optionBId = objectiveQuestion.getOptionB().getId();
            int optionCId = objectiveQuestion.getOptionC().getId();
            int optionDId = objectiveQuestion.getOptionD().getId();

            if (selectedOptionId != -1) { // an option was selected

                if (selectedOptionId == optionAId) {
                    optionAButton.setSelected(true);
                } else if (selectedOptionId == optionBId) {
                    optionBButton.setSelected(true);
                } else if (selectedOptionId == optionCId) {
                    optionCButton.setSelected(true);
                } else if (selectedOptionId == optionDId) {
                    optionDButton.setSelected(true);
                }

                if (questionAnswerId == optionAId) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.RED);
                    optionDIcon.setImage(incorrectImage);

                }


                if (selectedOptionId != questionAnswerId) { // wrong answer was selected

                    if (selectedOptionId == optionAId) {
                        optionA.setTextFill(Color.RED);
                        optionAIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionBId) {
                        optionB.setTextFill(Color.RED);
                        optionBIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionCId) {
                        optionC.setTextFill(Color.RED);
                        optionCIcon.setImage(incorrectImage);

                    } else if (selectedOptionId == optionDId) {
                        optionD.setTextFill(Color.RED);
                        optionDIcon.setImage(incorrectImage);

                    }

                }

                noOptionSelected.setVisible(false);

            } else { // no option was selected

                if (questionAnswerId == optionAId) {
                    optionA.setTextFill(Color.GREEN);
                    optionAIcon.setImage(correctImage);

                } else if (questionAnswerId == optionBId) {
                    optionB.setTextFill(Color.GREEN);
                    optionBIcon.setImage(correctImage);

                } else if (questionAnswerId == optionCId) {
                    optionC.setTextFill(Color.GREEN);
                    optionCIcon.setImage(correctImage);

                } else if (questionAnswerId == optionDId) {
                    optionD.setTextFill(Color.GREEN);
                    optionDIcon.setImage(correctImage);

                }

                noOptionSelected.setVisible(true);
            }

            explanationLabel.setText(questions.get(questionNumber - 1).getObjectiveQuestion().getQuestionAnswer().getExplanation());

        } else if (viewModel.getQuestionType() == Type.THEORY) {

            String questionText = questions.get(questionNumber - 1).getTheoryQuestion().getQuestion();
            questionLabel.setText(questionText.replaceAll("<br>", System.lineSeparator()));

            String explanationText = questions.get(questionNumber - 1).getTheoryQuestion().getQuestionAnswer().getExplanation();
            explanationLabel.setText(explanationText.replaceAll("<br>", System.lineSeparator()));

        }

    }

    /**
     * Initializes question tiles at bottom of the screen
     */
    private void setupTilePane() {
        PracticeScreenVM.SubjectQuestionsState subjectQuestionsState = viewModel.getSubjectsQuestions().get(viewModel.getSelectedSubject().getShortTitle());
        List<PracticeScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();

        tilePane.getChildren().clear();

        for (int i=1; i <= questions.size(); i++) {
            Rectangle r = new Rectangle(34, 34);
            r.setFill(Color.web("#FFFFFF"));
            r.setStroke(Paint.valueOf("#12AF20"));
            r.setStrokeType(StrokeType.OUTSIDE);

            Label l = new Label(Integer.toString(i));

            if (viewModel.getQuestionType() == Type.OBJECTIVE) {
                if (questions.get(i - 1).getObjectiveQuestion().getQuestionAnswer().getId() == questions.get(i-1).getSelectedOptionId()) {
                    r.setFill(Paint.valueOf("#12AF20"));
                    l.setTextFill(Paint.valueOf("#FFFFFF"));

                } else if (questions.get(i-1).getSelectedOptionId() != -1) {
                    r.setFill(Color.web("#FA0000", 0.5));
                    l.setTextFill(Color.WHITE);
                }

            }

            if (subjectQuestionsState.getSelectedQuestion() == i) {
                r.setStroke(Paint.valueOf("#FCB029"));
                r.setStrokeWidth(2.0);
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

        oldQuestionRect.setStroke(Paint.valueOf("#12AF20"));
        oldQuestionRect.setStrokeWidth(1.0);
        selectedQuestionRect.setStroke(Paint.valueOf("#FCB029"));
        selectedQuestionRect.setStrokeWidth(2);

    }


    public InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data -> " + data);
        return data;
    }

    public static class InitialData {
        private List<PQSubject> subjects;
        private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;
        private Type questionType;

        public InitialData(List<PQSubject> subjects, HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions, Type questionType) {
            this.subjects = subjects;
            this.subjectsQuestions = subjectsQuestions;
            this.questionType = questionType;
        }

        public List<PQSubject> getSubjects() {
            return subjects;
        }

        public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
            return subjectsQuestions;
        }

        public Type getQuestionType() {
            return questionType;
        }
    }
}
