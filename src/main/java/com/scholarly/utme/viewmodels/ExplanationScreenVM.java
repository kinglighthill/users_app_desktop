package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.ExplanationScreenController.InitialData;
import com.scholarly.utme.data.model.Subject;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class ExplanationScreenVM implements ViewModel, SceneLifecycle {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private ObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>();

    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions = new HashMap<>();


    public ExplanationScreenVM() {

    }

    public void processInitialData(InitialData data) {
        subjects.addAll(data.getSubjects());

        data.getSubjectsQuestions().forEach((s, subjectQuestionsState) -> {
            subjectQuestionsState.setSelectedQuestion(1);
        });

        subjectsQuestions = data.getSubjectsQuestions();
    }

    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {

    }


    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ObservableList<Subject> subjects) {
        this.subjects = subjects;
    }

    public Subject getSelectedSubject() {
        return selectedSubject.get();
    }

    public ObjectProperty<Subject> selectedSubjectProperty() {
        return selectedSubject;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }

    public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public void setSubjectsQuestions(HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions) {
        this.subjectsQuestions = subjectsQuestions;
    }
}
