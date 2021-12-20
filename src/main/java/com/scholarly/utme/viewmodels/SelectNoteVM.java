package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.TopicDao;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.Topic;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SelectNoteVM implements ViewModel {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private HashMap<String, ObservableList<Topic>> subjectTopics = new HashMap<>();

    private SimpleObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<Topic> selectedTopic = new SimpleObjectProperty<>(null);

    public SelectNoteVM() {

        ObservableList<Subject> subjectList = SubjectDao.getSubjects();

        subjects.addAll(subjectList);

        subjects.forEach(subject -> {
            subjectTopics.put(subject.getSubjectName(), TopicDao.getTopics(subject.getTableName() + "_topics"));
        });
    }


    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public HashMap<String, ObservableList<Topic>> getSubjectTopics() {
        return subjectTopics;
    }

    public Subject getSelectedSubject() {
        return selectedSubject.get();
    }

    public SimpleObjectProperty<Subject> selectedSubjectProperty() {
        return selectedSubject;
    }

    public Topic getSelectedTopic() {
        return selectedTopic.get();
    }

    public SimpleObjectProperty<Topic> selectedTopicProperty() {
        return selectedTopic;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }

    public void setSelectedTopic(Topic selectedTopic) {
        this.selectedTopic.set(selectedTopic);
    }
}
