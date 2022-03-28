package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.PracticeScreenController;
import com.scholarly.utme.controller.StudyPastQuestScreenController;
import com.scholarly.utme.data.dao.BookmarkDao;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.model.Bookmark;
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

    private HashMap<String, ObservableList<Bookmark>> subjectBookmarks = new HashMap<>();


    public void processInitialData(StudyPastQuestScreenController.InitialData data) {

        subjects.addAll(data.questionData.stream().map(SubjectListItemVM.SubjectState::getSubject).collect(Collectors.toList()));

        data.questionData.forEach(subjectState -> {

            List<QuestionState> questionStates = ObjectiveQuestionDao
                    .getQuestions(
                            subjectState.getSubject().getTableName(),
                            subjectState.getSelectedYear().getId(),
                            false
                    ).stream()
                    .map(objectiveQuestion -> new QuestionState(objectiveQuestion, false, false, false))
                    .collect(Collectors.toList());

            subjectsQuestions.put(subjectState.getSubject().getTableName(), new SubjectQuestionsState(1, questionStates));

            ObservableList<Bookmark> bookmarks = BookmarkDao
                    .getBookmarks(
                            subjectState.getSubject().getId()
                    );

            subjectBookmarks.put(subjectState.getSubject().getTableName(), bookmarks);

            /*assert bookmarks != null;
            if (!bookmarks.isEmpty()){
                System.out.println("Got subject bookmark with id: " + bookmarks.get(1).getId());
            }*/

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

    public HashMap<String, ObservableList<Bookmark>> getSubjectBookmarks() {
        return subjectBookmarks;
    }

    public void handleBookmarkClicked() {

       SubjectQuestionsState subjectQuestionsState = subjectsQuestions.get(selectedSubject.get().getTableName());
        List<QuestionState> questions = subjectQuestionsState.getQuestions();
        int selectedQuestion = subjectQuestionsState.getSelectedQuestion();

        ObservableList<Bookmark> bookmarks = subjectBookmarks.get(selectedSubject.get().getTableName());
        ObjectiveQuestion question = questions.get(selectedQuestion - 1).getQuestion();

        boolean currentQuestionBookmarked = false;
        Bookmark bookmarkToDelete = null;

        for (Bookmark bookmark : bookmarks) {
            if (bookmark.getQuestionId() == question.getId()) {
                currentQuestionBookmarked = true;
                bookmarkToDelete = bookmark;
            }
        }

        if (currentQuestionBookmarked) {
            int deletedId = BookmarkDao.deleteBookmark(bookmarkToDelete.getId());
            System.out.println("Deleted bookmark with id: " + deletedId);
        } else {
            int createdId = BookmarkDao.createBookmark(question.getId(), question.getSubjectId(), question.getYearId());
            System.out.println("Created bookmark with id: " + createdId);

        }

        ObservableList<Bookmark> newBookmarks = BookmarkDao
                .getBookmarks(
                        question.getSubjectId()
                );

        subjectBookmarks.get(selectedSubject.get().getTableName()).clear();
        assert newBookmarks != null;
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
        private ObjectiveQuestion question;
        private boolean showAnswer;
        private boolean showExplanation;
        private boolean isBookmarked;

        public QuestionState(ObjectiveQuestion question, boolean showAnswer, boolean showExplanation, boolean isBookmarked) {
            this.question = question;
            this.showAnswer = showAnswer;
            this.showExplanation = showExplanation;
            this.isBookmarked = isBookmarked;
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

        public boolean isBookmarked(){
            return isBookmarked;
        }

        public void setIsBookmarked(boolean isBookmarked){
            this.isBookmarked = isBookmarked;
        }

        public void setShowAnswer(boolean showAnswer) {
            this.showAnswer = showAnswer;
        }

        public void setShowExplanation(boolean showExplanation) {
            this.showExplanation = showExplanation;
        }
    }


}
