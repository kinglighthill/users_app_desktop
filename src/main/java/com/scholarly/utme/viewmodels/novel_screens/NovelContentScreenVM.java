package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelChapter;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.scholarly.utme.viewmodels.novel_screens.NovelChapterListVM.*;

public class NovelContentScreenVM implements ViewModel {

    private Novel novel;

    private ObservableList<NovelChapter> chapters = FXCollections.observableArrayList();

    private ObjectProperty<NovelChapter> novelChapter = new SimpleObjectProperty<>();


    public void processInitialData(NovelState data) {
        chapters.addAll(data.getChapters());
        novel = data.getNovel();
        novelChapter.set(data.getSelectedChapter());
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


}
