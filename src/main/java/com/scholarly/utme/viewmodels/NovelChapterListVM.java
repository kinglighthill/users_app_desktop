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

}
