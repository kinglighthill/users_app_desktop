package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.data.dao.NovelAuthorDao;
import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.novels.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.List;

public class NovelScreenVM implements ViewModel {
    private static final String TAG = "NovelScreenVM: ";

    private ObservableList<Novel> novels = FXCollections.observableArrayList();

    private ObservableList<NovelAuthor> authors = FXCollections.observableArrayList();

    private ObservableList<NovelGenre> genres = FXCollections.observableArrayList();

    private ObservableList<NovelCategory> categories = FXCollections.observableArrayList();

    private ObjectProperty<NovelModel> selectedNovelModel = new SimpleObjectProperty<>();

    private HashMap<NovelGenre, List<NovelCategory>> genreCategoryMap = new HashMap<>();

    private ObservableMap<NovelCategoryGenre, List<NovelModel>> genresCategories = FXCollections.observableHashMap();


    public NovelScreenVM() {
        novels.addAll(NovelsDao.getNovels());
        genres.addAll(NovelsDao.getGenres());
        authors.addAll(NovelAuthorDao.getAuthors());
        categories.addAll(NovelsDao.getCategories());
        genresCategories.putAll(NovelsDao.getGenresCategories());

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

    public ObservableMap<NovelCategoryGenre, List<NovelModel>> getGenresCategories() {
        return genresCategories;
    }

    public void setSelectedNovelModel(NovelModel selectedNovelModel) {
        this.selectedNovelModel.set(selectedNovelModel);
    }

    public NovelAuthor getAuthor(Novel selectedNovel) {
        for (NovelAuthor author : authors) {
            if (author.getNovelId() == selectedNovel.getId()) {
                return author;
            }
        }
        return null;
    }


    public NovelModel getSelectedNovelModel() {
        return selectedNovelModel.get();
    }

    public ObjectProperty<NovelModel> selectedNovelModelProperty() {
        return selectedNovelModel;
    }

}
