package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.PracticeScreenController;
import com.scholarly.utme.controller.StudyPastQuestScreenController;
import com.scholarly.utme.data.dao.ObjectiveBookmarkDao;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.dao.TheoryBookmarkDao;
import com.scholarly.utme.data.dao.TheoryQuestionDao;
import com.scholarly.utme.data.model.*;
import com.scholarly.utme.data.model.newDb.PQSubject;
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

import static com.scholarly.utme.viewmodels.SubjectListItemVM.*;

public class StudyPastScreenVM implements ViewModel {
    public static final String TAG = "StudyPastQuestViewModel: ";

    private ObservableList<PQSubject> subjects = FXCollections.observableArrayList();
    private ObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty<>();

    private HashMap<String, SubjectQuestionsState> subjectsQuestions = new HashMap<>();

    private ObservableList<ObjectiveBookmark> objectiveBookmarks = FXCollections.observableArrayList();

    private ObservableList<TheoryBookmark> theoryBookmarks = FXCollections.observableArrayList();

    private Type questionType;

    public void processInitialData(StudyPastQuestScreenController.InitialData data) {

        subjects.addAll(data.questionData.stream().map(SubjectState::getSubject).collect(Collectors.toList()));

        data.questionData.forEach(subjectState -> {

            questionType = subjectState.getType();

            if (subjectState.getType() == Type.OBJECTIVE) {
                List<QuestionState> questionStates = ObjectiveQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getSubjectId(),
                                subjectState.getSelectedYear().getId(),
                                false
                        ).stream()
                        .limit(subjectState.getNumberOfQuestions())
                        .map(objectiveQuestion -> new QuestionState(objectiveQuestion, false, false))
                        .collect(Collectors.toList());

                subjectsQuestions.put(subjectState.getSubject().getShortTitle(), new SubjectQuestionsState(1, questionStates));

                objectiveBookmarks.addAll(ObjectiveBookmarkDao.getBookmarks(subjectState.getSubject().getId()));

            } else if (subjectState.getType() == Type.THEORY) {
                List<QuestionState> questionStates = TheoryQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getSubjectId(),
                                subjectState.getSelectedYear().getId(),
                                false
                        ).stream()
                        .limit(subjectState.getNumberOfQuestions())
                        .map(theoryQuestion -> new QuestionState(theoryQuestion, false, false))
                        .collect(Collectors.toList());

                subjectsQuestions.put(subjectState.getSubject().getShortTitle(), new SubjectQuestionsState(1, questionStates));

                theoryBookmarks.addAll(TheoryBookmarkDao.getBookmarks(subjectState.getSubject().getId()));
            }

        });
    }

    public Type getQuestionType() {
        return questionType;
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

    public ObservableList<ObjectiveBookmark> getObjectiveBookmarks() {
        return objectiveBookmarks;
    }

    public ObservableList<TheoryBookmark> getTheoryBookmarks() {
        return theoryBookmarks;
    }

    public void setSelectedSubject(PQSubject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }

    public void handleBookmarkClicked() {
        StudyPastScreenVM.SubjectQuestionsState subjectQuestionsState = subjectsQuestions.get(selectedSubject.get().getShortTitle());
        List<StudyPastScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        if (questionType == Type.OBJECTIVE) {
            ObjectiveQuestion question = (ObjectiveQuestion) questions.get(selectedQuestion - 1).getQuestion();

            int selectedSubjectId = selectedSubject.get().getId();

            ObservableList<ObjectiveBookmark> oldBookmarks = ObjectiveBookmarkDao.getBookmarks(selectedSubjectId);
            System.out.println(TAG + "oldBookmarks -> " + oldBookmarks);

            boolean currentQuestionBookmarked = false;
            ObjectiveBookmark bookmarkToDelete = null;

            assert oldBookmarks != null;
            for (ObjectiveBookmark bookmark : oldBookmarks) {
                if (bookmark.getQuestionId() == question.getId()) {
                    currentQuestionBookmarked = true;
                    bookmarkToDelete = bookmark;
                    System.out.println("Bookmark to delete with id -> " + bookmark.getId() +  " subject id -> " + bookmark.getSubjectId() + " question id -> " + bookmark.getQuestionId());
                }
            }

            if (currentQuestionBookmarked) {
                int deletedId = ObjectiveBookmarkDao.deleteBookmark(bookmarkToDelete.getSubjectId(), bookmarkToDelete.getQuestionId());
                System.out.println(TAG + "Deleted bookmark with id -> " + deletedId);
            } else {
                int createdId = ObjectiveBookmarkDao.createBookmark(question.getSubjectId(), question.getYearId(), question.getId());
                System.out.println(TAG + "Created bookmark with id -> " + createdId + " subject_id -> " + question.getSubjectId() + " and question_id -> " + question.getId());
            }

            objectiveBookmarks.clear();
            objectiveBookmarks.addAll(ObjectiveBookmarkDao.getBookmarks(selectedSubjectId));

            System.out.println(TAG + "newBookmarks -> " + objectiveBookmarks);

        } else {

            TheoryQuestion question = (TheoryQuestion) questions.get(selectedQuestion - 1).getQuestion();

            int selectedSubjectId = selectedSubject.get().getId();

            ObservableList<TheoryBookmark> oldBookmarks = TheoryBookmarkDao.getBookmarks(selectedSubjectId);
            System.out.println(TAG + "oldBookmarks -> " + oldBookmarks);

            boolean currentQuestionBookmarked = false;
            TheoryBookmark bookmarkToDelete = null;

            assert oldBookmarks != null;
            for (TheoryBookmark bookmark : oldBookmarks) {
                if (bookmark.getQuestionId() == question.getId()) {
                    currentQuestionBookmarked = true;
                    bookmarkToDelete = bookmark;
                    System.out.println("Bookmark to delete with id -> " + bookmark.getId() +  " subject id -> " + bookmark.getSubjectId() + " question id -> " + bookmark.getQuestionId());
                }
            }

            if (currentQuestionBookmarked) {
                int deletedId = TheoryBookmarkDao.deleteBookmark(bookmarkToDelete.getSubjectId(), bookmarkToDelete.getQuestionId());
                System.out.println(TAG + "Deleted bookmark with id -> " + deletedId);
            } else {
                int createdId = TheoryBookmarkDao.createBookmark(question.getSubjectId(), question.getYearId(), question.getId());
                System.out.println(TAG + "Created bookmark with id -> " + createdId + " subject_id -> " + question.getSubjectId() + " and question_id -> " + question.getId());
            }

            theoryBookmarks.clear();
            theoryBookmarks.addAll(TheoryBookmarkDao.getBookmarks(selectedSubjectId));

            System.out.println(TAG + "newBookmarks -> " + theoryBookmarks);
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
        private boolean showAnswer;
        private boolean showExplanation;

        public QuestionState() {

        }

        public QuestionState(Question question, boolean showAnswer, boolean showExplanation) {
            this.question = question;
            this.showAnswer = showAnswer;
            this.showExplanation = showExplanation;
        }

        public Question getQuestion() {
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

    public static class TheoryQuestionState extends QuestionState {
        private TheoryQuestion question;
        private boolean showAnswer;
        private boolean showExplanation;

        public TheoryQuestionState(TheoryQuestion question, boolean showAnswer, boolean showExplanation) {
            this.question = question;
            this.showAnswer = showAnswer;
            this.showExplanation = showExplanation;
        }

        public TheoryQuestion getTheoryQuestion() {
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
