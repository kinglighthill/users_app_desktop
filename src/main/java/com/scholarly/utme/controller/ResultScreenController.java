package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.cellFactories.PracticeSubjectListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.viewmodels.PracticeScreenVM.Result;
import com.scholarly.utme.viewmodels.PracticeScreenVM.SubjectQuestionsState;
import com.scholarly.utme.viewmodels.ResultScreenVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static com.scholarly.utme.util.Constants.PRACTICE_SCREEN;

@FxmlPath("/layouts/ResultScreen.fxml")
public class ResultScreenController implements FxmlView<ResultScreenVM>, Initializable {
    private static final String TAG = "ResultScreenController: ";

    @InjectViewModel
    private ResultScreenVM viewModel;

    @FXML
    private Label averageScoreLabel, totalScoreLabel;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableView<Result> table;

    @FXML
    private TableColumn<Result, String> subjectColumn, yearColumn, totalQuestionsColumn, attemptsColumn, correctAnswersColumn, percentageColumn;

    @FXML
    private ListView<PQSubject> subjectListView;

    @FXML
    private BarChart<CategoryAxis, NumberAxis> barChart;

    @FXML
    private Button showExplanationButton, exitButton;

    @FXML
    private Pane exitDialogDimmer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewModel.processInitialData(getInitialData());

        initializeViews();

        initializeFont();

        averageScoreLabel.textProperty().bind(viewModel.averageScoreProperty());

        totalScoreLabel.textProperty().bind(viewModel.totalScoreProperty());

        ObservableList<PQSubject> items = FXCollections.observableArrayList();

        PQSubject subject = new PQSubject();
        subject.setTitle("All");
        items.add(subject);
        items.addAll(viewModel.getSubjectList());

        subjectListView.setCellFactory(new PracticeSubjectListCellFactory());
        subjectListView.setItems(items);

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
            ExplanationScreenController.InitialData data = new ExplanationScreenController.InitialData(viewModel.getSubjectList(), viewModel.getSubjectsQuestions(), SubjectListItemVM.Type.OBJECTIVE);
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.EXPLANATION_SCREEN);
        });

        exitButton.setOnAction(event -> {
            View previousScreen = viewModel.getPreviousScreen();

            if (previousScreen == View.HOME_SCREEN) {
                showExitDialog();

            } else if (previousScreen == View.LANDING_SCREEN) {
                ViewSwitcher.passData("performanceButton");
                ViewSwitcher.showScreen(View.LANDING_SCREEN);
            }

        });

    }

    private void initializeViews() {
        subjectListView.setBackground(Background.EMPTY);
        exitButton.setBackground(Background.EMPTY);
        showExplanationButton.setBackground(Background.EMPTY);
    }

    private void initializeFont() {
        showExplanationButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
    }

    private void showExitDialog() {
        Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Confirm Exit", null, "Are you sure you want to quit?");

        exitDialogDimmer.setVisible(true);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.YES) {
                exitDialogDimmer.setVisible(false);
                ViewSwitcher.passData(new HomeScreenController.InitialData(PRACTICE_SCREEN));
                ViewSwitcher.showScreen(View.HOME_SCREEN);
            } else if (buttonType == ButtonType.NO) {
                exitDialogDimmer.setVisible(false);
            }
            return buttonType;
        });

        dialog.show();
    }

    public InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println(TAG + "Got data -> " + data);
        return data;
    }

    public static class InitialData {
        private List<Result> results;
        private List<PQSubject> subjects;
        private HashMap<String, SubjectQuestionsState> subjectsQuestions;
        private View view;

        public InitialData(List<Result> results, List<PQSubject> subjects, HashMap<String, SubjectQuestionsState> subjectsQuestions, View view) {
            this.results = results;
            this.subjects = subjects;
            this.subjectsQuestions = subjectsQuestions;
            this.view = view;
        }

        public List<PQSubject> getSubjects() {
            return subjects;
        }

        public HashMap<String, SubjectQuestionsState> getSubjectsQuestions() {
            return subjectsQuestions;
        }

        public List<Result> getResults() {
            return results;
        }

        public View getView() {
            return view;
        }
    }
}
