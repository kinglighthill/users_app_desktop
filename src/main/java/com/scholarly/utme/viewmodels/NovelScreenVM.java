package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.NovelAuthorDao;
import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.Novel.Genre;
import com.scholarly.utme.data.model.novels.Novel.Type;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.stream.Collectors;

public class NovelScreenVM implements ViewModel {

    private ObservableList<Novel> novels = FXCollections.observableArrayList();

    private ObservableList<NovelAuthor> authors = FXCollections.observableArrayList();

    private ObjectProperty<Novel> selectedNovel = new SimpleObjectProperty<>();


    public NovelScreenVM() {
        novels.addAll(NovelsDao.getNovels());

        authors.addAll(NovelAuthorDao.getAuthors());

    }

    public ObservableList<Novel> getNovels(Type type, Genre genre) {
        ObservableList<Novel> novelList = FXCollections.observableArrayList();
        for (Novel novel : novels) {
            if (novel.getTypeId() == type.getId() && novel.getGenreId() == genre.getId()) {
                novelList.add(novel);
            }
        }
        return novelList;
    }

    public ObservableList<NovelAuthor> getAuthors() {
        return authors;
    }

    public String getAuthor(Novel selectedNovel) {
        for (NovelAuthor author : authors) {
            if (author.getId() == selectedNovel.getId()) {
                return author.getName();
            }
        }
        return null;
    }

    public ObservableList<Novel> getFirstFourNovels(Type type, Genre genre) {
        ObservableList<Novel> novelList = FXCollections.observableArrayList();
        for (Novel novel : novels) {
            if (novel.getTypeId() == type.getId() && novel.getGenreId() == genre.getId()) {
                novelList.add(novel);
            }
        }
        // Return only the first four novels in the list
        return FXCollections.observableList(novelList.stream().limit(4).collect(Collectors.toList()));
    }

    public void setSelectedNovel(Novel selectedNovel) {
        this.selectedNovel.set(selectedNovel);
    }

    public Novel getSelectedNovel() {
        return selectedNovel.get();
    }

    public ObjectProperty<Novel> selectedNovelProperty() {
        return selectedNovel;
    }


}
