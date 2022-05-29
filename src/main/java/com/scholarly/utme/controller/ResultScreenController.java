package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.PracticeScreenVM.Result;
import com.scholarly.utme.viewmodels.PracticeScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.ResultScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/ResultScreen.fxml")
public class ResultScreenController implements FxmlView<ResultScreenVM>, Initializable {

    @InjectViewModel
    private ResultScreenVM viewModel;

    @FXML
    private Label averageScoreLabel;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableView<Result> table;

    @FXML
    private TableColumn<Result, String> subjectColumn, yearColumn, totalQuestionsColumn, attemptsColumn, correctAnswersColumn, percentageColumn;

    @FXML
    private ListView<String> subjectListView;

    @FXML
    private BarChart<CategoryAxis, NumberAxis> barChart;

    @FXML
    private Button showExplanationButton, exitButton;;

    private InitialData data;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        averageScoreLabel.textProperty().bind(viewModel.averageScoreProperty());

        viewModel.processInitialData(getInitialData());


        List<String> items = new ArrayList<>();

        items.add("All");
        items.addAll(viewModel.getSubjectList().stream().map(item -> item.getSubjectName()).collect(Collectors.toList()));

        subjectListView.setItems(FXCollections.observableArrayList(items));

        subjectListView.getSelectionModel().select(0);


        barChart.getXAxis().setLabel("Subject");
        barChart.getYAxis().setLabel("Score (%)");

        barChart.getYAxis().setAutoRanging(false);


//        double maxBarWidth=40;
//        double minCategoryGap=10;

//        ViewSwitcher.getRootScene().widthProperty().addListener((obs,n,n1)->{
//            if(barChart.getData().size()==0) return;
//
//            if(n!=null && (n1.doubleValue()>n.doubleValue())){
//                double barWidth=0;
//                do{
//                    double catSpace = xAxis.getCategorySpacing();
//                    double avilableBarSpace = catSpace - (barChart.getCategoryGap() + barChart.getBarGap());
//                    barWidth = (avilableBarSpace / barChart.getData().size()) - barChart.getBarGap();
//                    if (barWidth >maxBarWidth){
//                        avilableBarSpace=(maxBarWidth + barChart.getBarGap())* barChart.getData().size();
//                        barChart.setCategoryGap(catSpace-avilableBarSpace- barChart.getBarGap());
//                    }
//                } while(barWidth>maxBarWidth);
//            }
//
//            if(n!=null && (n1.doubleValue()<n.doubleValue()) && barChart.getCategoryGap()>minCategoryGap){
//                double barWidth=0;
//                do{
//                    double catSpace = xAxis.getCategorySpacing();
//                    double avilableBarSpace = catSpace - (minCategoryGap + barChart.getBarGap());
//                    barWidth = Math.min(maxBarWidth, (avilableBarSpace / barChart.getData().size()) - barChart.getBarGap());
//                    avilableBarSpace=(barWidth + barChart.getBarGap())* barChart.getData().size();
//                    barChart.setCategoryGap(catSpace-avilableBarSpace- barChart.getBarGap());
//                } while(barWidth < maxBarWidth && barChart.getCategoryGap()>minCategoryGap);
//            }
//        });

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Score");

        for (int i = 0; i < viewModel.getResults().size(); i++) {
            Result result = viewModel.getResults().get(i);
            dataSeries1.getData().add(new XYChart.Data(result.getSubjectName(), result.getPercentage()));
        }

        barChart.getData().add(dataSeries1);

        table.setItems(viewModel.getResults());

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


        showExplanationButton.setOnAction(event -> {
            ExplanationScreen.InitialData data = new ExplanationScreen.InitialData(viewModel.getSubjectList(), viewModel.getSubjectsQuestions());
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);
        });

        exitButton.setOnAction(event -> {
            ViewSwitcher.passData("practicePanel");
            ViewSwitcher.showScreen(View.SELECT_SUBJECT_SCREEN);
        });

    }

    public InitialData getInitialData() {
        data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data -> " + data);
        return data;
    }

    public static class InitialData {
        private List<Result> results;
        private List<Subject> subjects;
        private HashMap<String, SubjectQuestionsState> subjectsQuestions;

        public InitialData(List<Result> results, List<Subject> subjects, HashMap<String, SubjectQuestionsState> subjectsQuestions) {
            this.results = results;
            this.subjects = subjects;
            this.subjectsQuestions = subjectsQuestions;
        }

        public List<Subject> getSubjects() {
            return subjects;
        }

        public HashMap<String, SubjectQuestionsState> getSubjectsQuestions() {
            return subjectsQuestions;
        }

        public List<Result> getResults() {
            return results;
        }
    }
}
