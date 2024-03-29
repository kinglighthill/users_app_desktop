package com.scholarly.viewmodels.practice_screens;

import com.scholarly.data.model.QuestionDescription;
import com.scholarly.data.model.Year;
import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.ui.utils.View;
import com.scholarly.viewmodels.practice_screens.PracticeScreenVM.Result;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

import static com.scholarly.controller.practice_screens.ResultScreenController.*;

public class ResultScreenVM implements ViewModel, SceneLifecycle {
    private static final String TAG = "ResultScreenVM: ";

    private SimpleStringProperty averageScore = new SimpleStringProperty("0%");
    private SimpleStringProperty total = new SimpleStringProperty("0");
    private ObservableList<Result> results = FXCollections.observableArrayList();
    private List<QuestionDescription> questionDescriptions = FXCollections.observableArrayList();

    private List<PQSubject> subjectList;
    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions;

    private HashMap<String, Year> selectedSubjectYear;

    private View previousScreen;


    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {

    }

    public void processInitialData(InitialData initialData) {
        previousScreen = initialData.getPreviousScreen();
        results.clear();
        results.addAll(initialData.getResults());

        subjectList = initialData.getSubjects();
        questionDescriptions = initialData.getQuestionDescriptions();
        subjectsQuestions = initialData.getSubjectsQuestions();
        selectedSubjectYear = initialData.getSelectedSubjectYear();

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

    public List<QuestionDescription> getQuestionDescriptions() {
        return questionDescriptions;
    }

    public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public HashMap<String, Year> getSelectedSubjectYear() {
        return selectedSubjectYear;
    }

    public View getPreviousScreen() {
        return previousScreen;
    }
}
