package com.scholarly.utme.viewmodels.novel_screens;

import com.scholarly.utme.data.dao.NovelAuthorDao;
import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.novels.*;
import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class NovelScreenVM implements ViewModel {
    private static final String TAG = "NovelScreenVM: ";

    private final ObservableList<Novel> novels = FXCollections.observableArrayList();

    private final ObservableList<NovelAuthor> authors = FXCollections.observableArrayList();

    private final ObservableList<NovelGenre> genres = FXCollections.observableArrayList();

    private final ObservableList<NovelCategory> categories = FXCollections.observableArrayList();

    private final ObjectProperty<NovelModel> selectedNovelModel = new SimpleObjectProperty<>();

    private final HashMap<NovelGenre, List<NovelCategory>> genreCategoryMap = new HashMap<>();

    private final ObservableMap<NovelCategoryGenre, List<NovelModel>> genresCategories = FXCollections.observableHashMap();

    private String userId;

    private final SimpleBooleanProperty novelsLoaded = new SimpleBooleanProperty();

    public NovelScreenVM() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Task<Boolean> novelsTask = new Task<>() {
            @Override
            protected Boolean call() {
                userId = PreferencesManager.get(PREF_KEY_USER_ID, "");

                novels.addAll(NovelsDao.getNovels());
                genres.addAll(NovelsDao.getGenres());
                authors.addAll(NovelAuthorDao.getAuthors());
                categories.addAll(NovelsDao.getCategories());
                genresCategories.putAll(NovelsDao.getGenresCategories());

                genres.forEach(novelGenre -> genreCategoryMap.put(novelGenre, categories));
                return true;
            }
        };
        novelsLoaded.bind(novelsTask.valueProperty());

        executorService.execute(novelsTask);
        executorService.shutdown();
    }

    public SimpleBooleanProperty getNovelsLoaded() {
        return novelsLoaded;
    }

    public String getUserId() {
        return userId;
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
