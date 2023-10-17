package com.scholarly.utme.viewmodels.practice_screens;

import com.scholarly.utme.controller.practice_screens.StudyPastQuestScreenController2;
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
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import static com.scholarly.utme.viewmodels.SubjectListItemVM.Type;

public class StudyPastScreenVM2 implements ViewModel {
    public static final String TAG = "StudyPastQuestViewModel: ";

    private final ObservableList<PQSubject> subjects = FXCollections.observableArrayList();
    private final ObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty<>();

    private SubjectQuestionsState subjectsQuestions;

    private final ObservableList<QuestionDescription> questionDescriptions = FXCollections.observableArrayList();

    private final ObservableList<ObjectiveQuestionDescription> objectiveQuestionDescriptions = FXCollections.observableArrayList();

    private final ObservableList<TheoryQuestionDescription> theoryQuestionDescriptions = FXCollections.observableArrayList();

    private final HashMap<Integer, ObservableList<ObjectiveBookmark>> objectiveBookmarks = new HashMap<>();

    private final HashMap<Integer, ObservableList<TheoryBookmark>> theoryBookmarks = new HashMap<>();

    private Type questionType;

    public void processInitialData(StudyPastQuestScreenController2.InitialData data) {
        subjectsQuestions = new SubjectQuestionsState();

        subjects.addAll(data.questionData.stream().map(SubjectState::getSubject).toList());

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
                        .map(objectiveQuestion -> new QuestionState(subjectState.getSubject(), objectiveQuestion, false, false))
                        .collect(Collectors.toList());

                subjectsQuestions.addQuestions(subjectState.getSubject().getShortTitle(), questionStates);

                ObservableList<ObjectiveBookmark> bookmarks = ObjectiveBookmarkDao.getBookmarks(subjectState.getSubject().getId());

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
                        .map(theoryQuestion -> new QuestionState(subjectState.getSubject(), theoryQuestion, false, false))
                        .collect(Collectors.toList());

                subjectsQuestions.addQuestions(subjectState.getSubject().getShortTitle(), questionStates);;

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

    public SubjectQuestionsState getSubjectsQuestions() {
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

    /*public void handleBookmarkClicked() {
        StudyPastScreenVM2.SubjectQuestionsState subjectQuestionsState = subjectsQuestions.get(selectedSubject.get().getShortTitle());
        List<StudyPastScreenVM2.QuestionState> questions = subjectQuestionsState.getQuestions();
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

    }*/

    public static class SubjectQuestionsState {
        private final SimpleIntegerProperty selectedQuestion = new SimpleIntegerProperty();
        private final List<QuestionState> questions = new ArrayList<>();

        private final HashMap<String, Pair<Integer, Integer>> subjectStartIndexAndCountMap = new HashMap<>();

        public SubjectQuestionsState() {
            this.selectedQuestion.set(0);
        }

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

        public void addQuestions(String subjectTitle, List<QuestionState> questions) {
            subjectStartIndexAndCountMap.put(subjectTitle, new Pair<>(this.questions.size(), questions.size()));
            this.questions.addAll(questions);
        }

        public Pair<Integer, Integer> getQuestionNumberAndCount(String subjectTitle, int selectedIndex) {
            Pair<Integer, Integer> startIndexAndCount = subjectStartIndexAndCountMap.get(subjectTitle);
            int questionNumber = selectedIndex - startIndexAndCount.getKey() + 1;
            int questionCount = startIndexAndCount.getValue();
            return new Pair<>(questionNumber, questionCount);
        }

        public Integer getSubjectStartIndex(String subjectTitle) {
            Pair<Integer, Integer> startIndexAndCount = subjectStartIndexAndCountMap.get(subjectTitle);
            return startIndexAndCount.getKey();
        }

        public Integer getSubjectQuestionCount(String subjectTitle) {
            Pair<Integer, Integer> startIndexAndCount = subjectStartIndexAndCountMap.get(subjectTitle);
            return startIndexAndCount.getValue();
        }

        public boolean moveToNextSubject(String subjectTitle, int selectedIndex) {
            Pair<Integer, Integer> startIndexAndCount = subjectStartIndexAndCountMap.get(subjectTitle);

            return selectedIndex >= startIndexAndCount.getKey() + startIndexAndCount.getValue();
        }

        public boolean moveToPrevSubject(String subjectTitle, int selectedIndex) {
            Pair<Integer, Integer> startIndexAndCount = subjectStartIndexAndCountMap.get(subjectTitle);

            return selectedIndex < startIndexAndCount.getKey();
        }

        public SimpleIntegerProperty selectedQuestionProperty() {
            return selectedQuestion;
        }

        public List<QuestionState> getQuestions() {
            return questions;
        }
    }

    public static class QuestionState {
        private PQSubject pqSubject;
        private Question question;
        private boolean showAnswer;
        private boolean showExplanation;

        public QuestionState() {

        }

        public QuestionState(PQSubject pqSubject, Question question, boolean showAnswer, boolean showExplanation) {
            this.pqSubject = pqSubject;
            this.question = question;
            this.showAnswer = showAnswer;
            this.showExplanation = showExplanation;
        }

        public PQSubject getPqSubject() {
            return pqSubject;
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
