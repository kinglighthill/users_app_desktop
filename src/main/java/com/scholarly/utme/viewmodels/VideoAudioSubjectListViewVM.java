package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.SubjectDao;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class VideoAudioSubjectListViewVM implements ViewModel {

    private ObservableList<VideoAudioSubjectListItemVM> subjects = FXCollections.observableArrayList();

    public VideoAudioSubjectListViewVM() {
        subjects.addAll(SubjectDao.getSubjects().stream().map(VideoAudioSubjectListItemVM::new).collect(Collectors.toList()));
    }

    public ObservableList<VideoAudioSubjectListItemVM> getSubjects() {
        return subjects;
    }
}
