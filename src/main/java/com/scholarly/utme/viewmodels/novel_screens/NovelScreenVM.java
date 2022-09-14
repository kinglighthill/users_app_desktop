package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.data.dao.NovelAuthorDao;
import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.newDb.contentType.html.Link;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.Novel.Genre;
import com.scholarly.utme.data.model.novels.Novel.Type;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import com.scholarly.utme.data.model.novels.NovelCategory;
import com.scholarly.utme.data.model.novels.NovelGenre;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class NovelScreenVM implements ViewModel {
    private static final String TAG = "NovelScreenVM: ";

    private ObservableList<Novel> novels = FXCollections.observableArrayList();

    private ObservableList<NovelAuthor> authors = FXCollections.observableArrayList();

    private ObservableList<NovelGenre> genres = FXCollections.observableArrayList();

    private ObservableList<NovelCategory> categories = FXCollections.observableArrayList();

    private ObjectProperty<Novel> selectedNovel = new SimpleObjectProperty<>();

    private HashMap<NovelGenre, List<NovelCategory>> genreCategoryMap = new HashMap<>();


    public NovelScreenVM() {
        novels.addAll(NovelsDao.getNovels());
        genres.addAll(NovelsDao.getGenres());
        authors.addAll(NovelAuthorDao.getAuthors());
        categories.addAll(NovelsDao.getCategories());

        genres.forEach(novelGenre -> {
            genreCategoryMap.put(novelGenre, categories);
        });

    }

    public ObservableList<Novel> getNovels(NovelCategory category, NovelGenre genre) {
        ObservableList<Novel> novelList = FXCollections.observableArrayList();
        for (Novel novel : novels) {
            if (novel.getCategoryId() == category.getId() && novel.getGenreId() == genre.getId()) {
                novelList.add(novel);
            }
        }
        return novelList;
    }

    public ObservableList<NovelAuthor> getAuthors() {
        return authors;
    }

    public ObservableList<NovelGenre> getGenres() {
        return genres;
    }

    public HashMap<NovelGenre, List<NovelCategory>> getGenreCategoryMap() {
        return genreCategoryMap;
    }

    public void setSelectedNovel(Novel selectedNovel) {
        this.selectedNovel.set(selectedNovel);
    }

    public String getAuthor(Novel selectedNovel) {
        for (NovelAuthor author : authors) {
            if (author.getNovelId() == selectedNovel.getId()) {
                return author.getName();
            }
        }
        return null;
    }


    public Novel getSelectedNovel() {
        return selectedNovel.get();
    }

    public ObjectProperty<Novel> selectedNovelProperty() {
        return selectedNovel;
    }

}
