package com.scholarly.utme.viewmodels.practice_screens;

import com.scholarly.utme.controller.practice_screens.ExplanationScreenController.InitialData;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class ExplanationScreenVM implements ViewModel, SceneLifecycle {

    private ObservableList<PQSubject> subjects = FXCollections.observableArrayList();
    private ObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty<>();

    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions = new HashMap<>();

    private SubjectListItemVM.Type questionType;

    public ExplanationScreenVM() {

    }

    public void processInitialData(InitialData data) {
        subjects.addAll(data.getSubjects());

        data.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
            subjectQuestionsState.setSelectedQuestion(1);
        });

        subjectsQuestions = data.getSubjectsQuestions();

        questionType = data.getQuestionType();
    }

    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {

    }


    public ObservableList<PQSubject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ObservableList<PQSubject> subjects) {
        this.subjects = subjects;
    }

    public PQSubject getSelectedSubject() {
        return selectedSubject.get();
    }

    public ObjectProperty<PQSubject> selectedSubjectProperty() {
        return selectedSubject;
    }

    public void setSelectedSubject(PQSubject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }

    public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public void setSubjectsQuestions(HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions) {
        this.subjectsQuestions = subjectsQuestions;
    }

    public SubjectListItemVM.Type getQuestionType() {
        return questionType;
    }
}
