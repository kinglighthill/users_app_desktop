package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.CBTGameScreenController;
import com.scholarly.utme.controller.CBTGameScreenController.InitialData;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.Subject;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CBTGameScreenVM implements ViewModel {

    private SimpleIntegerProperty selectedQuestion = new SimpleIntegerProperty();
    private List<QuestionState> questions = new ArrayList<>();

    private SimpleIntegerProperty fiftyFiftyCount = new SimpleIntegerProperty();

    private List<Subject> subjectList = FXCollections.observableArrayList();
    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions = new HashMap<>();

    private int correctAnswers, incorrectAnswers, questionAttempts;

    public CBTGameScreenVM() {
        selectedQuestion.set(1);
    }

    public void processInitialData(InitialData data) {

        subjectList.addAll(data.questionData.stream().map(SubjectListItemVM.SubjectState::getSubject).collect(Collectors.toList()));
        data.questionData.forEach(subjectState -> {

            List<QuestionState> questionStates = ObjectiveQuestionDao
                    .getQuestions(
                            subjectState.getSubject().getTableName(),
                            subjectState.getSelectedYear().getId(),
                            false
                    )
                    .stream()
                    .limit(subjectState.getNumberOfQuestions())
                    .map(question -> new QuestionState(question, new ArrayList<>()))
                    .collect(Collectors.toList());

            List<PracticeScreenVM.QuestionState> practiceQuestionState = questionStates
                    .stream()
                    .map(questionState -> new PracticeScreenVM.QuestionState(questionState.question, SubjectListItemVM.Type.OBJECTIVE, null))
                    .collect(Collectors.toList());

            questions.addAll(questionStates);

            subjectsQuestions.put(subjectState.getSubject().getTableName(), new PracticeScreenVM.SubjectQuestionsState(1, practiceQuestionState));

        });

        int fiftyFifty = Math.round(questions.size()/10f);

        fiftyFiftyCount.set(fiftyFifty);
    }

    public int getSelectedQuestion() {
        return selectedQuestion.get();
    }

    public SimpleIntegerProperty selectedQuestionProperty() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(int selectedQuestion) {
        this.selectedQuestion.set(selectedQuestion);
    }

    public List<QuestionState> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionState> questions) {
        this.questions = questions;
    }

    public int getFiftyFiftyCount() {
        return fiftyFiftyCount.get();
    }

    public SimpleIntegerProperty fiftyFiftyCountProperty() {
        return fiftyFiftyCount;
    }

    public void setFiftyFiftyCount(int fiftyFiftyCount) {
        this.fiftyFiftyCount.set(fiftyFiftyCount);
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getQuestionAttempts() {
        return questionAttempts;
    }

    public void setQuestionAttempts(int questionAttempts) {
        this.questionAttempts = questionAttempts;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public class QuestionState {
        private ObjectiveQuestion question;
        private List<String> selectedOptions;

        public QuestionState(ObjectiveQuestion question, List<String> selectedOptions) {
            this.question = question;
            this.selectedOptions = selectedOptions;
        }

        public ObjectiveQuestion getQuestion() {
            return question;
        }

        public List<String> getSelectedOptions() {
            return selectedOptions;
        }
    }
}
