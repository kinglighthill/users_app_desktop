package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.controller.ResultScreenController;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.listItems.TestPerformanceItem;
import com.scholarly.utme.ui.cellFactories.TestPerformanceListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.viewmodels.PracticeScreenVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenPerformanceVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen_performance.fxml")
public class LandingScreenPerformanceController implements FxmlView<LandingScreenPerformanceVM>, Initializable {

    @FXML
    private ChoiceBox<String> practiceChoiceBox;

    @FXML
    private LineChart<CategoryAxis, NumberAxis> lineChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private ListView<ResultScreenController.InitialData> testPerformancesList;

    @FXML
    private Label pointsHeader, pointsText, totalTestHeader, totalTestText, topSubjectsHeader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        ObservableList<String> items = FXCollections.observableArrayList("CBT Practice");
        practiceChoiceBox.setItems(items);
        practiceChoiceBox.getSelectionModel().selectFirst();

        XYChart.Series dataSeries = new XYChart.Series<>();
        dataSeries.setName("Tests");
        dataSeries.getData().add(new XYChart.Data<>("T1", 40));
        dataSeries.getData().add(new XYChart.Data<>("T2", 80));
        dataSeries.getData().add(new XYChart.Data<>("T3", 20));
        dataSeries.getData().add(new XYChart.Data<>("T4", 60));
        dataSeries.getData().add(new XYChart.Data<>("T5", 40));
        dataSeries.getData().add(new XYChart.Data<>("T6", 80));

        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelGap(10);

        StringConverter<Number> converter = new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.valueOf(number.intValue()).concat("%");
            }

            @Override
            public Number fromString(String s) {
                return null;
            }
        };
        yAxis.setTickLabelFormatter(converter);
        yAxis.setTickUnit(20);

        lineChart.getData().add(dataSeries);

        PracticeScreenVM.Result result = new PracticeScreenVM.Result("English", "2020", 24, 20, 16, 38.7);
        List<PracticeScreenVM.Result> results = new ArrayList<>();
        results.add(result);

        Subject subject = new Subject(3, "english", "English Language", 50, null, null, null, null);
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);

        PracticeScreenVM.QuestionState questionState = new PracticeScreenVM.QuestionState(null, SubjectListItemVM.Type.OBJECTIVE, "3");
        List<PracticeScreenVM.QuestionState> questionStates = new ArrayList<>();
        questionStates.add(questionState);

        HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectQuestions = new HashMap<>();
        subjectQuestions.put("english", new PracticeScreenVM.SubjectQuestionsState(4, questionStates));

        ResultScreenController.InitialData performance1 = new ResultScreenController.InitialData(results, subjects, subjectQuestions, View.LANDING_SCREEN);

        ObservableList<ResultScreenController.InitialData> performanceItems = FXCollections.observableArrayList(performance1);
        testPerformancesList.setCellFactory(new TestPerformanceListCellFactory());
        testPerformancesList.setItems(performanceItems);
    }

    private void initializeViews() {

    }

    private void initializeFonts() {
        pointsHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        pointsText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        totalTestHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        totalTestText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        topSubjectsHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
    }
}
