package com.scholarly.utme.viewmodels.audio_video_screens;

import com.scholarly.utme.data.dao.SubjectDao;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class AudioVideoSubjectListViewVM implements ViewModel {

    private ObservableList<AudioVideoSubjectListItemVM> subjects = FXCollections.observableArrayList();

    private Type type;

    public AudioVideoSubjectListViewVM() {
        subjects.addAll(SubjectDao.getSubjects().stream().map(AudioVideoSubjectListItemVM::new).collect(Collectors.toList()));
    }

    public ObservableList<AudioVideoSubjectListItemVM> getSubjects() {
        return subjects;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }


    public enum Type {
        VIDEO,
        AUDIO
    }
}
