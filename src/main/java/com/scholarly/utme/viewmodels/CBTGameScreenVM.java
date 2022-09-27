package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.CBTGameScreenController;
import com.scholarly.utme.controller.CBTGameScreenController.InitialData;
import com.scholarly.utme.data.dao.ObjectiveBookmarkDao;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.dao.QuestionDescriptionDao;
import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.QuestionDescription;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.PQSubject;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CBTGameScreenVM implements ViewModel {
    private static final String TAG = "CBTGameScreenVM: ";

    private SimpleIntegerProperty selectedQuestion = new SimpleIntegerProperty();
    private List<QuestionState> questions = new ArrayList<>();

    private SimpleIntegerProperty fiftyFiftyCount = new SimpleIntegerProperty();

    private List<PQSubject> subjectList = FXCollections.observableArrayList();
    private HashMap<String, PracticeScreenVM.SubjectQuestionsState> subjectsQuestions = new HashMap<>();

    private ObservableList<QuestionDescription> questionDescriptions = FXCollections.observableArrayList();

    private ObservableList<ObjectiveBookmark> objectiveBookmarks = FXCollections.observableArrayList();

    private int correctAnswers, incorrectAnswers, questionAttempts;

    public CBTGameScreenVM() {
        selectedQuestion.set(1);
    }

    public void processInitialData(InitialData data) {

        subjectList.addAll(data.questionData.stream().map(SubjectListItemVM.SubjectState::getSubject).collect(Collectors.toList()));
        data.questionData.forEach(subjectState -> {

            List<QuestionState> questionStates = ObjectiveQuestionDao
                    .getQuestions(
                            subjectState.getSubject().getId(),
                            subjectState.getSelectedYear().getId(),
                            FXCollections.emptyObservableList(),
                            false
                    )
                    .stream()
                    .limit(subjectState.getNumberOfQuestions())
                    .map(question -> new QuestionState(question, new ArrayList<>()))
                    .collect(Collectors.toList());

            List<PracticeScreenVM.QuestionState> practiceQuestionState = questionStates
                    .stream()
                    .map(questionState -> new PracticeScreenVM.QuestionState(questionState.question, SubjectListItemVM.Type.OBJECTIVE, -1))
                    .collect(Collectors.toList());

            questions.addAll(questionStates);

            subjectsQuestions.put(subjectState.getSubject().getShortTitle(), new PracticeScreenVM.SubjectQuestionsState(1, practiceQuestionState));

            List<QuestionDescription> questionDescriptionsList = QuestionDescriptionDao
                    .getQuestionDescriptions(
                            subjectState.getSubject().getId(),
                            subjectState.getSelectedYear().getId()
                    );
            assert questionDescriptionsList != null;
            questionDescriptions.addAll(questionDescriptionsList);

            objectiveBookmarks.addAll(ObjectiveBookmarkDao.getBookmarks(subjectState.getSubject().getId()));

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

    public ObservableList<QuestionDescription> getQuestionDescriptions() {
        return questionDescriptions;
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

    public List<PQSubject> getSubjectList() {
        return subjectList;
    }

    public HashMap<String, PracticeScreenVM.SubjectQuestionsState> getSubjectsQuestions() {
        return subjectsQuestions;
    }

    public ObservableList<ObjectiveBookmark> getObjectiveBookmarks() {
        return objectiveBookmarks;
    }

    public void handleBookmarkClicked() {

        ObjectiveQuestion question = questions.get(selectedQuestion.get() - 1).getQuestion();

        ObservableList<ObjectiveBookmark> oldBookmarks = ObjectiveBookmarkDao.getBookmarks();
//        System.out.println(TAG + "oldBookmarks -> " + oldBookmarks);

        boolean currentQuestionBookmarked = false;
        ObjectiveBookmark bookmarkToDelete = null;

        for (ObjectiveBookmark bookmark : oldBookmarks) {
            if (bookmark.getQuestionId() == question.getId()) {
                currentQuestionBookmarked = true;
                bookmarkToDelete = bookmark;
//                System.out.println("Bookmark to delete with id -> " + bookmark.getId() +  " subject id -> " + bookmark.getSubjectId() + " question id -> " + bookmark.getQuestionId());
            }
        }

        if (currentQuestionBookmarked) {
            int deletedId = ObjectiveBookmarkDao.deleteBookmark(bookmarkToDelete.getQuestionId());
//            System.out.println(TAG + "Deleted bookmark with id -> " + deletedId);
        } else {
            int createdId = ObjectiveBookmarkDao.createBookmark(question.getSubjectId(), question.getYearId(), question.getId());
//            System.out.println(TAG + "Created bookmark with id -> " + createdId + " subject_id -> " + question.getSubjectId() + " and question_id -> " + question.getId());
        }

        objectiveBookmarks.clear();
        objectiveBookmarks.addAll(ObjectiveBookmarkDao.getBookmarks());

//        System.out.println(TAG + "newBookmarks -> " + objectiveBookmarks);

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
