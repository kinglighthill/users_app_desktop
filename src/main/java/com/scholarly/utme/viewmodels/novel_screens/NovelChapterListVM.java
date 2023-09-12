package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.controller.novel_screens.NovelChapterListController;
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


    public void processInitialData(NovelChapterListController.InitialData data) {
        NovelChapterDao novelChapterDao = new NovelChapterDao();
        this.novel.set(data.getNovel());
        authors.addAll(NovelAuthorDao.getAuthors());

        novelChapterDao.getNovelChapters().stream().filter(novelChapter ->
                novelChapter.getNovelId() == data.getNovel().getId()).forEach(novelChapter -> chapters.add(novelChapter));

    }

    public Novel getNovel() {
        return novel.get();
    }

    public ObservableList<NovelChapter> getChapters() {
        return chapters;
    }


    public NovelAuthor getAuthor() {
        for (NovelAuthor author : authors) {
            if (author.getNovelId() == novel.get().getId()) {
                return author;
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

}
