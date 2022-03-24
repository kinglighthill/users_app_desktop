package com.scholarly.utme.viewmodels.landing_screen;

import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.model.Subject;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class LandingScreenHomeVM implements ViewModel {

    private final ObservableList<String> recentlyViewedSubjects = FXCollections.observableArrayList();

    public LandingScreenHomeVM(){

        // Get the first four subjects from Subjects table in the database
        recentlyViewedSubjects.addAll(SubjectDao.getSubjects().stream().map(Subject::getSubjectName).limit(4).collect(Collectors.toList()));

        for (String subjectName : recentlyViewedSubjects) {

            System.out.println("Subjects in DB: " + subjectName);
        }

    }

    public ObservableList<String> getRecentlyViewedSubjects() {
        return recentlyViewedSubjects;
    }
}
