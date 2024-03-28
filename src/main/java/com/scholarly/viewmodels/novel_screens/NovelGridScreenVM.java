package com.scholarly.viewmodels.novel_screens;

import com.scholarly.controller.novel_screens.NovelGridScreenController;
import com.scholarly.data.model.novels.NovelModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NovelGridScreenVM implements ViewModel {

    private ObservableList<NovelModel> novelModels = FXCollections.observableArrayList();

    private SimpleStringProperty novelType = new SimpleStringProperty();


    public void processInitialData(NovelGridScreenController.InitialData data) {
        novelType.set(data.getCategoryGenreTitle());
        novelModels.addAll(data.getNovelModels());
        /*Pair<String, ObservableList<Novel>> novelPair = (Pair<String, ObservableList<Novel>>) data;
        novelType.set(getNovelName(novelPair.getKey()));
        ObservableList<Novel> novelList = novelPair.getValue();
        novels.addAll(novelList);*/
    }

    public ObservableList<NovelModel> getNovelModels() {
        return novelModels;
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
