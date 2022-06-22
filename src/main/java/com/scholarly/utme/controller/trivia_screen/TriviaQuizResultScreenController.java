package com.scholarly.utme.controller.trivia_screen;

import com.scholarly.utme.controller.ResultScreenController;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.listItems.TriviaParticipantItem;
import com.scholarly.utme.ui.cellFactories.TriviaParticipantListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import com.scholarly.utme.viewmodels.trivia_screen.TriviaQuizResultScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/trivia_screen/TriviaQuizResultScreen.fxml")
public class TriviaQuizResultScreenController implements FxmlView<TriviaQuizResultScreenVM>, Initializable {

    @InjectViewModel
    private TriviaQuizResultScreenVM viewModel;

    @FXML
    private Label averageScoreLabel, totalScoreLabel, rankingText;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableView<PracticeScreenVM.Result> table;

    @FXML
    private TableColumn<PracticeScreenVM.Result, String> subjectColumn, yearColumn, totalQuestionsColumn, attemptsColumn, correctAnswersColumn, percentageColumn;

    @FXML
    private ListView<TriviaParticipantItem> participantsList;

    @FXML
    private BarChart<CategoryAxis, NumberAxis> barChart;

    @FXML
    private Button showExplanationButton, backButton;

    private ResultScreenController.InitialData data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        viewModel.processInitialData(getInitialData());

        initializeViews();

        initializeFont();

//        totalScoreLabel.textProperty().bind(viewModel.totalScoreProperty());
//        averageScoreLabel.textProperty().bind(viewModel.averageScoreProperty());

        barChart.getXAxis().setLabel("Subject");
        barChart.getYAxis().setLabel("Score (%)");

        barChart.getYAxis().setAutoRanging(false);


        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Score");

        /*for (int i = 0; i < viewModel.getResults().size(); i++) {
            PracticeScreenVM.Result result = viewModel.getResults().get(i);
            dataSeries1.getData().add(new XYChart.Data(result.getSubjectName(), result.getPercentage()));
        }*/

//        barChart.getData().add(dataSeries1);

//        table.setItems(viewModel.getResults());

        String hideEmptyRowsStyle = ".table-row-cell:empty { -fx-background-color: white; }"
                + ".table-row-cell:empty .table-cell {  -fx-border-width: 0px;  }";

        subjectColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(param.getValue().getSubjectName());
        });

        yearColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(param.getValue().getYear());
        });

        totalQuestionsColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(Integer.toString(param.getValue().getTotalQuestions()));
        });

        attemptsColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(Integer.toString(param.getValue().getAttempts()));
        });

        correctAnswersColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(Integer.toString(param.getValue().getCorrectAnswers()));
        });

        percentageColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(String.format("%.1f", param.getValue().getPercentage()));
        });


        TriviaParticipantItem participant1 = new TriviaParticipantItem("", "Uche Umeh", 300);
        TriviaParticipantItem participant2 = new TriviaParticipantItem("", "John Uzo", 500);
        TriviaParticipantItem participant3 = new TriviaParticipantItem("", "Kingsley Ugwu", 400);
        TriviaParticipantItem participant4 = new TriviaParticipantItem("", "Uche Umeh", 300);
        TriviaParticipantItem participant5 = new TriviaParticipantItem("", "Uche Umeh", 300);
        ObservableList<TriviaParticipantItem> participants = FXCollections.observableArrayList(participant1, participant2, participant3, participant4, participant5);
        participantsList.setItems(participants);
        participantsList.setCellFactory(new TriviaParticipantListCellFactory());


        showExplanationButton.setOnAction(event -> {
            /*ExplanationScreen.InitialData data = new ExplanationScreen.InitialData(viewModel.getSubjectList(), viewModel.getSubjectsQuestions());
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);*/
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.TRIVIA_CHALLENGE_SCREEN);
        });

    }

    private void initializeViews() {
        participantsList.setBackground(Background.EMPTY);
        showExplanationButton.setBackground(Background.EMPTY);
        backButton.setBackground(Background.EMPTY);

        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));
    }

    private void initializeFont() {
        showExplanationButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        rankingText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 15));
    }

    /*public ResultScreenController.InitialData getInitialData() {
        data = (ResultScreenController.InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data -> " + data);
        return data;
    }*/

    public static class InitialData {
        private List<PracticeScreenVM.Result> results;
        private List<Subject> subjects;
        private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;

        public InitialData(List<PracticeScreenVM.Result> results, List<Subject> subjects, HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions) {
            this.results = results;
            this.subjects = subjects;
            this.subjectsQuestions = subjectsQuestions;
        }

        public List<Subject> getSubjects() {
            return subjects;
        }

        public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
            return subjectsQuestions;
        }

        public List<PracticeScreenVM.Result> getResults() {
            return results;
        }
    }
}
