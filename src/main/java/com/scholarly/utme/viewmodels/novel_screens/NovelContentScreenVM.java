package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.controller.novel_screens.NovelContentScreenController;
import com.scholarly.utme.data.dao.NovelObjectiveBookmarkDao;
import com.scholarly.utme.data.dao.NovelSectionDao;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.model.novels.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.scholarly.utme.viewmodels.novel_screens.NovelChapterListVM.*;

public class NovelContentScreenVM implements ViewModel {
    private static final String TAG = "NovelContentScreenVM: ";

    private Novel novel;

    private NovelAuthor author;

    private ObservableList<NovelChapter> chapters = FXCollections.observableArrayList();

    private ObjectProperty<NovelChapter> selectedChapter = new SimpleObjectProperty<>();

    private HashMap<Integer, ObservableList<ChapterSection>> chapterSections =  new HashMap<>();

    private HashMap<Integer, ObservableList<NovelObjectiveQuestion>> chapterQuestions = new HashMap<>();

    private HashMap<Integer, ObservableList<NovelObjectiveBookmark>> chapterBookmarks = new HashMap<>();

    private SimpleIntegerProperty selectedQuestionIndex = new SimpleIntegerProperty();

    private SimpleIntegerProperty fiftyFiftyCount = new SimpleIntegerProperty();

    private double correctAnswers, totalGuesses;


    public void processInitialData(NovelContentScreenController.InitialData data) {
        chapters.addAll(data.getChapters());
        novel = data.getNovel();
        author = data.getAuthor();
        selectedChapter.set(data.getSelectedChapter());

        selectedQuestionIndex.set(1);

        chapters.forEach(novelChapter -> {
            chapterSections.put(novelChapter.getId(), NovelSectionDao.getSections(novelChapter.getId()));

            chapterQuestions.put(novelChapter.getId(), ObjectiveQuestionDao.getNovelQuestions(novelChapter.getId()));

//            chapterBookmarks.put(novelChapter.getId(), NovelObjectiveBookmarkDao.getNovelBookmarks(novelChapter.getId()));
        });

        int fiftyFifty = Math.round(chapterQuestions.get(selectedChapter.get().getId()).size()/10f);

        fiftyFiftyCount.set(5);
    }


    public void handleBookmarkClicked() {
        ObservableList<NovelObjectiveBookmark> novelObjectiveBookmarks = chapterBookmarks.get(selectedChapter.get().getId());

        ObservableList<NovelObjectiveQuestion> novelObjectiveQuestions = chapterQuestions.get(selectedChapter.get().getId());

        NovelObjectiveQuestion currentQuestion = novelObjectiveQuestions.get(selectedQuestionIndex.get()-1);

        boolean currentQuestionBookmarked = false;
        NovelObjectiveBookmark bookmarkToDelete = null;

        for (NovelObjectiveBookmark bookmark : novelObjectiveBookmarks) {
            if (bookmark.getQuestionId() == currentQuestion.getId()) {
                currentQuestionBookmarked = true;
                bookmarkToDelete = bookmark;
//                System.out.println(TAG + "Delete bookmark with ID -> " + bookmarkToDelete.getId() + " and question ID -> " + bookmarkToDelete.getQuestionId());
            }
        }

        if (currentQuestionBookmarked) {
            int response = NovelObjectiveBookmarkDao.deleteBookmark(bookmarkToDelete.getQuestionId());
            System.out.println(TAG + "Bookmark with question ID -> " + bookmarkToDelete.getQuestionId() + " deleted with SQL response " + response);
        } else {
            NovelObjectiveBookmarkDao.createBookmark(novel.getId(), selectedChapter.get().getId(), currentQuestion.getId());
        }

//        ObservableList<NovelObjectiveBookmark> newBookmarks = NovelObjectiveBookmarkDao.getNovelBookmarks(
//                selectedChapter.get().getId()
//        );
//
//        chapterBookmarks.get(selectedChapter.get().getId()).clear();
//        chapterBookmarks.put(selectedChapter.get().getId(), newBookmarks);
    }

    public Novel getNovel() {
        return novel;
    }

    public NovelAuthor getAuthor() {
        return author;
    }

    public ObservableList<NovelChapter> getChapters() {
        return chapters;
    }

    public void setSelectedChapter(NovelChapter chapter) {
        this.selectedChapter.set(chapter);
    }

    public NovelChapter getSelectedChapter() {
        return selectedChapter.get();
    }

    public ObjectProperty<NovelChapter> selectedChapterProperty() {
        return selectedChapter;
    }

    public HashMap<Integer, ObservableList<ChapterSection>> getChapterSections() {
        return chapterSections;
    }

    public HashMap<Integer, ObservableList<NovelObjectiveQuestion>> getChapterQuestions() {
        return chapterQuestions;
    }

    public HashMap<Integer, ObservableList<NovelObjectiveBookmark>> getChapterBookmarks() {
        return chapterBookmarks;
    }

    public void setSelectedQuestionIndex(int selectedQuestionIndex) {
        this.selectedQuestionIndex.set(selectedQuestionIndex);
    }

    public int getSelectedQuestionIndex() {
        return selectedQuestionIndex.get();
    }

    public SimpleIntegerProperty selectedQuestionIndexProperty() {
        return selectedQuestionIndex;
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

    public void setTotalGuesses(double totalGuesses) {
        this.totalGuesses = totalGuesses;
    }

    public double getTotalGuesses() {
        return totalGuesses;
    }

    public void setCorrectAnswers(double correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public double getCorrectAnswers() {
        return correctAnswers;
    }

    public double getScorePercentage() {
        return (correctAnswers/totalGuesses) * 100;
    }


    public static class QuestionState {
        private NovelObjectiveQuestion question;
        private List<String> selectedOptions;

        public QuestionState(NovelObjectiveQuestion question, List<String> selectedOptions) {
            this.question = question;
            this.selectedOptions = selectedOptions;
        }

        public NovelObjectiveQuestion getQuestion() {
            return question;
        }

        public List<String> getSelectedOptions() {
            return selectedOptions;
        }
    }

}
