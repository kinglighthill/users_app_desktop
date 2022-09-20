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

    private NovelAuthor author;

    private NovelChapter selectedChapter;


    public void processInitialData(NovelChapterListController.InitialData data) {
        this.novel.set(data.getNovel());
        author = data.getAuthor();

        NovelChapterDao.getNovelChapters().stream().filter(novelChapter ->
                novelChapter.getNovelId() == data.getNovel().getId()).forEach(novelChapter -> chapters.add(novelChapter));

    }

    public Novel getNovel() {
        return novel.get();
    }

    public ObservableList<NovelChapter> getChapters() {
        return chapters;
    }

    public NovelAuthor getAuthor() {
        return author;
    }

    public void setSelectedChapter(NovelChapter selectedChapter) {
        this.selectedChapter = selectedChapter;
    }

    public NovelChapter getSelectedChapter() {
        return selectedChapter;
    }

}
