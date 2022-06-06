package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.model.novels.Novel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.net.NetworkInterface;

public class NovelChapterListVM implements ViewModel {

    private SimpleObjectProperty<Novel> novelProperty = new SimpleObjectProperty<>();


    public void processInitialData(Novel novel) {
        novelProperty.set(novel);
        System.out.println("Got novel with name -> " + novel.getName());
    }

    public Novel getNovel() {
        return novelProperty.get();
    }
}
