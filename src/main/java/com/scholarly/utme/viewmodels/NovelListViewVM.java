package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.novelsDb.Novel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class NovelListViewVM implements ViewModel {

    private ObservableList<Novel> novels = FXCollections.observableArrayList();

    public NovelListViewVM() {
        novels.addAll(NovelsDao.getNovels().stream().limit(5).collect(Collectors.toList()));
    }

    public ObservableList<Novel> getNovels() {
        return novels;
    }
}
