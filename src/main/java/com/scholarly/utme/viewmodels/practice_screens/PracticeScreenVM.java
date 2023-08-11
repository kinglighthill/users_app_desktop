package com.scholarly.utme.viewmodels.practice_screens;

import com.scholarly.utme.controller.practice_screens.PracticeScreenController.InitialData;
import com.scholarly.utme.data.dao.*;
import com.scholarly.utme.data.model.*;
import com.scholarly.utme.data.model.newDb.ObjectiveQuestionDescription;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.data.model.QuestionDescription;
import com.scholarly.utme.data.model.newDb.TheoryQuestionDescription;
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
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PracticeScreenVM implements ViewModel, SceneLifecycle {
    private static final String TAG = "PracticeScreenViewModel: ";

    private ObservableList<PQSubject> subjects = FXCollections.observableArrayList();

    private ObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty<>();

    private SimpleLongProperty time = new SimpleLongProperty();

    private HashMap<String, SubjectQuestionsState> subjectsQuestions = new HashMap<>();

    private ObservableList<QuestionDescription> questionDescriptions = FXCollections.observableArrayList();

    private ObservableList<ObjectiveQuestionDescription> objectiveQuestionDescriptions = FXCollections.observableArrayList();

    private ObservableList<TheoryQuestionDescription> theoryQuestionDescriptions = FXCollections.observableArrayList();

    private HashMap<String, ObservableList<ObjectiveBookmark>> subjectBookmarks = new HashMap<>();

    private HashMap<Integer, ObservableList<ObjectiveBookmark>> objectiveBookmarks = new HashMap<>();

    private HashMap<Integer, ObservableList<TheoryBookmark>> theoryBookmarks = new HashMap<>();

    private CompositeDisposable timeDisposables = new CompositeDisposable();

    private Type questionType;

    public PracticeScreenVM() {

    }

    public void processInitialData(InitialData data) {

        subjects.addAll(data.questionData.stream().map(SubjectState::getSubject).toList());

        data.questionData.forEach(subjectState -> {

            questionType = subjectState.getType();

            if (subjectState.getType() == Type.OBJECTIVE) {

                List<QuestionState> questionStates = ObjectiveQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId(),
                                FXCollections.observableArrayList(subjectState.getSelectedTopics()),
                                subjectState.getShuffleQuestions()
                        )
                        .stream()
                        .limit(subjectState.getNumberOfQuestions())
                        .map(question -> new QuestionState(question, Type.OBJECTIVE, -1))
                        .collect(Collectors.toList());

                subjectsQuestions.put(subjectState.getSubject().getShortTitle(), new SubjectQuestionsState(1, questionStates));

                ObservableList<ObjectiveBookmark> bookmarks = ObjectiveBookmarkDao.getBookmarks(
                        subjectState.getSubject().getId()
                );

                objectiveBookmarks.put(subjectState.getSubject().getId(), bookmarks);

                List<ObjectiveQuestionDescription> objectiveQuestionDescriptionList = QuestionDescriptionDao
                        .getObjectiveQuestionDescriptions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId()
                        );

                objectiveQuestionDescriptions.addAll(objectiveQuestionDescriptionList);
                questionDescriptions.addAll(objectiveQuestionDescriptionList);

            } else if (subjectState.getType() == Type.THEORY) {

                List<QuestionState> questionStates = TheoryQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId(),
                                FXCollections.observableArrayList(subjectState.getSelectedTopics()),
                                subjectState.getShuffleQuestions()
                        )
                        .stream()
                        .map(question -> new QuestionState(question, Type.THEORY, -1))
                        .collect(Collectors.toList());

                subjectsQuestions.put(subjectState.getSubject().getShortTitle(), new SubjectQuestionsState(1, questionStates));

                ObservableList<TheoryBookmark> bookmarks = TheoryBookmarkDao.getBookmarks(
                        subjectState.getSubject().getId()
                );

                theoryBookmarks.put(subjectState.getSubject().getId(), bookmarks);

                List<TheoryQuestionDescription> theoryQuestionDescriptionList = QuestionDescriptionDao
                        .getTheoryQuestionDescriptions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId()
                        );

                theoryQuestionDescriptions.addAll(theoryQuestionDescriptionList);
                questionDescriptions.addAll(theoryQuestionDescriptionList);

            }

//            List<QuestionDescription> questionDescriptionsList = QuestionDescriptionDao
//                    .getQuestionDescriptions(
//                            subjectState.getSubject().getId(),
//                            subjectState.getSelectedYear().getId()
//                    );
//
//            questionDescriptions.addAll(questionDescriptionsList);


        });

        time.set(((long) data.hours * 60 * 60) + (data.minutes * 60L));

        timeDisposables.add(
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


    public ObservableList<PQSubject> getSubjects() {
        return subjects;
    }

    public PQSubject getSelectedSubject() {
        return selectedSubject.get();
    }

    public ObjectProperty<PQSubject> selectedSubjectProperty() {
        return selectedSubject;
    }

    public HashMap<String, SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public HashMap<String, ObservableList<ObjectiveBookmark>> getSubjectBookmarks() {
        return subjectBookmarks;
    }

    public ObservableList<QuestionDescription> getQuestionDescriptions() {
        return questionDescriptions;
    }

    public ObservableList<ObjectiveQuestionDescription> getObjectiveQuestionDescriptions() {
        return objectiveQuestionDescriptions;
    }

    public ObservableList<TheoryQuestionDescription> getTheoryQuestionDescriptions() {
        return theoryQuestionDescriptions;
    }

    public HashMap<Integer, ObservableList<ObjectiveBookmark>> getObjectiveBookmarks() {
        return objectiveBookmarks;
    }

    public HashMap<Integer, ObservableList<TheoryBookmark>> getTheoryBookmarks() {
        return theoryBookmarks;
    }

    public void setSelectedSubject(PQSubject selectedSubject) {
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


            if (questionType == Type.OBJECTIVE) {
                result.setSubjectName(SubjectDao.getSubjectName(s));
                result.setTotalQuestions(subjectQuestionsState.getQuestions().size());
                result.setYear(YearsDao.getYear(((ObjectiveQuestion)subjectQuestionsState.getQuestions().get(0).getQuestion()).getYearId()).get().getYear());


                for (int i = 0; i < subjectQuestionsState.getQuestions().size(); i++) {

                    QuestionState questionState = subjectQuestionsState.getQuestions().get(i);
                    if (questionState.getSelectedOptionId() != -1) {
                        attempts++;
                    }
                    /*if (Objects.equals(questionState.selectedOption, ((ObjectiveQuestion) questionState.getQuestion()).getOptionAnswer())) {
                        correctAnswers += 1;
                    }*/
                    if (questionState.getSelectedOptionId() == ((ObjectiveQuestion) questionState.getQuestion()).getQuestionAnswer().getId()) {
//                        System.out.println(TAG + "Selected Option ID -> " + questionState.getSelectedOptionId());
//                        System.out.println(TAG + "Selected Question answer ID -> " + ((ObjectiveQuestion) questionState.getQuestion()).getQuestionAnswer().getId());
                        correctAnswers ++;
                    }
                }

                System.out.println(TAG + "Number of attempts -> " + attempts);
                System.out.println(TAG + "Number of correct answers -> " + correctAnswers);

                result.setAttempts(attempts);
                result.setCorrectAnswers((int) correctAnswers);
                result.setPercentage((correctAnswers/result.getTotalQuestions()) * 100);

                results.add(result);

            } else {
                System.out.println(TAG + "No result for Theory Question type");
            }


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
        timeDisposables.dispose();
    }

    public void handleBookmarkClicked() {
        SubjectQuestionsState subjectQuestionsState = subjectsQuestions.get(selectedSubject.get().getShortTitle());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestionIndex = subjectQuestionsState.getSelectedQuestion();

        if (questionType == Type.OBJECTIVE) {
            ObjectiveQuestion currentQuestion = (ObjectiveQuestion) questions.get(selectedQuestionIndex - 1).getQuestion();

            ObservableList<ObjectiveBookmark> oldBookmarks = objectiveBookmarks.get(selectedSubject.get().getId());

            boolean currentQuestionBookmarked = false;
            ObjectiveBookmark bookmarkToDelete = null;

            for (ObjectiveBookmark bookmark : oldBookmarks) {
                if (bookmark.getQuestionId() == currentQuestion.getId()) {
                    currentQuestionBookmarked = true;
                    bookmarkToDelete = bookmark;
                }
            }

            if (currentQuestionBookmarked) {
                ObjectiveBookmarkDao.deleteBookmark(bookmarkToDelete.getQuestionId());
            } else {
                ObjectiveBookmarkDao.createBookmark(currentQuestion.getSubjectId(), currentQuestion.getYearId(), currentQuestion.getId());
            }

            ObservableList<ObjectiveBookmark> newBookmarks = ObjectiveBookmarkDao.getBookmarks(
                    selectedSubject.get().getId()
            );

            objectiveBookmarks.get(selectedSubject.get().getId()).clear();
            objectiveBookmarks.put(selectedSubject.get().getId(), newBookmarks);

        } else {
            TheoryQuestion currentQuestion = (TheoryQuestion) questions.get(selectedQuestionIndex - 1).getQuestion();

            ObservableList<TheoryBookmark> oldBookmarks = theoryBookmarks.get(selectedSubject.get().getId());


            boolean currentQuestionBookmarked = false;
            TheoryBookmark bookmarkToDelete = null;

            for (TheoryBookmark bookmark : oldBookmarks) {
                if (bookmark.getQuestionId() == currentQuestion.getId()) {
                    currentQuestionBookmarked = true;
                    bookmarkToDelete = bookmark;
                }
            }

            if (currentQuestionBookmarked) {
                TheoryBookmarkDao.deleteBookmark(bookmarkToDelete.getQuestionId());
            } else {
                TheoryBookmarkDao.createBookmark(currentQuestion.getSubjectId(), currentQuestion.getYearId(), currentQuestion.getId());
            }

            ObservableList<TheoryBookmark> newBookmarks = TheoryBookmarkDao.getBookmarks(
                    selectedSubject.get().getId()
            );

            theoryBookmarks.get(selectedSubject.get().getId()).clear();
            theoryBookmarks.put(selectedSubject.get().getId(), newBookmarks);

        }
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
        private int selectedOptionId;

       /* public QuestionState(Question question, Type questionType, String selectedOption) {
            this.question = question;
            this.questionType = questionType;
            this.selectedOption = selectedOption;
        }*/

        public QuestionState(Question question, Type questionType, int selectedOptionId) {
            this.question = question;
            this.questionType = questionType;
            this.selectedOptionId = selectedOptionId;
        }

        public Question getQuestion() {
            return question;
        }

        public ObjectiveQuestion getObjectiveQuestion() {
            return (ObjectiveQuestion) question;
        }

        public TheoryQuestion getTheoryQuestion() {
            return (TheoryQuestion) question;
        }

        public Type getQuestionType() {
            return questionType;
        }

        public void setSelectedOptionId(int selectedOptionId) {
            this.selectedOptionId = selectedOptionId;
        }

        public int getSelectedOptionId() {
            return selectedOptionId;
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
