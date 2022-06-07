package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.model.novels.Novel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.stream.Collectors;

public class NovelGridScreenVM implements ViewModel {

    private ObservableList<Novel> novels = FXCollections.observableArrayList();

    private SimpleStringProperty novelType = new SimpleStringProperty();


    public void processInitialData(Object data) {
        Pair<String, ObservableList<Novel>> novelPair = (Pair<String, ObservableList<Novel>>) data;
        novelType.set(getNovelName(novelPair.getKey()));
        ObservableList<Novel> novelList = novelPair.getValue();
        novels.addAll(novelList);
    }

    public ObservableList<Novel> getNovels() {
        return novels;
    }

    public String getNovelType() {
        return novelType.get();
    }

    private String getNovelName(String key) {
        return switch (key) {
            case "jambProseButton" -> "JAMB Prose";
            case "africanProseButton" -> "African Prose";
            case "nonAfricanProseButton" -> "Non African Prose";
            case "africanDramaButton" -> "African Drama";
            case "nonAfricanDramaButton" -> "Non African Drama";
            case "shakespeareanTextButton" -> "Shakespearean Text";
            case "africanPoetryButton" -> "African Poetry";
            default -> "Non African Poetry";
        };
    }

}
