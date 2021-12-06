package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.PracticeScreenController;
import com.scholarly.utme.controller.StudyPastQuestScreenController;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.Subject;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StudyPastScreenVM implements ViewModel {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private ObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>();

    private HashMap<String, SubjectQuestionsState> subjectsQuestions = new HashMap<>();


    public void processInitialData(StudyPastQuestScreenController.InitialData data) {

        subjects.addAll(data.questionData.stream().map(SubjectListItemVM.SubjectState::getSubject).collect(Collectors.toList()));

        data.questionData.forEach(subjectState -> {

            List<QuestionState> questionStates = ObjectiveQuestionDao
                    .getQuestions(
                            subjectState.getSubject().getTableName(),
                            subjectState.getSelectedYear().getId(),
                            false
                    ).stream()
                    .map(objectiveQuestion -> new QuestionState(objectiveQuestion, false, false))
                    .collect(Collectors.toList());

            subjectsQuestions.put(subjectState.getSubject().getTableName(), new SubjectQuestionsState(1, questionStates));

        });
    }

    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public Subject getSelectedSubject() {
        return selectedSubject.get();
    }

    public ObjectProperty<Subject> selectedSubjectProperty() {
        return selectedSubject;
    }

    public HashMap<String, SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }

    public static class SubjectQuestionsState {
        private SimpleIntegerProperty selectedQuestion = new SimpleIntegerProperty();
        private List<QuestionState> questions = new ArrayList<>();

        public SubjectQuestionsState(int selectedQuestion, List<QuestionState> questions) {
            this.selectedQuestion.set(selectedQuestion);
            this.questions.addAll(questions);
        }

        public int getSelectedQuestion() {
            return selectedQuestion.get();
        }


        public void setSelectedQuestion(int selectedQuestion) {
            this.selectedQuestion.set(selectedQuestion);
        }

        public SimpleIntegerProperty selectedQuestionProperty() {
            return selectedQuestion;
        }

        public List<QuestionState> getQuestions() {
            return questions;
        }
    }

    public static class QuestionState {
        private ObjectiveQuestion question;
        private boolean showAnswer;
        private boolean showExplanation;

        public QuestionState(ObjectiveQuestion question, boolean showAnswer, boolean showExplanation) {
            this.question = question;
            this.showAnswer = showAnswer;
            this.showExplanation = showExplanation;
        }

        public ObjectiveQuestion getQuestion() {
            return question;
        }

        public boolean isShowAnswer() {
            return showAnswer;
        }

        public boolean isShowExplanation() {
            return showExplanation;
        }

        public void setShowAnswer(boolean showAnswer) {
            this.showAnswer = showAnswer;
        }

        public void setShowExplanation(boolean showExplanation) {
            this.showExplanation = showExplanation;
        }
    }


}
