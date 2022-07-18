package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.PracticeScreenController;
import com.scholarly.utme.controller.PracticeScreenController.InitialData;
import com.scholarly.utme.data.dao.*;
import com.scholarly.utme.data.model.Bookmark;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.Question;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import com.scholarly.utme.viewmodels.SubjectListItemVM.Type;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
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

    private HashMap<String, ObservableList<Bookmark>> subjectBookmarks = new HashMap<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    private Type questionType;

    public PracticeScreenVM() {

    }

    public void processInitialData(InitialData data) {

        subjects.addAll(data.questionData.stream().map(SubjectState::getSubject).collect(Collectors.toList()));

        data.questionData.forEach(subjectState -> {

            questionType = subjectState.getType();

            if (subjectState.getType() == Type.OBJECTIVE) {

                List<QuestionState> questionStates = ObjectiveQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getTableName(),
                                subjectState.getSelectedYear().getId(),
                                subjectState.getShuffleQuestions()
                        )
                        .stream()
                        .limit(subjectState.getNumberOfQuestions())
                        .map(question -> new QuestionState(question, Type.OBJECTIVE, null))
                        .collect(Collectors.toList());

                ObservableList<Bookmark> bookmarks = BookmarkDao
                        .getBookmarks(
                                subjectState.getSubject().getId()
                        );

                subjectBookmarks.put(subjectState.getSubject().getTableName(), bookmarks);

                subjectsQuestions.put(subjectState.getSubject().getTableName(), new SubjectQuestionsState(1, questionStates));

            } else if (subjectState.getType() == Type.THEORY) {

                List<QuestionState> questionStates = TheoryQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getTableName(),
                                subjectState.getSelectedYear().getId(),
                                subjectState.getShuffleQuestions()
                        )
                        .stream()
                        .map(question -> new QuestionState(question, Type.THEORY, null))
                        .collect(Collectors.toList());

                ObservableList<Bookmark> bookmarks = BookmarkDao
                        .getBookmarks(
                                subjectState.getSubject().getId()
                        );

                subjectBookmarks.put(subjectState.getSubject().getTableName(), bookmarks);

                subjectsQuestions.put(subjectState.getSubject().getTableName(), new SubjectQuestionsState(1, questionStates));

            }

        });

        time.set(((long) data.hours * 60 * 60) + (data.minutes * 60L));

        disposables.add(
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                        .observeOn(JavaFxScheduler.platform())
                        .subscribe(

                                it -> {
                                    if (time.get() != 0) {
                                        time.set(time.get() - 1);
                                    }
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

    public HashMap<String, ObservableList<Bookmark>> getSubjectBookmarks() {
        return subjectBookmarks;
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


            result.setSubjectName(SubjectDao.getSubjectName(s));
            result.setTotalQuestions(subjectQuestionsState.getQuestions().size());
            result.setYear(YearsDao.getYear(((ObjectiveQuestion)subjectQuestionsState.getQuestions().get(0).getQuestion()).getYearId()).get().getYear());


            for (int i = 0; i < subjectQuestionsState.getQuestions().size(); i++) {
                QuestionState questionState = subjectQuestionsState.getQuestions().get(i);
                if (questionState.selectedOption != null) {
                    attempts++;
                }
                if (Objects.equals(questionState.selectedOption, ((ObjectiveQuestion) questionState.getQuestion()).getOptionAnswer())) {
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


    public Type getQuestionType() {
        return questionType;
    }

    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {
        disposables.dispose();
    }

    public void handleBookmarkClicked() {
        SubjectQuestionsState subjectQuestionsState = subjectsQuestions.get(selectedSubject.get().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        ObservableList<Bookmark> bookmarks = subjectBookmarks.get(selectedSubject.get().getTableName());
        ObjectiveQuestion question = (ObjectiveQuestion)  questions.get(selectedQuestion - 1).getQuestion();

        boolean currentQuestionBookmarked = false;
        Bookmark bookmarkToDelete = null;

        for (int i = 0; i < bookmarks.size(); i++) {
            if (bookmarks.get(i).getQuestionId() == question.getId()) {
                currentQuestionBookmarked = true;
                bookmarkToDelete = bookmarks.get(i);
            }
        }

        if (currentQuestionBookmarked) {
            int deletedId = BookmarkDao.deleteBookmark(bookmarkToDelete.getId());
        } else {
            int createdId = BookmarkDao.createBookmark(question.getId(), question.getSubjectId(), question.getYearId());
        }

        ObservableList<Bookmark> newBookmarks = BookmarkDao
                .getBookmarks(
                        question.getSubjectId()
                );

        subjectBookmarks.get(selectedSubject.get().getTableName()).clear();
        subjectBookmarks.get(selectedSubject.get().getTableName()).addAll(newBookmarks);
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
        private Question question;
        private Type questionType;
        private String selectedOption;

        public QuestionState(Question question, Type questionType, String selectedOption) {
            this.question = question;
            this.questionType = questionType;
            this.selectedOption = selectedOption;
        }

        public Question getQuestion() {
            return question;
        }

        public ObjectiveQuestion getObjectiveQuestion() {
            return (ObjectiveQuestion) question;
        }

        public String getSelectedOption() {
            return selectedOption;
        }

        public Type getQuestionType() {
            return questionType;
        }

        public void setSelectedOption(String selectedOption) {
            this.selectedOption = selectedOption;
        }
    }



    public static class Result {
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
