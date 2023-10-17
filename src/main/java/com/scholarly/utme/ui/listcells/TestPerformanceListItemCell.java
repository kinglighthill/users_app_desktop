package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.controller.practice_screens.ResultScreenController;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.practice_screens.PracticeScreenVM;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TestPerformanceListItemCell extends ListCell<ResultScreenController.InitialData> {

    private List<PracticeScreenVM.Result> results;
    private List<PQSubject> subjects;
    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;
    private View view;

    public Label testTitle;
    public Label testDate;
    public Label testPercentage;
    public Label testSubjects;


    public TestPerformanceListItemCell() {
        loadFxml();

        setOnMouseClicked(mouseEvent -> {
            ResultScreenController.InitialData data = new ResultScreenController.InitialData(results, subjects, null, subjectsQuestions, null, view);
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.RESULT_SCREEN);
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/test_performance_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(ResultScreenController.InitialData item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            initializeFonts();

            results = item.getResults();
            subjects = item.getSubjects();
            subjectsQuestions = item.getSubjectsQuestions();
            view = item.getPreviousScreen();

            int totalScore = 0;
            double totalPercent = 0;
            int totalQuestions = 0;
            /*for (int i = 0; i < results.size(); i++) {
                totalScore += results.get(i).getCorrectAnswers();
                totalPercent += results.get(i).getPercentage();
                totalQuestions += results.get(i).getTotalQuestions();
            }*/

//            total.set(totalScore + "/" + totalQuestions);
//            testPercentage.setText(String.format("%.1f", (totalPercent/results.size())) + "%");

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void initializeFonts() {
        testTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
        testDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        testPercentage.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
        testSubjects.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }
}
