package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.novels.Novel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NovelGridScreenVM implements ViewModel {

    private ObservableList<Novel> novels = FXCollections.observableArrayList();

    public NovelGridScreenVM() {
        novels.addAll(NovelsDao.getNovels());
    }

    public ObservableList<Novel> getNovels() {
        return novels;
    }
}
