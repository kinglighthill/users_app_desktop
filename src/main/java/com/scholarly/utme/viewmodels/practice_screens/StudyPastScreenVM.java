package com.scholarly.utme.viewmodels.practice_screens;

import com.scholarly.utme.controller.practice_screens.StudyPastQuestScreenController;
import com.scholarly.utme.data.dao.*;
import com.scholarly.utme.data.model.*;
import com.scholarly.utme.data.model.newDb.ObjectiveQuestionDescription;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.data.model.newDb.TheoryQuestionDescription;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.scholarly.utme.viewmodels.SubjectListItemVM.*;

public class StudyPastScreenVM implements ViewModel {
    public static final String TAG = "StudyPastQuestViewModel: ";

    private ObservableList<PQSubject> subjects = FXCollections.observableArrayList();
    private ObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty<>();

    private HashMap<String, SubjectQuestionsState> subjectsQuestions = new HashMap<>();

    private ObservableList<QuestionDescription> questionDescriptions = FXCollections.observableArrayList();

    private ObservableList<ObjectiveQuestionDescription> objectiveQuestionDescriptions = FXCollections.observableArrayList();

    private ObservableList<TheoryQuestionDescription> theoryQuestionDescriptions = FXCollections.observableArrayList();

    private HashMap<Integer, ObservableList<ObjectiveBookmark>> objectiveBookmarks = new HashMap<>();

    private HashMap<Integer, ObservableList<TheoryBookmark>> theoryBookmarks = new HashMap<>();

    private Type questionType;

    public void processInitialData(StudyPastQuestScreenController.InitialData data) {

        subjects.addAll(data.questionData.stream().map(SubjectState::getSubject).collect(Collectors.toList()));

        data.questionData.forEach(subjectState -> {

            questionType = subjectState.getType();

            if (subjectState.getType() == Type.OBJECTIVE) {
                List<QuestionState> questionStates = ObjectiveQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId(),
                                FXCollections.emptyObservableList(),
                                false
                        ).stream()
                        .limit(subjectState.getNumberOfQuestions())
                        .map(objectiveQuestion -> new QuestionState(objectiveQuestion, false, false))
                        .collect(Collectors.toList());

                subjectsQuestions.put(subjectState.getSubject().getShortTitle(), new SubjectQuestionsState(1, questionStates));

                ObservableList<ObjectiveBookmark> bookmarks = ObjectiveBookmarkDao.getBookmarks(
                        subjectState.getSubject().getId()
                );

                objectiveBookmarks.put(subjectState.getSubject().getId(), bookmarks);

                List<ObjectiveQuestionDescription> questionDescriptionsList = QuestionDescriptionDao
                        .getObjectiveQuestionDescriptions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId()
                        );

                objectiveQuestionDescriptions.addAll(questionDescriptionsList);

            } else if (subjectState.getType() == Type.THEORY) {
                List<QuestionState> questionStates = TheoryQuestionDao
                        .getQuestions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId(),
                                FXCollections.emptyObservableList(),
                                false
                        ).stream()
                        .limit(subjectState.getNumberOfQuestions())
                        .map(theoryQuestion -> new QuestionState(theoryQuestion, false, false))
                        .collect(Collectors.toList());

                subjectsQuestions.put(subjectState.getSubject().getShortTitle(), new SubjectQuestionsState(1, questionStates));

                ObservableList<TheoryBookmark> bookmarks = TheoryBookmarkDao.getBookmarks(
                        subjectState.getSubject().getId()
                );

                theoryBookmarks.put(subjectState.getSubject().getId(), bookmarks);

                List<TheoryQuestionDescription> questionDescriptionsList = QuestionDescriptionDao
                        .getTheoryQuestionDescriptions(
                                subjectState.getSubject().getId(),
                                subjectState.getSelectedYear().getId()
                        );

                theoryQuestionDescriptions.addAll(questionDescriptionsList);
            }

//            List<QuestionDescription> questionDescriptionsList = QuestionDescriptionDao
//                    .getQuestionDescriptions(
//                            subjectState.getSubject().getId(),
//                            subjectState.getSelectedYear().getId()
//                    );
//
//            questionDescriptions.addAll(questionDescriptionsList);

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

    public void handleBookmarkClicked() {
        StudyPastScreenVM.SubjectQuestionsState subjectQuestionsState = subjectsQuestions.get(selectedSubject.get().getShortTitle());
        List<StudyPastScreenVM.QuestionState> questions = subjectQuestionsState.getQuestions();
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

}
