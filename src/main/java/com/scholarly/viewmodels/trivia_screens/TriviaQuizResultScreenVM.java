package com.scholarly.viewmodels.trivia_screens;

import com.scholarly.controller.practice_screens.ResultScreenController;
import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.viewmodels.practice_screens.PracticeScreenVM;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

public class TriviaQuizResultScreenVM implements ViewModel {

    private SimpleStringProperty averageScore = new SimpleStringProperty("0%");
    private SimpleStringProperty total = new SimpleStringProperty("0");
    private ObservableList<PracticeScreenVM.Result> results = FXCollections.observableArrayList();

    private List<PQSubject> subjectList;
    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;


    public void processInitialData(ResultScreenController.InitialData initialData) {
        results.clear();
        results.addAll(initialData.getResults());

        subjectList = initialData.getSubjects();
        subjectsQuestions = initialData.getSubjectsQuestions();

        int totalScore = 0;
        double totalPercent = 0;
        int totalQuestions = 0;
        for (int i = 0; i < results.size(); i++) {
            totalScore += results.get(i).getCorrectAnswers();
            totalPercent += results.get(i).getPercentage();
            totalQuestions += results.get(i).getTotalQuestions();
        }

        total.set(totalScore + "/" + totalQuestions);
        averageScore.set(String.format("%.1f", (totalPercent/results.size())) + "%");
    }


    public ObservableList<PracticeScreenVM.Result> getResults() {
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
}
