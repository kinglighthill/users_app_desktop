package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.CBTGameScreenController;
import com.scholarly.utme.controller.CBTGameScreenController.InitialData;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CBTGameScreenVM implements ViewModel {

    private SimpleIntegerProperty selectedQuestion = new SimpleIntegerProperty();
    private List<QuestionState> questions = new ArrayList<>();

    private SimpleIntegerProperty fiftyFiftyCount = new SimpleIntegerProperty();

    public CBTGameScreenVM() {
        selectedQuestion.set(1);
    }

    public void processInitialData(InitialData data) {

        data.questionData.forEach(subjectState -> {

            List<QuestionState> questionStates = ObjectiveQuestionDao
                    .getQuestions(
                            subjectState.getSubject().getTableName(),
                            subjectState.getSelectedYear().getId(),
                            false
                    )
                    .stream()
                    .map(question -> new QuestionState(question, new ArrayList<>()))
                    .collect(Collectors.toList());

            questions.addAll(questionStates);
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
