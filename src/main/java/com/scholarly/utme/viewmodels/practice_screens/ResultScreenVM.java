package com.scholarly.utme.viewmodels.practice_screens;

import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.viewmodels.practice_screens.PracticeScreenVM;
import com.scholarly.utme.viewmodels.practice_screens.PracticeScreenVM.Result;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

import static com.scholarly.utme.controller.practice_screens.ResultScreenController.*;

public class ResultScreenVM implements ViewModel, SceneLifecycle {

    private SimpleStringProperty averageScore = new SimpleStringProperty("0%");
    private SimpleStringProperty total = new SimpleStringProperty("0");
    private ObservableList<Result> results = FXCollections.observableArrayList();

    private List<PQSubject> subjectList;
    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;

    private View previousScreen;


    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {

    }

    public void processInitialData(InitialData initialData) {
        previousScreen = initialData.getView();
        results.clear();
        results.addAll(initialData.getResults());

        subjectList = initialData.getSubjects();
        subjectsQuestions = initialData.getSubjectsQuestions();

        double totalScore = 0;
        double totalPercent = 0;
        double totalQuestions = 0;
        for (int i = 0; i < results.size(); i++) {
            totalScore += results.get(i).getCorrectAnswers();
            totalPercent += results.get(i).getPercentage();
            totalQuestions += results.get(i).getTotalQuestions();
        }

        double newTotalScore = (totalScore / totalQuestions) * 400;
        total.set((int)newTotalScore + "/" + 400);
        averageScore.set(String.format("%.1f", (totalPercent/results.size())) + "%");
    }


    public ObservableList<Result> getResults() {
        return results;
    }

    public String getAverageScore() {
        return averageScore.get();
    }

    public SimpleStringProperty totalScoreProperty() {
        return total;
    }

    public SimpleStringProperty averageScoreProperty() {
        return averageScore;
    }

    public List<PQSubject> getSubjectList() {
        return subjectList;
    }

    public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public View getPreviousScreen() {
        return previousScreen;
    }
}
