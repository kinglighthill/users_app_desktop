package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.SubjectDao;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class VideoAudioSubjectListViewVM implements ViewModel {

    private ObservableList<VideoAudioSubjectListItemVM> subjects = FXCollections.observableArrayList();

    private Type type;

    public VideoAudioSubjectListViewVM() {
        subjects.addAll(SubjectDao.getSubjects().stream().map(VideoAudioSubjectListItemVM::new).collect(Collectors.toList()));
    }

    public ObservableList<VideoAudioSubjectListItemVM> getSubjects() {
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
