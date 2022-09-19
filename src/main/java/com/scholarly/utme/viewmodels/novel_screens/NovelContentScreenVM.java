package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.data.dao.NovelSectionDao;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.model.novels.ChapterSection;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.model.novels.NovelObjectiveQuestion;
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

    private ObservableList<NovelChapter> chapters = FXCollections.observableArrayList();

    private ObjectProperty<NovelChapter> novelChapter = new SimpleObjectProperty<>();

    private HashMap<Integer, ObservableList<ChapterSection>> chapterSections =  new HashMap<>();

    private HashMap<Integer, ObservableList<NovelObjectiveQuestion>> chapterQuestions = new HashMap<>();


    private List<QuestionState> questions = new ArrayList<>();

    private SimpleIntegerProperty selectedQuestionIndex = new SimpleIntegerProperty();


    public void processInitialData(NovelState data) {
        chapters.addAll(data.getChapters());
        novel = data.getNovel();
        novelChapter.set(data.getSelectedChapter());

        selectedQuestionIndex.set(1);

        chapters.forEach(novelChapter -> {
            chapterSections.put(novelChapter.getId(), NovelSectionDao.getSections(novelChapter.getId()));

            chapterQuestions.put(novelChapter.getId(), ObjectiveQuestionDao.getNovelQuestions(novelChapter.getId()));
        });

//        List<QuestionState> questionStates = chapterQuestions.get(getSelectedChapter())

    }

    public Novel getNovel() {
        return novel;
    }

    public ObservableList<NovelChapter> getChapters() {
        return chapters;
    }

    public void setSelectedChapter(NovelChapter chapter) {
        this.novelChapter.set(chapter);
    }

    public NovelChapter getSelectedChapter() {
        return novelChapter.get();
    }

    public ObjectProperty<NovelChapter> selectedChapterProperty() {
        return novelChapter;
    }

    public HashMap<Integer, ObservableList<ChapterSection>> getChapterSections() {
        return chapterSections;
    }

    public HashMap<Integer, ObservableList<NovelObjectiveQuestion>> getChapterQuestions() {
        return chapterQuestions;
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
