package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelChapter;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

import static com.scholarly.utme.viewmodels.NovelChapterListVM.*;

public class NovelContentScreenVM implements ViewModel {

    private Novel novel;

    private ObservableList<NovelChapter> chapters = FXCollections.observableArrayList();

    private ObjectProperty<NovelChapter> novelChapter = new SimpleObjectProperty<>();

    private SimpleIntegerProperty chapterPosition = new SimpleIntegerProperty();

    public void processInitialData(NovelState data) {
        chapters.addAll(data.getChapters());
        novel = data.getNovel();

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

    public SimpleIntegerProperty selectedPositionProperty() {
        return new SimpleIntegerProperty(novelChapter.get().getPosition());
    }

    public SimpleStringProperty novelDetailsProperty() {
        return new SimpleStringProperty(novelChapter.get().getDetails());
    }

}
