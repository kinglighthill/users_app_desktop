package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.PracticeScreenController;
import com.scholarly.utme.controller.PracticeScreenController.InitialData;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.dao.YearsDao;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PracticeScreenVM implements ViewModel, SceneLifecycle {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private ObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>();

    private SimpleLongProperty time = new SimpleLongProperty();

    private HashMap<String, SubjectQuestionsState> subjectsQuestions = new HashMap<>();

    private CompositeDisposable disposables = new CompositeDisposable();



    public PracticeScreenVM() {

    }

    public void processInitialData(InitialData data) {

        subjects.addAll(data.questionData.stream().map(SubjectState::getSubject).collect(Collectors.toList()));

        data.questionData.forEach(subjectState -> {

            List<QuestionState> questionStates = ObjectiveQuestionDao
                    .getQuestions(
                            subjectState.getSubject().getTableName(),
                            subjectState.getSelectedYear().getId(),
                            false
                    )
                    .stream()
                    .map(question -> new QuestionState(question, null))
                    .collect(Collectors.toList());

            subjectsQuestions.put(subjectState.getSubject().getTableName(), new SubjectQuestionsState(1, questionStates));

        });

        time.set(((long) data.hours * 60 * 60) + (data.minutes * 60L));

        disposables.add(
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                        .observeOn(JavaFxScheduler.platform())
                        .subscribe(
                                it -> {
                                    time.set(time.get() - 1);
                                }
                        )
        );
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



    public long getTime() {
        return time.get();
    }

    public SimpleLongProperty timeProperty() {
        return time;
    }


    public List<Result> getResults() {
        List<Result> results = new ArrayList<>();

        subjectsQuestions.forEach((s, subjectQuestionsState) -> {
            Result result = new Result();

            int attempts = 0;
            double correctAnswers = 0;


            result.setSubjectName(s);
            result.setTotalQuestions(subjectQuestionsState.getQuestions().size());
            result.setYear(YearsDao.getYear(subjectQuestionsState.getQuestions().get(0).getQuestion().getYearId()).get().getYear());


            for (int i = 0; i < subjectQuestionsState.getQuestions().size(); i++) {
                QuestionState questionState = subjectQuestionsState.getQuestions().get(i);
                if (questionState.selectedOption != null) {
                    attempts++;
                }
                if (Objects.equals(questionState.selectedOption, questionState.getQuestion().getOptionAnswer())) {
                    correctAnswers += 1;
                }
            }

            result.setAttempts(attempts);
            result.setCorrectAnswers((int) correctAnswers);


            result.setPercentage((correctAnswers/result.getTotalQuestions()) * 100);


            results.add(result);
        });

        return results;
    }

    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {
        disposables.dispose();
    }



    public class SubjectQuestionsState {
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

    public class QuestionState {
        private ObjectiveQuestion question;
        private String selectedOption;

        public QuestionState(ObjectiveQuestion question, String selectedOption) {
            this.question = question;
            this.selectedOption = selectedOption;
        }

        public ObjectiveQuestion getQuestion() {
            return question;
        }

        public String getSelectedOption() {
            return selectedOption;
        }

        public void setSelectedOption(String selectedOption) {
            this.selectedOption = selectedOption;
        }
    }



    public class Result {
        private String subjectName;
        private String year;
        private int totalQuestions;
        private int attempts;
        private int correctAnswers;
        private double percentage;

        public Result() {}

        public Result(String subjectName, String year, int totalQuestions, int attempts, int correctAnswers, double percentage) {
            this.subjectName = subjectName;
            this.year = year;
            this.totalQuestions = totalQuestions;
            this.attempts = attempts;
            this.correctAnswers = correctAnswers;
            this.percentage = percentage;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public void setCorrectAnswers(int correctAnswers) {
            this.correctAnswers = correctAnswers;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }
    }
}
