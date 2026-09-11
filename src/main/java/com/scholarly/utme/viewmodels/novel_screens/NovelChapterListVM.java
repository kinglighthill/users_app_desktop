package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.novel_screens.NovelChapterListController;
import com.scholarly.utme.data.dao.NovelAuthorDao;
import com.scholarly.utme.data.dao.NovelChapterDao;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.model.novels.NovelModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NovelChapterListVM implements ViewModel {

    private final SimpleObjectProperty<NovelModel> novelModel = new SimpleObjectProperty<>();

    private final ObservableList<NovelChapter> chapters = FXCollections.observableArrayList();

    private final ObservableList<NovelAuthor> authors = FXCollections.observableArrayList();

    private NovelChapter selectedChapter;


    public void processInitialData(NovelChapterListController.InitialData data) {
        NovelChapterDao novelChapterDao = new NovelChapterDao();
        this.novelModel.set(data.getNovelModel());
        authors.addAll(NovelAuthorDao.getAuthors());

        novelChapterDao.getNovelChapters().stream().filter(
                novelChapter -> novelChapter.getNovelId() == data.getNovelModel().getNovel().getId()
        ).forEach(chapters::add);
        MainApplication.timeTakenTo("Chapters num: " + chapters.size());
    }

    public NovelModel getNovelModel() {
        return novelModel.get();
    }

    public ObservableList<NovelChapter> getChapters() {
        return chapters;
    }


    public NovelAuthor getAuthor() {
        for (NovelAuthor author : authors) {
            if (author.getNovelId() == novelModel.get().getNovel().getId()) {
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
