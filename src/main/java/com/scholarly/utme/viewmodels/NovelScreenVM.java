package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.NovelAuthorDao;
import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.Novel.Genre;
import com.scholarly.utme.data.model.novels.Novel.Type;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.stream.Collectors;

public class NovelScreenVM implements ViewModel {

    private ObservableList<Novel> novels = FXCollections.observableArrayList();

    private ObservableList<NovelAuthor> authors = FXCollections.observableArrayList();

    private HashMap<String, NovelState> novelStateHashMap = new HashMap<>();

    private ObjectProperty<Novel> selectedNovel = new SimpleObjectProperty<>();

    private SimpleStringProperty novelDescription = new SimpleStringProperty();

    private SimpleStringProperty chaptersCount = new SimpleStringProperty();

    public NovelScreenVM() {
        novels.addAll(NovelsDao.getNovels());

        authors.addAll(NovelAuthorDao.getAuthors());

    }

    public ObservableList<Novel> getNovels() {
        return novels;
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

    public ObservableList<Novel> getNovels(Type type, Genre genre) {
        ObservableList<Novel> novelList = FXCollections.observableArrayList();
        for (Novel novel : novels) {
            if (novel.getTypeId() == type.getId() && novel.getGenreId() == genre.getId()) {
                System.out.println("Novel -> " + novel.getName());
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

    public void setNovelDescription(Novel selectedNovel) {
        this.novelDescription.set(selectedNovel.getAbout());
    }

    public ObjectProperty<Novel> selectedNovelProperty() {
        return selectedNovel;
    }

    public SimpleStringProperty novelDescriptionProperty() {
        return novelDescription;
    }

    public void setChaptersCount(Novel selectedNovel) {
        this.chaptersCount.set(selectedNovel.getChaptersCount() + " chapters");
    }

    public SimpleStringProperty chaptersCountProperty() {
        return chaptersCount;
    }


    public static class NovelState {
        private Novel novel;
        private NovelAuthor author;

        public NovelState(Novel novel, NovelAuthor author) {
            this.novel = novel;
            this.author = author;
        }

        public Novel getNovel() {
            return novel;
        }

        public void setNovel(Novel novel) {
            this.novel = novel;
        }

        public NovelAuthor getAuthor() {
            return author;
        }

        public void setAuthor(NovelAuthor author) {
            this.author = author;
        }
    }
}
