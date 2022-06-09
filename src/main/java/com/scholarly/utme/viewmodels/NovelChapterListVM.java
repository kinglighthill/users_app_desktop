package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.NovelAuthorDao;
import com.scholarly.utme.data.dao.NovelChapterDao;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import com.scholarly.utme.data.model.novels.NovelChapter;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NovelChapterListVM implements ViewModel {

    private SimpleObjectProperty<Novel> novel = new SimpleObjectProperty<>();

    private ObservableList<NovelChapter> chapters = FXCollections.observableArrayList();

    private ObservableList<NovelAuthor> authors = FXCollections.observableArrayList();

    private NovelChapter selectedChapter;


    public void processInitialData(Novel novel) {
        this.novel.set(novel);
        authors.addAll(NovelAuthorDao.getAuthors());

        NovelChapterDao.getNovelChapters().stream().filter(novelChapter -> novelChapter.getNovelId() == novel.getId()).forEach(novelChapter -> chapters.add(novelChapter));

    }

    public Novel getNovel() {
        return novel.get();
    }

    public ObservableList<NovelChapter> getChapters() {
        return chapters;
    }

    public String getAuthor(Novel novel) {
        for (NovelAuthor author : authors) {
            if (author.getId() == novel.getId()) {
                return author.getName();
            }
        }
        return null;
    }

    public void setSelectedChapter(NovelChapter selectedChapter) {
        this.selectedChapter = selectedChapter;
    }

    public NovelChapter getSelectedChapter() {
        return selectedChapter;
    }

    public static class NovelState {
        private Novel novel;
        private ObservableList<NovelChapter> chapters;
        private NovelChapter selectedChapter;

        public NovelState(Novel novel, ObservableList<NovelChapter> novelChapters, NovelChapter chapter) {
            this.novel = novel;
            this.chapters = novelChapters;
            selectedChapter = chapter;
        }

        public Novel getNovel() {
            return novel;
        }

        public void setNovel(Novel novel) {
            this.novel = novel;
        }

        public ObservableList<NovelChapter> getChapters() {
            return chapters;
        }

        public void setChapters(ObservableList<NovelChapter> chapter) {
            this.chapters = chapter;
        }

        public NovelChapter getSelectedChapter() {
            return selectedChapter;
        }

        public void setSelectedChapter(NovelChapter selectedChapter) {
            this.selectedChapter = selectedChapter;
        }
    }

}
