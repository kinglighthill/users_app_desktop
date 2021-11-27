package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.ResultScreenController;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.viewmodels.PracticeScreenVM.Result;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

import static com.scholarly.utme.controller.ResultScreenController.*;

public class ResultScreenVM implements ViewModel, SceneLifecycle {

    private SimpleStringProperty averageScore = new SimpleStringProperty("0%");
    private ObservableList<Result> results = FXCollections.observableArrayList();

    private List<Subject> subjectList;
    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;


    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {

    }

    public void processInitialData(InitialData initialData) {
        results.clear();
        results.addAll(initialData.getResults());

        subjectList = initialData.getSubjects();
        subjectsQuestions = initialData.getSubjectsQuestions();

        double totalScore = 0;
        for (int i = 0; i < results.size(); i++) {
            totalScore += results.get(i).getPercentage();
        }

        averageScore.set(String.format("%.1f", (totalScore/results.size())) + "%");
    }


    public ObservableList<Result> getResults() {
        return results;
    }

    public String getAverageScore() {
        return averageScore.get();
    }

    public SimpleStringProperty averageScoreProperty() {
        return averageScore;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }
}
