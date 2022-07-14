package com.scholarly.utme.controller.landing_screens;

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
import javafx.util.StringConverter;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    }
}
